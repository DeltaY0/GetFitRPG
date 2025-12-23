"""
Test the detection endpoint with an actual image
"""
import requests
import sys
import os
from io import BytesIO
from PIL import Image
import numpy as np

API_URL = "http://localhost:5000/api/detect"

def create_test_image():
    """Create a simple test image if no image is provided"""
    # Create a simple 640x640 RGB image
    img = Image.new('RGB', (640, 640), color=(255, 100, 100))
    
    # Save to bytes
    img_bytes = BytesIO()
    img.save(img_bytes, format='JPEG')
    img_bytes.seek(0)
    
    return img_bytes

def test_detection(image_path=None):
    """Test the detection endpoint"""
    print("\n" + "="*80)
    print("🍕 TESTING FOOD DETECTION ENDPOINT")
    print("="*80)
    
    if image_path and os.path.exists(image_path):
        print(f"\n📸 Using image: {image_path}")
        with open(image_path, 'rb') as f:
            files = {'image': f}
            
            print(f"📡 Sending POST request to: {API_URL}")
            print("   Method: POST")
            print("   Content-Type: multipart/form-data")
            print("   Field: 'image'\n")
            
            try:
                response = requests.post(API_URL, files=files, timeout=30)
                
                print(f"✅ Status Code: {response.status_code}\n")
                
                if response.status_code == 200:
                    data = response.json()
                    
                    if data['success']:
                        print("✅ DETECTION SUCCESSFUL!")
                        print(f"\n🍽️  Top Prediction: {data['top_prediction']['food_name']}")
                        print(f"   Confidence: {data['top_prediction']['confidence']:.2f}%")
                        
                        if len(data['predictions']) > 1:
                            print(f"\n📊 All Predictions:")
                            for i, pred in enumerate(data['predictions'], 1):
                                print(f"   {i}. {pred['food_name']}: {pred['confidence']:.2f}%")
                    else:
                        print(f"⚠️  Detection failed: {data['message']}")
                else:
                    print(f"❌ Error {response.status_code}")
                    print(response.text)
                    
            except requests.exceptions.ConnectionError:
                print("❌ Cannot connect to API server!")
                print("   Make sure the server is running: python food_detection_api.py")
            except Exception as e:
                print(f"❌ Error: {e}")
    else:
        print("\n⚠️  No image file provided or file doesn't exist")
        print("\n📝 Usage:")
        print("   python test_detection.py path/to/food_image.jpg")
        print("\n💡 Example:")
        print("   python test_detection.py test_images/pizza.jpg")
        
        # Test with a dummy image
        print("\n🧪 Testing with a dummy image instead...\n")
        
        img_bytes = create_test_image()
        files = {'image': ('test.jpg', img_bytes, 'image/jpeg')}
        
        try:
            response = requests.post(API_URL, files=files, timeout=30)
            print(f"✅ Status Code: {response.status_code}")
            print(f"✅ Response: {response.json()}\n")
        except Exception as e:
            print(f"❌ Error: {e}")
    
    print("="*80 + "\n")

def show_curl_example():
    """Show how to test with curl"""
    print("\n" + "="*80)
    print("💡 TESTING WITH CURL (PowerShell)")
    print("="*80)
    print("\nTo test from command line:")
    print("\ncurl -X POST http://localhost:5000/api/detect `")
    print('  -F "image=@C:\\path\\to\\food_image.jpg"')
    print("\nReplace the path with your actual image file path")
    print("="*80 + "\n")

def show_postman_example():
    """Show how to test with Postman"""
    print("\n" + "="*80)
    print("💡 TESTING WITH POSTMAN")
    print("="*80)
    print("\n1. Method: POST")
    print("2. URL: http://localhost:5000/api/detect")
    print("3. Go to 'Body' tab")
    print("4. Select 'form-data'")
    print("5. Add key: 'image' (type: File)")
    print("6. Choose your food image file")
    print("7. Click 'Send'")
    print("="*80 + "\n")

if __name__ == "__main__":
    image_path = sys.argv[1] if len(sys.argv) > 1 else None
    
    test_detection(image_path)
    
    print("\n" + "="*80)
    print("ℹ️  IMPORTANT NOTES")
    print("="*80)
    print("\n✅ DO:")
    print("   • Use POST request (not GET)")
    print("   • Send multipart/form-data")
    print("   • Field name must be 'image'")
    print("   • Include actual image file")
    print("\n❌ DON'T:")
    print("   • Don't use GET request on /api/detect")
    print("   • Don't access /api/detect from browser (it's POST only)")
    print("   • Don't forget the image file")
    print("="*80 + "\n")
    
    show_curl_example()
    show_postman_example()
