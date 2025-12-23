from typing import List, Dict
import pandas as pd
import numpy as np
from collections import Counter


def score_recipe_for_patient(recipe: pd.Series, patient: pd.Series, meal_cal_target: float, cuisine_bonus=1.2) -> float:
    # Calorie fit: smaller absolute difference is better
    cal_diff = abs(recipe.get('calories', 0) - meal_cal_target)
    cal_score = max(0, 1 - (cal_diff / max(1, meal_cal_target)))

    # Cuisine match
    cuisine_match = 1.0
    if 'Preferred_Cuisine' in patient and pd.notna(patient['Preferred_Cuisine']):
        try:
            pref = str(patient['Preferred_Cuisine']).strip().lower()
            if pref and 'Cuisine_type' in recipe and isinstance(recipe['Cuisine_type'], str) and pref in recipe['Cuisine_type'].lower():
                cuisine_match = cuisine_bonus
        except Exception:
            cuisine_match = 1.0

    # Macro match: compare macro percents vs generic target by diet recommendation
    # For prototype, simple distance on protein/carbs/fat ratios
    macro_score = 1.0
    if 'Diet_Recommendation' in patient and pd.notna(patient['Diet_Recommendation']):
        diet = str(patient['Diet_Recommendation']).lower()
        # rough targets
        if 'low_carb' in diet:
            target_carbs_pct = 0.25
        elif 'low_sodium' in diet:
            target_carbs_pct = 0.45
        else:
            target_carbs_pct = 0.45
        # compute recipe carb pct
        p = recipe.get('Protein(g)', 0) * 4
        c = recipe.get('Carbs(g)', 0) * 4
        f = recipe.get('Fat(g)', 0) * 9
        tot = max(1, p + c + f)
        carb_pct = c / tot
        macro_score = max(0, 1 - abs(carb_pct - target_carbs_pct))

    # Combine
    score = (0.6 * cal_score + 0.4 * macro_score) * cuisine_match
    return score


def filter_by_allergies_and_restrictions(recipes: pd.DataFrame, patient: pd.Series) -> pd.DataFrame:
    df = recipes.copy()
    # Basic filtering using the textual Diet_type and Recipe_name fields
    allergies = str(patient.get('Allergies', '')).lower()
    restrictions = str(patient.get('Dietary_Restrictions', '')).lower()

    import json
    from pathlib import Path

    SYN_PATH = Path(__file__).parent / 'data' / 'ingredient_synonyms.json'
    SYN_MAP = {}
    if SYN_PATH.exists():
        try:
            SYN_MAP = json.loads(SYN_PATH.read_text(encoding='utf8'))
        except Exception:
            SYN_MAP = {}

    def tokenize(s: str):
        import re
        return [t for t in re.findall(r"[a-zA-Z]+", s.lower())]

    def simple_stem(token: str):
        # very small stemmer: remove common plural -s, -es
        if token.endswith('ies'):
            return token[:-3] + 'y'
        if token.endswith('es'):
            return token[:-2]
        if token.endswith('s'):
            return token[:-1]
        return token

    def violates(row):
        # Check multiple fields for food items
        text_fields = [
            str(row.get('Recipe_name', '')),
            str(row.get('Diet_type', '')),
            str(row.get('Cuisine_type', '')),
            str(row.get('Ingredients', '')),  # Check ingredients!
            str(row.get('ingredients', '')),  # Alternative column name
            str(row.get('description', '')),  # Sometimes descriptions have ingredients
            str(row.get('Description', ''))
        ]
        text = ' '.join(text_fields).lower()
        tokens = [simple_stem(t) for t in tokenize(text)]
        
        for a in [s.strip() for s in allergies.split(',') if s.strip()]:
            if not a or a == 'none':
                continue
            atoks = [simple_stem(t) for t in tokenize(a)]
            # expand allergy tokens using synonym map
            expanded = set(atoks)
            for key, vals in SYN_MAP.items():
                for v in vals:
                    if v in atoks:
                        expanded.update([simple_stem(x) for x in vals])
            # if any expanded allergy token appears in recipe tokens -> exclude
            if any(ex in tokens for ex in expanded):
                return True
        for r in [s.strip() for s in restrictions.split(',') if s.strip()]:
            if not r or r == 'none':
                continue
            rtoks = [simple_stem(t) for t in tokenize(r)]
            # treat explicit ingredient restrictions like allergies (exclude)
            if any(rr in tokens for rr in rtoks):
                return True
        return False

    mask = df.apply(violates, axis=1)
    return df[~mask]


def recommend_top_n(recipes: pd.DataFrame, patient: pd.Series, n: int = 20, meals_per_day: int = 3, diet_label_probs: Dict[str, float] = None) -> pd.DataFrame:
    """
    Recommend top N recipes for a patient.

    If diet_label_probs is provided (mapping from diet label string -> probability), recipes that match a label
    will receive a multiplicative boost proportional to that probability.
    """
    meal_cal = patient.get('target_calories', 2000) / float(meals_per_day)
    cand = filter_by_allergies_and_restrictions(recipes, patient)

    scores = []
    boosts = []
    for _, r in cand.iterrows():
        s = score_recipe_for_patient(r, patient, meal_cal)
        boost = 1.0
        if diet_label_probs and isinstance(diet_label_probs, dict):
            # compute a boost by checking if any diet label text appears in the recipe's Diet_type or Recipe_name
            text = ' '.join([str(r.get('Diet_type','')), str(r.get('Recipe_name',''))]).lower()
            match_prob = 0.0
            for label, prob in diet_label_probs.items():
                if label and label.replace('_',' ').lower() in text:
                    match_prob += float(prob)
            # scale boost into [1, 1 + match_prob]
            boost = 1.0 + match_prob
        scores.append(s)
        boosts.append(boost)
    cand = cand.copy()
    cand['base_score'] = scores
    cand['boost'] = boosts
    cand['score'] = cand['base_score'] * cand['boost']
    return cand.sort_values('score', ascending=False).head(n)


if __name__ == '__main__':
    import pandas as pd
    recs = pd.read_csv('..\\DataSets\\All_Diets.csv')
    pts = pd.read_csv('..\\DataSets\\diet_recommendations_dataset.csv')
    pt = pts.iloc[0]
    top = recommend_top_n(recs, pt, n=5)
    print(top[['Recipe_name','Cuisine_type','calories','score']])
