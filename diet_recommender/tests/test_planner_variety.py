import pandas as pd
from planner import make_30_day_plan


def test_planner_variety():
    # create 10 recipes with distinct names and calories around 500
    recipes = pd.DataFrame([{"Recipe_name":f"r{i}", "calories":500} for i in range(10)])
    patient = pd.Series({'target_calories':1500})
    plans = make_30_day_plan(recipes, patient, days=5, meals_per_day=3)
    # count total unique recipes used
    used = set()
    for day in plans:
        for _, r in day.iterrows():
            used.add(r['Recipe_name'])
    assert len(used) >= 5  # across 5 days we should use at least 5 different recipes
