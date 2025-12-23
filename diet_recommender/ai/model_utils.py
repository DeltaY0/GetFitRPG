import joblib
from sklearn.preprocessing import LabelEncoder


def save_model(obj, path: str):
    joblib.dump(obj, path)


def load_model(path: str):
    return joblib.load(path)


def fit_label_encoder(series, save_path=None):
    le = LabelEncoder()
    le.fit(series.fillna(''))
    if save_path:
        save_model(le, save_path)
    return le
