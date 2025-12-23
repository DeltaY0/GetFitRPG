from preprocess import load_recipes, clean_recipes, load_patients, compute_daily_needs
from recommend import recommend_top_n
from planner import greedy_daily_plan


def demo(patient_index: int = 0):
    recipes = load_recipes()
    recipes = clean_recipes(recipes)
    patients = load_patients()
    patients = compute_daily_needs(patients)
    patient = patients.iloc[patient_index]

    print("Patient:", patient['Patient_ID'], "target calories:", patient['target_calories'])
    print("Top 5 recipe recommendations:")
    top = recommend_top_n(recipes, patient, n=5)
    print(top[['Recipe_name','Cuisine_type','calories','score']].to_string(index=False))

    print('\nGenerating 3-day plan...')
    plans = greedy_daily_plan(recipes, patient, days=3)
    for i, p in enumerate(plans):
        print(f"\nDay {i+1} (total {p['calories'].sum():.0f} kcal):")
        print(p[['Recipe_name','calories']].to_string(index=False))

if __name__ == '__main__':
    demo(0)
