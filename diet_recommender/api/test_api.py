"""
Test script for Diet Recommender Mobile API
Tests all endpoints with various scenarios
"""
import requests
import json
from typing import Dict, Any


# API Configuration
BASE_URL = "http://localhost:8000"
API_V1 = f"{BASE_URL}/api/v1"


def print_section(title: str):
    """Print formatted section header"""
    print("\n" + "="*80)
    print(f"  {title}")
    print("="*80)


def print_result(test_name: str, success: bool, details: str = ""):
    """Print test result"""
    status = "PASS" if success else "FAIL"
    print(f"\n{status} - {test_name}")
    if details:
        print(f"  Details: {details}")


def test_health_check():
    """Test health check endpoints"""
    print_section("Testing Health Check Endpoints")
    
    try:
        # Test root endpoint
        response = requests.get(BASE_URL)
        success = response.status_code == 200
        print_result("Root endpoint", success, f"Status: {response.status_code}")
        if success:
            print(json.dumps(response.json(), indent=2))
        
        # Test health endpoint
        response = requests.get(f"{BASE_URL}/health")
        success = response.status_code == 200
        print_result("Health endpoint", success, f"Status: {response.status_code}")
        if success:
            data = response.json()
            print(f"  Model loaded: {data.get('model_loaded')}")
            print(f"  Feature count: {data.get('feature_count')}")
            print(f"  Diet types: {data.get('diet_types')}")
        
        return True
    except Exception as e:
        print_result("Health check", False, str(e))
        return False


def test_predict_diet_type():
    """Test diet type prediction endpoint"""
    print_section("Testing Diet Type Prediction")
    
    # Test case 1: Normal adult male
    patient1 = {
        "Age": 30,
        "Weight_kg": 75,
        "Height_cm": 175,
        "Gender": "Male",
        "Disease_Type": "None",
        "Physical_Activity_Level": "Moderate",
        "Weekly_Exercise_Hours": 3.5,
        "Adherence_to_Diet_Plan": 80,
        "Dietary_Nutrient_Imbalance_Score": 10,
        "Dietary_Restrictions": "None",
        "Allergies": "None"
    }
    
    try:
        response = requests.post(f"{API_V1}/predict-diet-type", json=patient1)
        success = response.status_code == 200
        print_result("Predict diet type - Normal adult male", success)
        
        if success:
            data = response.json()
            print(f"  Predicted diet: {data.get('predicted_diet_type')}")
            print(f"  Confidence: {data.get('confidence'):.2%}")
            print(f"  Target calories: {data.get('target_calories'):.0f}")
            print(f"  BMI: {data.get('bmi'):.1f} ({data.get('bmi_category')})")
            print(f"  Top predictions:")
            for diet, prob in list(data.get('all_predictions', {}).items())[:3]:
                print(f"    - {diet}: {prob:.2%}")
        
        return success
    except Exception as e:
        print_result("Predict diet type", False, str(e))
        return False


def test_quick_recommend():
    """Test quick recommendation endpoint"""
    print_section("Testing Quick Recommendation")
    
    quick_request = {
        "age": 25,
        "weight_kg": 70,
        "height_cm": 170,
        "gender": "Female",
        "activity_level": "Active",
        "dietary_restrictions": "Vegetarian",
        "allergies": "Nuts",
        "days": 3
    }
    
    try:
        response = requests.post(f"{API_V1}/quick-recommend", json=quick_request)
        success = response.status_code == 200
        print_result("Quick recommend - Vegetarian with nut allergy", success)
        
        if success:
            data = response.json()
            print(f"  Patient ID: {data.get('patient_id')}")
            print(f"  Target calories: {data.get('target_calories'):.0f}")
            print(f"  Total days: {data.get('total_days')}")
            print(f"  Dietary restrictions: {data.get('dietary_restrictions')}")
            print(f"  Allergies: {data.get('allergies')}")
            
            # Show first day's meals
            if data.get('meal_plan'):
                first_day = data['meal_plan'][0]
                print(f"\n  Day 1 - Total: {first_day['total_calories']:.0f} kcal")
                for i, meal in enumerate(first_day['meals'], 1):
                    print(f"    Meal {i}: {meal['recipe_name']} ({meal['calories']:.0f} kcal)")
        
        return success
    except Exception as e:
        print_result("Quick recommend", False, str(e))
        return False


def test_full_recommendation():
    """Test full recommendation endpoint"""
    print_section("Testing Full Recommendation")
    
    full_request = {
        "patient": {
            "Age": 45,
            "Weight_kg": 85,
            "Height_cm": 178,
            "Gender": "Male",
            "Disease_Type": "Diabetes",
            "Physical_Activity_Level": "Moderate",
            "Weekly_Exercise_Hours": 5,
            "Adherence_to_Diet_Plan": 85,
            "Dietary_Nutrient_Imbalance_Score": 15,
            "Dietary_Restrictions": "Low-Carb",
            "Allergies": "Shellfish",
            "Daily_Caloric_Intake": 2000
        },
        "days": 7,
        "meals_per_day": 3,
        "no_repeat_days": 3
    }
    
    try:
        response = requests.post(f"{API_V1}/recommend", json=full_request)
        success = response.status_code == 200
        print_result("Full recommendation - Diabetic patient", success)
        
        if success:
            data = response.json()
            print(f"  Patient ID: {data.get('patient_id')}")
            print(f"  Target calories: {data.get('target_calories'):.0f}")
            print(f"  Total days: {data.get('total_days')}")
            print(f"  Dietary restrictions: {data.get('dietary_restrictions')}")
            print(f"  Allergies: {data.get('allergies')}")
            
            # Show summary of all days
            print(f"\n  Weekly meal plan summary:")
            for day_plan in data.get('meal_plan', []):
                print(f"    Day {day_plan['day']}: {day_plan['total_calories']:.0f} kcal ({len(day_plan['meals'])} meals)")
            
            # Show detailed first day
            if data.get('meal_plan'):
                first_day = data['meal_plan'][0]
                print(f"\n  Day 1 detailed breakdown:")
                for i, meal in enumerate(first_day['meals'], 1):
                    print(f"    Meal {i}:")
                    print(f"      Recipe: {meal['recipe_name']}")
                    print(f"      Cuisine: {meal['cuisine_type']}")
                    print(f"      Calories: {meal['calories']:.0f}")
                    if meal.get('protein'):
                        print(f"      Protein: {meal['protein']:.1f}g")
                    if meal.get('carbs'):
                        print(f"      Carbs: {meal['carbs']:.1f}g")
                    if meal.get('fat'):
                        print(f"      Fat: {meal['fat']:.1f}g")
        
        return success
    except Exception as e:
        print_result("Full recommendation", False, str(e))
        return False


def test_edge_cases():
    """Test edge cases and error handling"""
    print_section("Testing Edge Cases")
    
    # Test case 1: Invalid age
    invalid_age = {
        "Age": 200,  # Invalid
        "Weight_kg": 70,
        "Height_cm": 170,
        "Gender": "Male"
    }
    
    try:
        response = requests.post(f"{API_V1}/predict-diet-type", json=invalid_age)
        success = response.status_code == 422  # Validation error expected
        print_result("Invalid age validation", success, f"Status: {response.status_code}")
    except Exception as e:
        print_result("Invalid age validation", False, str(e))
    
    # Test case 2: Missing required fields
    missing_fields = {
        "Age": 30
        # Missing other required fields
    }
    
    try:
        response = requests.post(f"{API_V1}/predict-diet-type", json=missing_fields)
        success = response.status_code == 422  # Validation error expected
        print_result("Missing fields validation", success, f"Status: {response.status_code}")
    except Exception as e:
        print_result("Missing fields validation", False, str(e))
    
    # Test case 3: Extreme values
    extreme_values = {
        "age": 18,
        "weight_kg": 45,
        "height_cm": 150,
        "gender": "Female",
        "activity_level": "Sedentary",
        "dietary_restrictions": "Vegan",
        "allergies": "Gluten, Dairy, Soy",
        "days": 1
    }
    
    try:
        response = requests.post(f"{API_V1}/quick-recommend", json=extreme_values)
        success = response.status_code == 200
        print_result("Extreme values (very restrictive diet)", success)
        if success:
            data = response.json()
            print(f"  Successfully generated plan despite restrictions")
            print(f"  Target calories: {data.get('target_calories'):.0f}")
    except Exception as e:
        print_result("Extreme values", False, str(e))


def test_performance():
    """Test API performance with multiple requests"""
    print_section("Testing API Performance")
    
    import time
    
    patient = {
        "age": 30,
        "weight_kg": 70,
        "height_cm": 170,
        "gender": "Male",
        "activity_level": "Moderate",
        "dietary_restrictions": "None",
        "allergies": "None",
        "days": 7
    }
    
    num_requests = 5
    start_time = time.time()
    
    success_count = 0
    for i in range(num_requests):
        try:
            response = requests.post(f"{API_V1}/quick-recommend", json=patient)
            if response.status_code == 200:
                success_count += 1
        except:
            pass
    
    end_time = time.time()
    total_time = end_time - start_time
    avg_time = total_time / num_requests
    
    success = success_count == num_requests
    print_result(
        f"Performance test ({num_requests} requests)",
        success,
        f"Success: {success_count}/{num_requests}, Avg time: {avg_time:.2f}s"
    )


def run_all_tests():
    """Run all tests"""
    print("\n" + "█"*80)
    print("  DIET RECOMMENDER API TEST SUITE")
    print("█"*80)
    print(f"\nTesting API at: {BASE_URL}")
    print("Make sure the API server is running!")
    print("Run: python api_mobile.py")
    
    results = []
    
    # Run all test functions
    results.append(("Health Check", test_health_check()))
    results.append(("Diet Type Prediction", test_predict_diet_type()))
    results.append(("Quick Recommendation", test_quick_recommend()))
    results.append(("Full Recommendation", test_full_recommendation()))
    
    test_edge_cases()
    test_performance()
    
    # Summary
    print_section("TEST SUMMARY")
    passed = sum(1 for _, result in results if result)
    total = len(results)
    
    print(f"\nTests passed: {passed}/{total}")
    for name, result in results:
        status = "PASS" if result else "FAIL"
        print(f"  {status} - {name}")
    
    if passed == total:
        print("\nAll tests passed!")
    else:
        print(f"\n{total - passed} test(s) failed")
    
    return passed == total


if __name__ == "__main__":
    try:
        run_all_tests()
    except KeyboardInterrupt:
        print("\n\nTests interrupted by user")
    except Exception as e:
        print(f"\n\nTest suite error: {str(e)}")
        import traceback
        traceback.print_exc()
