"""
Simple command-line tool to detect food in images
Usage: python detect_image.py path/to/image.jpg
"""
import requests
import sys
import os

API_URL = "http://localhost:5000/api/detect"

def detect_food(image_path):
    """Send image to API and display results"""
    
    if not os.path.exists(image_path):
        print(f"❌ Error: Image not found: {image_path}")
        return
    
    print("\n" + "="*80)
    print("🍕 FOOD DETECTION")
    print("="*80)
    print(f"\n📸 Image: {os.path.basename(image_path)}")
    print(f"📡 Sending to API...")
    
    try:
        with open(image_path, 'rb') as f:
            files = {'image': f}
            response = requests.post(API_URL, files=files, timeout=30)
        
        if response.status_code == 200:
            data = response.json()
            
            if data['success']:
                print("\n" + "="*80)
                print("✅ DETECTION SUCCESSFUL!")
                print("="*80)
                
                # Top prediction
                top = data['top_prediction']
                print(f"\n🥇 TOP RESULT:")
                print(f"   Food: {top['food_name'].replace('_', ' ').upper()}")
                print(f"   Confidence: {top['confidence']:.2f}%")
                
                # All predictions
                if len(data['predictions']) > 1:
                    print(f"\n📊 ALL PREDICTIONS:")
                    for i, pred in enumerate(data['predictions'], 1):
                        food = pred['food_name'].replace('_', ' ').title()
                        conf = pred['confidence']
                        
                        # Progress bar
                        bar_length = int(conf / 2)
                        bar = '█' * bar_length + '░' * (50 - bar_length)
                        
                        print(f"\n   {i}. {food}")
                        print(f"      {bar} {conf:.2f}%")
                
                print("\n" + "="*80 + "\n")
            else:
                print(f"\n❌ {data['message']}")
        else:
            print(f"\n❌ API Error {response.status_code}")
            print(f"   {response.text}")
    
    except requests.exceptions.ConnectionError:
        print("\n❌ ERROR: Cannot connect to API server!")
        print("   Make sure the server is running:")
        print("   python food_detection_api.py")
    except Exception as e:
        print(f"\n❌ ERROR: {e}")

def main():
    if len(sys.argv) < 2:
        print("\n" + "="*80)
        print("🍕 FOOD DETECTION - Command Line Tool")
        print("="*80)
        print("\nUsage:")
        print("   python detect_image.py <image_path>")
        print("\nExample:")
        print("   python detect_image.py pizza.jpg")
        print("   python detect_image.py \"C:\\path\\to\\food.jpg\"")
        print("\n" + "="*80 + "\n")
        sys.exit(1)
    
    image_path = sys.argv[1]
    detect_food(image_path)

if __name__ == "__main__":
    main()
