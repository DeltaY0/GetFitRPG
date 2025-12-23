import os
import json
import pandas as pd
import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))

from ai.model_utils import load_model
from utils.preprocess import load_recipes, clean_recipes, load_patients, compute_daily_needs
from ai.recommend import recommend_top_n, filter_by_allergies_and_restrictions
from ai.planner import make_30_day_plan
try:
    from ai.planner_ilp import make_plan_ilp
except Exception:
    make_plan_ilp = None

MODEL_DIR = "models"


def load_artifacts(model_dir=MODEL_DIR):
    clf = load_model(os.path.join(model_dir, 'rf_diet_recommender.joblib'))
    le = load_model(os.path.join(model_dir, 'label_encoder.joblib'))
    with open(os.path.join(model_dir, 'feature_cols.json'), 'r', encoding='utf8') as fh:
        feature_cols = json.load(fh)
    # load categorical encoders if present
    encoders = {}
    for name in ['Gender', 'Disease_Type', 'Physical_Activity_Level']:
        path = os.path.join(model_dir, f'{name}_encoder.joblib')
        if os.path.exists(path):
            encoders[name] = load_model(path)
    return clf, le, feature_cols, encoders


def patient_row_to_features(patient_row: pd.Series, feature_cols, encoders):
    p = patient_row.copy()
    p = compute_daily_needs(pd.DataFrame([p])).iloc[0]
    # basic numeric conversions
    X = {}
    for c in ['Age','Weight_kg','Height_cm','BMI','Weekly_Exercise_Hours','Adherence_to_Diet_Plan','Dietary_Nutrient_Imbalance_Score','target_calories']:
        X[c] = float(p.get(c, 0))
    # categorical encoding
    for name, enc in encoders.items():
        val = str(p.get(name, ''))
        try:
            X[name + '_enc'] = int(enc.transform([val])[0])
        except Exception:
            # unseen label -> map to 0
            X[name + '_enc'] = 0
    # ensure order
    return pd.DataFrame([X])[feature_cols]


def format_plan_output(patient, plans):
    lines = []
    lines.append(f"Patient: {patient.get('Patient_ID','<unknown>')} | Target calories: {patient.get('target_calories')}")
    lines.append("Dietary restrictions: " + str(patient.get('Dietary_Restrictions','None')))
    lines.append("Allergies: " + str(patient.get('Allergies','None')))
    lines.append('\n30-day meal plan:')
    for i, day in enumerate(plans):
        day_total = day['calories'].sum() if not day.empty else 0
        lines.append(f"\nDay {i+1} - total {day_total:.0f} kcal")
        for _, r in day.iterrows():
            lines.append(f"  - {r['Recipe_name']} ({r.get('Cuisine_type', '')}) - {r['calories']:.0f} kcal")
    return '\n'.join(lines)


def predict_and_recommend(patient_index: int = 0, days: int = 30, meals_per_day: int = 3, output_json: bool = False, out_path: str = None, no_repeat_within_days: int = 7, use_ilp: bool = False):
    clf, le, feature_cols, encoders = load_artifacts()
    patients = load_patients()
    patients = compute_daily_needs(patients)
    patient = patients.iloc[patient_index]

    X = patient_row_to_features(patient, feature_cols, encoders)
    # predict and get probabilities for diet labels
    pred_enc = clf.predict(X)[0]
    pred_label = le.inverse_transform([pred_enc])[0]
    if hasattr(clf, 'predict_proba'):
        probs = clf.predict_proba(X)[0]
        label_probs = {lab: float(p) for lab, p in zip(le.classes_, probs)}
    else:
        label_probs = {pred_label: 1.0}

    # load recipes and filter
    recipes = load_recipes()
    recipes = clean_recipes(recipes)
    # get candidate recommendations; pass model-derived diet label probabilities to boost matching recipes
    candidates = recommend_top_n(recipes, patient, n=1000, diet_label_probs=label_probs)

    # generate plan using prefiltered candidate pool for better quality
    plan_pool = filter_by_allergies_and_restrictions(candidates, patient)
    if use_ilp and make_plan_ilp is not None:
        try:
            plans = make_plan_ilp(plan_pool, patient, days=days, meals_per_day=meals_per_day, no_repeat_within_days=no_repeat_within_days)
        except Exception:
            plans = make_30_day_plan(plan_pool, patient, days=days, meals_per_day=meals_per_day, no_repeat_within_days=no_repeat_within_days)
    else:
        plans = make_30_day_plan(plan_pool, patient, days=days, meals_per_day=meals_per_day, no_repeat_within_days=no_repeat_within_days)

    if output_json or out_path:
        # serialize to JSON
        out = {
            'Patient_ID': patient.get('Patient_ID', ''),
            'target_calories': float(patient.get('target_calories', 0)),
            'Dietary_Restrictions': str(patient.get('Dietary_Restrictions','')),
            'Allergies': str(patient.get('Allergies','')),
            'days': []
        }
        for i, day in enumerate(plans):
            day_entry = {
                'day': i+1,
                'day_total': float(day['calories'].sum()) if not day.empty else 0,
                'meals': []
            }
            for _, r in day.iterrows():
                day_entry['meals'].append({'Recipe_name': r['Recipe_name'], 'Cuisine_type': r.get('Cuisine_type',''), 'calories': float(r['calories'])})
            out['days'].append(day_entry)
        if out_path:
            with open(out_path, 'w', encoding='utf8') as fh:
                import json
                json.dump(out, fh, indent=2)
            print(f"Wrote JSON output to {out_path}")
        if output_json:
            import json
            print(json.dumps(out, indent=2))
        return out

    output = format_plan_output(patient, plans)
    print(output)


if __name__ == '__main__':
    predict_and_recommend(0)
