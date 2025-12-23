import requests
import sys

print("\n" + "="*80)
print("FINAL API TEST")
print("="*80)

# Test 1: Health check
print("\n[1] Testing Health...")
try:
    r = requests.get('http://localhost:5000/api/health', timeout=5)
    print(f"✅ Health: {r.status_code}")
    print(f"   {r.json()}")
except Exception as e:
    print(f"❌ Health failed: {e}")
    print("   Server not running! Run: python food_detection_api.py")
    sys.exit(1)

# Test 2: Detection
print("\n[2] Testing Detection...")
try:
    with open('download.jpg', 'rb') as f:
        r = requests.post('http://localhost:5000/api/detect', 
                         files={'image': f}, 
                         timeout=30)
    print(f"✅ Detection: {r.status_code}")
    data = r.json()
    if data['success']:
        print(f"   Food: {data['top_prediction']['food_name']}")
        print(f"   Confidence: {data['top_prediction']['confidence']}%")
    else:
        print(f"   Message: {data['message']}")
except Exception as e:
    print(f"❌ Detection failed: {e}")

print("\n" + "="*80)
print("✅ ALL TESTS COMPLETE!")
print("="*80)
print("\n📱 For Kotlin App:")
print(f"   Base URL: http://192.168.1.50:5000/api/")
print(f"   Make sure to use /api/ at the end!")
print("="*80 + "\n")
