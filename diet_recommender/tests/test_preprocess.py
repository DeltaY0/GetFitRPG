import pandas as pd
from preprocess import compute_daily_needs


def test_compute_daily_needs_basic():
    df = pd.DataFrame([{"Age":30, "Gender":"Male", "Weight_kg":70, "Height_cm":175, "Physical_Activity_Level":"Moderate"}])
    out = compute_daily_needs(df)
    assert 'estimated_daily_calories' in out.columns
    assert out.iloc[0]['target_calories'] > 0
