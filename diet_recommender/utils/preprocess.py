import pandas as pd
import numpy as np
from typing import Tuple

RECIPE_PATH = "..\\DataSets\\All_Diets.csv"
PATIENT_PATH = "..\\DataSets\\diet_recommendations_dataset.csv"


def load_recipes(path: str = RECIPE_PATH) -> pd.DataFrame:
    df = pd.read_csv(path)
    return df


def clean_recipes(df: pd.DataFrame) -> pd.DataFrame:
    # Normalize column names
    df = df.rename(columns=lambda c: c.strip())
    # Ensure macro columns are numeric
    for col in ["Protein(g)", "Carbs(g)", "Fat(g)"]:
        if col in df.columns:
            df[col] = pd.to_numeric(df[col], errors='coerce')
        else:
            df[col] = np.nan

    # Plausibility: set huge values to NaN (e.g., > 1000 g)
    for col in ["Protein(g)", "Carbs(g)", "Fat(g)"]:
        df.loc[df[col] > 1000, col] = np.nan
        df.loc[df[col] < 0, col] = np.nan

    # Compute calories
    df["calories"] = (df["Protein(g)"].fillna(0) + df["Carbs(g)"].fillna(0)) * 4 + df["Fat(g)"].fillna(0) * 9

    # Simple dedup and normalize text
    if "Recipe_name" in df.columns:
        df["Recipe_name"] = df["Recipe_name"].astype(str).str.strip()
        df = df.drop_duplicates(subset=["Recipe_name"])

    return df


def load_patients(path: str = PATIENT_PATH) -> pd.DataFrame:
    df = pd.read_csv(path)
    return df


def compute_bmr(row: pd.Series) -> float:
    # Mifflin-St Jeor
    if row.get("Gender", "Male").strip().lower().startswith('m'):
        s = 5
    else:
        s = -161
    return 10 * row["Weight_kg"] + 6.25 * row["Height_cm"] - 5 * row["Age"] + s


def compute_daily_needs(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()

    def activity_factor(level: str) -> float:
        if pd.isna(level):
            return 1.2
        lvl = str(level).strip().lower()
        if "sedentary" in lvl:
            return 1.2
        if "moderate" in lvl:
            return 1.55
        if "active" in lvl:
            return 1.725
        return 1.375

    df["BMR"] = df.apply(compute_bmr, axis=1)
    df["activity_factor"] = df["Physical_Activity_Level"].apply(activity_factor)
    df["estimated_daily_calories"] = (df["BMR"] * df["activity_factor"]).round(0)

    # If Daily_Caloric_Intake present and valid, prefer that
    df["Daily_Caloric_Intake"] = pd.to_numeric(df.get("Daily_Caloric_Intake", pd.Series(np.nan)), errors='coerce')
    df["target_calories"] = df["Daily_Caloric_Intake"].fillna(df["estimated_daily_calories"])

    return df


if __name__ == '__main__':
    recipes = load_recipes()
    print(f"Loaded recipes: {len(recipes)}")
    recipes = clean_recipes(recipes)
    print(f"Clean recipes: {len(recipes)}; sample cols: {list(recipes.columns)}")

    patients = load_patients()
    print(f"Loaded patients: {len(patients)}")
    patients = compute_daily_needs(patients)
    print(patients[["Patient_ID", "target_calories", "estimated_daily_calories"]].head())
