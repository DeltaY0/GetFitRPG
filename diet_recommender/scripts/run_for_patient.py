import sys
import argparse
from predict_and_recommend import predict_and_recommend


def main():
    p = argparse.ArgumentParser(description='Run recommender for a patient')
    p.add_argument('patient', nargs='?', default='0', help='Patient index (0-based) or Patient_ID')
    p.add_argument('--days', type=int, default=30, help='Number of days to generate')
    p.add_argument('--meals', type=int, default=3, help='Meals per day')
    p.add_argument('--json', action='store_true', help='Print JSON output')
    p.add_argument('--out', type=str, help='Write JSON output to file')
    p.add_argument('--no-repeat-days', type=int, default=7, help='Do not repeat same recipe within these many days')
    args = p.parse_args()

    arg = args.patient
    # try numeric index first
    try:
        idx = int(arg)
        predict_and_recommend(idx, days=args.days, meals_per_day=args.meals, output_json=args.json, out_path=args.out, no_repeat_within_days=args.no_repeat_days)
        return
    except Exception:
        pass
    # try Patient_ID
    from preprocess import load_patients, compute_daily_needs
    pts = load_patients()
    pts = compute_daily_needs(pts)
    matches = pts[pts['Patient_ID'] == arg]
    if not matches.empty:
        i = matches.index[0]
        predict_and_recommend(int(i), days=args.days, meals_per_day=args.meals, output_json=args.json, out_path=args.out, no_repeat_within_days=args.no_repeat_days)
        return
    print(f"Patient '{arg}' not found. Provide numeric index or Patient_ID.")


if __name__ == '__main__':
    main()
