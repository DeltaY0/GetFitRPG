from typing import List
import pandas as pd
import numpy as np


def make_30_day_plan(recipes: pd.DataFrame, patient: pd.Series, days: int = 30, meals_per_day: int = 3, no_repeat_within_days: int = 7, seed: int = 0) -> List[pd.DataFrame]:
    """Make a multi-day plan from candidate recipes.

    no_repeat_within_days: do not repeat the same recipe within this many days (soft constraint enforced by blocking recent picks)
    seed: for deterministic selection when scores tie
    """
    import random
    random.seed(seed)

    target = float(patient.get('target_calories', 2000))
    per_meal = target / meals_per_day

    pool = recipes.copy()
    pool = pool.dropna(subset=['Recipe_name'])

    plans = []
    # track last day used index of each recipe_name
    last_used_day = {}

    for d in range(days):
        day_picks = []
        # create a fresh candidate pool for the day's selection
        day_pool = pool.copy()
        for m in range(meals_per_day):
            if day_pool.empty:
                break
            day_pool = day_pool.copy()
            # score by absolute difference
            day_pool['abs_diff'] = (day_pool['calories'] - per_meal).abs()
            # penalize if used within recent days
            def recent_penalty(r):
                name = r if isinstance(r, str) else str(r)
                if name in last_used_day:
                    days_since = d - last_used_day[name]
                    if days_since < no_repeat_within_days:
                        return 1 + (no_repeat_within_days - days_since)  # larger penalty the more recent
                return 1

            day_pool['repeat_penalty'] = day_pool['Recipe_name'].map(recent_penalty)
            day_pool['weighted_score'] = day_pool['abs_diff'] * day_pool['repeat_penalty']
            day_pool = day_pool.sort_values(['weighted_score', 'Recipe_name'])
            # pick top candidate; break ties by random choice among top-k close ones
            best_score = day_pool['weighted_score'].iloc[0]
            topk = day_pool[day_pool['weighted_score'] <= best_score * 1.05]
            if len(topk) > 1:
                pick_idx = random.choice(list(topk.index))
                pick = day_pool.loc[pick_idx]
            else:
                pick = day_pool.iloc[0]

            day_picks.append(pick)
            last_used_day[pick['Recipe_name']] = d
            # remove chosen recipe from day_pool for remaining meals
            day_pool = day_pool[day_pool['Recipe_name'] != pick['Recipe_name']]
        plans.append(pd.DataFrame(day_picks))

    return plans


if __name__ == '__main__':
    import pandas as pd
    recs = pd.read_csv('..\\DataSets\\All_Diets.csv')
    recs['calories'] = (pd.to_numeric(recs.get('Protein(g)', 0)).fillna(0) + pd.to_numeric(recs.get('Carbs(g)', 0)).fillna(0)) * 4 + pd.to_numeric(recs.get('Fat(g)', 0)).fillna(0) * 9
    pts = pd.read_csv('..\\DataSets\\diet_recommendations_dataset.csv')
    pt = pts.iloc[0]
    plans = make_30_day_plan(recs, pt, days=3)
    for i, p in enumerate(plans):
        print(f"Day {i+1} total calories: {p['calories'].sum():.1f}")

