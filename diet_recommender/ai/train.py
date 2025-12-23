import pandas as pd
import numpy as np
from typing import Tuple
import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))

from utils.preprocess import compute_daily_needs
from ai.model_utils import save_model, fit_label_encoder
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.metrics import classification_report
from sklearn.preprocessing import LabelEncoder
import os
import json

PATIENT_PATH = "..\\DataSets\\diet_recommendations_dataset.csv"
MODEL_DIR = "models"
os.makedirs(MODEL_DIR, exist_ok=True)


def featurize(df: pd.DataFrame) -> Tuple[pd.DataFrame, pd.Series, dict]:
    df = df.copy()
    # basic numeric features
    df['Age'] = pd.to_numeric(df['Age'], errors='coerce').fillna(df['Age'].median())
    df['Weight_kg'] = pd.to_numeric(df['Weight_kg'], errors='coerce').fillna(df['Weight_kg'].median())
    df['Height_cm'] = pd.to_numeric(df['Height_cm'], errors='coerce').fillna(df['Height_cm'].median())
    df['BMI'] = pd.to_numeric(df['BMI'], errors='coerce').fillna(df['BMI'].median())
    df['Weekly_Exercise_Hours'] = pd.to_numeric(df['Weekly_Exercise_Hours'], errors='coerce').fillna(0)
    df['Adherence_to_Diet_Plan'] = pd.to_numeric(df['Adherence_to_Diet_Plan'], errors='coerce').fillna(df['Adherence_to_Diet_Plan'].median())
    df['Dietary_Nutrient_Imbalance_Score'] = pd.to_numeric(df['Dietary_Nutrient_Imbalance_Score'], errors='coerce').fillna(0)

    # compute/ensure target_calories
    df = compute_daily_needs(df)
    df['target_calories'] = pd.to_numeric(df['target_calories'], errors='coerce').fillna(df['estimated_daily_calories'])

    # categorical: Gender, Disease_Type, Physical_Activity_Level
    df['Gender'] = df['Gender'].fillna('Unknown')
    df['Disease_Type'] = df['Disease_Type'].fillna('None')
    df['Physical_Activity_Level'] = df['Physical_Activity_Level'].fillna('Unknown')

    # label encode categories with sklearn and save encoders later
    cats = ['Gender', 'Disease_Type', 'Physical_Activity_Level']
    encoders = {}
    for c in cats:
        le = LabelEncoder()
        df[c+'_enc'] = le.fit_transform(df[c].astype(str))
        encoders[c] = le

    # target label
    y = df['Diet_Recommendation'].fillna('Balanced')

    feature_cols = ['Age', 'Weight_kg', 'Height_cm', 'BMI', 'Weekly_Exercise_Hours', 'Adherence_to_Diet_Plan', 'Dietary_Nutrient_Imbalance_Score', 'target_calories'] + [c + '_enc' for c in cats]
    X = df[feature_cols]
    return X, y, {'feature_cols': feature_cols, 'encoders': encoders}


def train_and_save(grid_search: bool = True):
    df = pd.read_csv(PATIENT_PATH)
    X, y, meta = featurize(df)
    print('Features shape:', X.shape)

    # encode labels
    le = LabelEncoder()
    y_filled = y.fillna('Balanced').astype(str)
    y_enc = le.fit_transform(y_filled)
    save_model(le, os.path.join(MODEL_DIR, 'label_encoder.joblib'))

    # persist encoders for categorical features
    for name, encoder in meta['encoders'].items():
        save_model(encoder, os.path.join(MODEL_DIR, f'{name}_encoder.joblib'))

    # also save feature column list
    with open(os.path.join(MODEL_DIR, 'feature_cols.json'), 'w', encoding='utf8') as fh:
        json.dump(meta['feature_cols'], fh)

    X_train, X_test, y_train, y_test = train_test_split(X, y_enc, test_size=0.2, random_state=42, stratify=y_enc)

    if grid_search:
        param_grid = {
            'n_estimators': [100, 200],
            'max_depth': [None, 10, 20]
        }
        base = RandomForestClassifier(random_state=42, n_jobs=-1)
        gs = GridSearchCV(base, param_grid, cv=3, n_jobs=-1, verbose=1)
        gs.fit(X_train, y_train)
        clf = gs.best_estimator_
        print('Best params:', gs.best_params_)
    else:
        clf = RandomForestClassifier(n_estimators=200, random_state=42, n_jobs=-1)
        clf.fit(X_train, y_train)

    # If we used GridSearchCV, ensure clf is fitted
    if not hasattr(clf, 'predict'):
        clf.fit(X_train, y_train)

    preds = clf.predict(X_test)
    print(classification_report(y_test, preds, target_names=le.classes_))

    save_model(clf, os.path.join(MODEL_DIR, 'rf_diet_recommender.joblib'))
    print('Saved model and encoders to models/')


if __name__ == '__main__':
    train_and_save(grid_search=True)
