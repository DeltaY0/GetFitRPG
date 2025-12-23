"""
Quick diagnostic tool to help identify API connection issues
"""
import requests
import sys

def test_endpoint(url, method='GET', description=''):
    """Test a single endpoint and show detailed results"""
    print(f"\n{'='*80}")
    print(f"Testing: {description}")
    print(f"URL: {url}")
    print(f"Method: {method}")
    print(f"{'='*80}")
    
    try:
        if method == 'GET':
            response = requests.get(url, timeout=5)
        else:
            response = requests.post(url, timeout=5)
        
        print(f"✅ Status Code: {response.status_code}")
        print(f"✅ Response:")
        print(response.text[:500])  # First 500 chars
        
        if response.status_code == 404:
            print("\n⚠️  404 ERROR - Endpoint not found!")
            print("   Check the URL path carefully")
            try:
                error_data = response.json()
                if 'available_endpoints' in error_data:
                    print("\n📋 Available endpoints:")
                    for endpoint, desc in error_data['available_endpoints'].items():
                        print(f"   {endpoint}: {desc}")
            except:
                pass
        
        return response.status_code == 200
        
    except requests.exceptions.ConnectionError:
        print("❌ CONNECTION ERROR!")
        print("   The API server is not running or not accessible")
        print("\n   Solutions:")
        print("   1. Start the API server: python food_detection_api.py")
        print("   2. Check if port 5000 is already in use")
        return False
    except requests.exceptions.Timeout:
        print("❌ TIMEOUT ERROR!")
        print("   The server took too long to respond")
        return False
    except Exception as e:
        print(f"❌ ERROR: {e}")
        return False

def main():
    print("\n" + "="*80)
    print("🔍 API DIAGNOSTIC TOOL")
    print("="*80)
    
    # Get base URL from command line or use default
    if len(sys.argv) > 1:
        base = sys.argv[1]
    else:
        base = "http://localhost:5000"
    
    print(f"\nBase URL: {base}")
    print("Testing common endpoints...\n")
    
    # Test various URLs
    tests = [
        (f"{base}/", "GET", "Root endpoint"),
        (f"{base}/api", "GET", "API root"),
        (f"{base}/api/health", "GET", "Health check (CORRECT)"),
        (f"{base}/health", "GET", "Health without /api/ (WRONG - will fail)"),
        (f"{base}/api/classes", "GET", "Get classes"),
        (f"{base}/api/config", "GET", "Get config"),
    ]
    
    results = []
    for url, method, desc in tests:
        result = test_endpoint(url, method, desc)
        results.append((desc, result))
    
    # Summary
    print("\n" + "="*80)
    print("📊 SUMMARY")
    print("="*80)
    
    for desc, result in results:
        status = "✅ PASS" if result else "❌ FAIL"
        print(f"{status}: {desc}")
    
    print("\n" + "="*80)
    print("💡 TIPS")
    print("="*80)
    print("1. All endpoints must start with /api/ prefix")
    print("2. Correct: http://localhost:5000/api/health")
    print("3. Wrong:   http://localhost:5000/health")
    print("4. In Kotlin, Base URL must be: http://YOUR_IP:5000/api/")
    print("="*80 + "\n")

if __name__ == "__main__":
    main()
