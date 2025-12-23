import json
from predict_and_recommend import predict_and_recommend

if __name__ == '__main__':
    # patient index 1 is P0002 in the dataset used earlier
    out = predict_and_recommend(1, days=5, meals_per_day=3, output_json=True, use_ilp=True, no_repeat_within_days=3)
    print(json.dumps(out, indent=2))
