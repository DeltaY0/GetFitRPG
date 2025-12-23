"""
Simple test script to verify the API is working
"""
import requests
import sys
import os

API_BASE = "http://localhost:5000/api"

def test_health():
    """Test health endpoint"""
    print("\n1. Testing Health Endpoint...")
    try:
        response = requests.get(f"{API_BASE}/health", timeout=5)
        if response.status_code == 200:
            data = response.json()
            print(f"   ✓ Server is online")
            print(f"   ✓ Model loaded: {data['model_loaded']}")
            print(f"   ✓ Number of classes: {data['num_classes']}")
            return True
        else:
            print(f"   ❌ Failed with status code: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"   ❌ Connection failed: {e}")
        print("\n   Make sure the API server is running!")
        print("   Run: python food_detection_api.py")
        return False

def test_classes():
    """Test classes endpoint"""
    print("\n2. Testing Classes Endpoint...")
    try:
        response = requests.get(f"{API_BASE}/classes", timeout=5)
        if response.status_code == 200:
            data = response.json()
            print(f"   ✓ Retrieved {data['total_classes']} food classes")
            print(f"   ✓ First 5 classes:")
            for cls in data['classes'][:5]:
                print(f"      - {cls['name']}")
            return True
        else:
            print(f"   ❌ Failed with status code: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"   ❌ Request failed: {e}")
        return False

def test_config():
    """Test config endpoint"""
    print("\n3. Testing Config Endpoint...")
    try:
        response = requests.get(f"{API_BASE}/config", timeout=5)
        if response.status_code == 200:
            data = response.json()
            config = data['config']
            print(f"   ✓ Confidence threshold: {config['confidence_threshold']}")
            print(f"   ✓ Top K predictions: {config['top_k_predictions']}")
            return True
        else:
            print(f"   ❌ Failed with status code: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"   ❌ Request failed: {e}")
        return False

def test_detection(image_path=None):
    """Test detection endpoint"""
    print("\n4. Testing Detection Endpoint...")
    
    if image_path and os.path.exists(image_path):
        try:
            with open(image_path, 'rb') as f:
                files = {'image': f}
                response = requests.post(f"{API_BASE}/detect", files=files, timeout=30)
                
                if response.status_code == 200:
                    data = response.json()
                    if data['success']:
                        print(f"   ✓ Detection successful!")
                        print(f"   ✓ Top prediction: {data['top_prediction']['food_name']}")
                        print(f"      Confidence: {data['top_prediction']['confidence']:.2f}%")
                        
                        if len(data['predictions']) > 1:
                            print(f"\n   ✓ All predictions:")
                            for i, pred in enumerate(data['predictions'], 1):
                                print(f"      {i}. {pred['food_name']}: {pred['confidence']:.2f}%")
                        return True
                    else:
                        print(f"   ⚠️  Detection failed: {data['message']}")
                        return False
                else:
                    print(f"   ❌ Failed with status code: {response.status_code}")
                    return False
        except requests.exceptions.RequestException as e:
            print(f"   ❌ Request failed: {e}")
            return False
    else:
        print("   ⚠️  Skipping detection test (no image provided)")
        print("   To test detection, run:")
        print("   python test_api.py path/to/food_image.jpg")
        return None

def main():
    print("=" * 80)
    print("🍕 FOOD DETECTION API - Test Suite")
    print("=" * 80)
    
    # Get image path from command line if provided
    image_path = sys.argv[1] if len(sys.argv) > 1 else None
    
    # Run tests
    results = []
    results.append(("Health Check", test_health()))
    
    if results[0][1]:  # Only continue if health check passed
        results.append(("Classes", test_classes()))
        results.append(("Config", test_config()))
        results.append(("Detection", test_detection(image_path)))
    
    # Summary
    print("\n" + "=" * 80)
    print("TEST SUMMARY")
    print("=" * 80)
    
    passed = sum(1 for _, result in results if result is True)
    failed = sum(1 for _, result in results if result is False)
    skipped = sum(1 for _, result in results if result is None)
    
    for test_name, result in results:
        if result is True:
            print(f"✓ {test_name}: PASSED")
        elif result is False:
            print(f"❌ {test_name}: FAILED")
        else:
            print(f"⚠️  {test_name}: SKIPPED")
    
    print("\n" + "=" * 80)
    print(f"Passed: {passed} | Failed: {failed} | Skipped: {skipped}")
    print("=" * 80 + "\n")
    
    if failed > 0:
        sys.exit(1)

if __name__ == "__main__":
    main()
