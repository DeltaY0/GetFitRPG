from typing import List
import pandas as pd

# ILP planner using pulp to enforce hard no-repeat constraints

def make_plan_ilp(recipes: pd.DataFrame, patient: pd.Series, days: int = 30, meals_per_day: int = 3, no_repeat_within_days: int = 7):
    try:
        import pulp
    except Exception as e:
        raise RuntimeError('pulp is required for ILP planner. Install with `pip install pulp`.') from e

    # limit candidate pool size for tractability
    pool = recipes.copy().reset_index(drop=True)
    pool = pool.dropna(subset=['Recipe_name'])
    n_recipes = len(pool)
    n_slots = days * meals_per_day

    # compute the per-slot target calories
    target = float(patient.get('target_calories', 2000))
    per_meal = target / meals_per_day

    # precompute cost: abs diff of recipe calories to per_meal
    costs = [abs(float(c) - per_meal) for c in pool['calories']]

    # decision vars x[i,s] -> recipe i assigned to slot s
    prob = pulp.LpProblem('meal_plan', pulp.LpMinimize)
    x = pulp.LpVariable.dicts('x', ((i, s) for i in range(n_recipes) for s in range(n_slots)), cat='Binary')

    # objective: minimize total cost
    prob += pulp.lpSum([costs[i] * x[(i, s)] for i in range(n_recipes) for s in range(n_slots)])

    # each slot must have exactly one recipe
    for s in range(n_slots):
        prob += pulp.lpSum([x[(i, s)] for i in range(n_recipes)]) == 1

    # optional: avoid assigning same recipe to overlapping slots within no_repeat window
    for i in range(n_recipes):
        for s in range(n_slots):
            for t in range(max(0, s - no_repeat_within_days * meals_per_day), min(n_slots, s + no_repeat_within_days * meals_per_day + 1)):
                if s != t:
                    # prevent both s and t from using recipe i if within window
                    prob += x[(i, s)] + x[(i, t)] <= 1

    # solve with pulp's default solver
    prob.solve()
    status = pulp.LpStatus.get(prob.status, None) if hasattr(pulp, 'LpStatus') else None
    # pulp may expose LpStatus differently depending on version; fallback to prob.status
    try:
        status_name = pulp.LpStatus[prob.status]
    except Exception:
        status_name = None
    # if solver did not find optimal, raise so caller can fallback
    if status_name is None and status is None:
        pass
    else:
        sname = status_name or status
        if sname != 'Optimal':
            raise RuntimeError(f'ILP solver did not find optimal solution: {sname}')

    # build plan
    plans = []
    for d in range(days):
        day_meals = []
        for m in range(meals_per_day):
            s = d * meals_per_day + m
            chosen = None
            for i in range(n_recipes):
                val = pulp.value(x[(i, s)])
                if val is not None and val > 0.5:
                    chosen = pool.iloc[i]
                    break
            if chosen is not None:
                day_meals.append(chosen)
        plans.append(pd.DataFrame(day_meals))
    return plans
