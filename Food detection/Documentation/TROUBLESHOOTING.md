# 🔧 API Troubleshooting Guide

## ❌ Error: "Endpoint not found"

This error means you're trying to access a URL that doesn't exist in the API.

---

## ✅ Correct Endpoints

All endpoints should start with `/api/` prefix:

### **Working URLs:**
```
✅ http://localhost:5000/api/health
✅ http://localhost:5000/api/detect
✅ http://localhost:5000/api/detect/base64
✅ http://localhost:5000/api/classes
✅ http://localhost:5000/api/config
```

### **Won't Work (Missing /api/):**
```
❌ http://localhost:5000/health
❌ http://localhost:5000/detect
❌ http://localhost:5000/classes
```

---

## 🧪 Quick Test Commands

### Test from PowerShell/CMD:
```powershell
# 1. Health check
curl http://localhost:5000/api/health

# 2. Get all classes
curl http://localhost:5000/api/classes

# 3. Upload image (replace path)
curl -X POST http://localhost:5000/api/detect -F "image=@C:\path\to\food.jpg"
```

### Test from Python:
```python
import requests

# Health check
response = requests.get('http://localhost:5000/api/health')
print(response.json())

# Upload image
with open('food_image.jpg', 'rb') as f:
    response = requests.post(
        'http://localhost:5000/api/detect',
        files={'image': f}
    )
    print(response.json())
```

### Test from Browser:
```
http://localhost:5000/api/health
```

---

## 📱 From Mobile/Kotlin

### ✅ Correct Retrofit Base URL:
```kotlin
private const val BASE_URL = "http://192.168.1.100:5000/api/"
//                                                      ^^^^^ Must end with /api/
```

### ❌ Wrong Base URL:
```kotlin
// Missing /api/
private const val BASE_URL = "http://192.168.1.100:5000/"

// Will try to access: http://192.168.1.100:5000/health
// Instead of:         http://192.168.1.100:5000/api/health
```

---

## 🔍 Common Issues

### Issue 1: Wrong Base URL in Kotlin
**Problem:** Base URL doesn't end with `/api/`

**Solution:**
```kotlin
// In FoodDetectionAPI.kt, line ~85
private const val BASE_URL = "http://YOUR_IP:5000/api/"
//                           Must include /api/ ^^^^^^^^
```

### Issue 2: Using Wrong HTTP Method
**Problem:** Using GET instead of POST for detection

**Solution:**
```kotlin
@POST("detect")  // ✅ Correct
// not @GET("detect")  ❌ Wrong
```

### Issue 3: Missing Image Parameter
**Problem:** Not sending 'image' field in multipart request

**Solution:**
```kotlin
val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//                                            ^^^^^ Must be "image"
```

---

## 🌐 Network Issues

### Can't Connect from Mobile?

**1. Check IP Address:**
```powershell
ipconfig
# Look for IPv4 Address
```

**2. Verify Same Network:**
- PC and mobile must be on same WiFi

**3. Test from Mobile Browser First:**
```
http://YOUR_PC_IP:5000/api/health
```

**4. Check Firewall:**
- Windows Firewall might be blocking port 5000
- Allow Python through firewall

---

## 🐛 Debug Steps

### Step 1: Check if API is Running
```powershell
curl http://localhost:5000/api/health
```

Expected response:
```json
{
  "status": "online",
  "model_loaded": true,
  "num_classes": 101
}
```

### Step 2: Check Endpoint Exists
```powershell
# This will show all available endpoints
curl http://localhost:5000/api
```

### Step 3: Check Full URL in Kotlin
Add logging to see the full URL being called:
```kotlin
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY  // Shows full request/response
}
```

---

## 📋 Complete Endpoint List

| HTTP Method | Endpoint | Description |
|-------------|----------|-------------|
| GET | `/` | API info (root) |
| GET | `/api` | API info |
| GET | `/api/health` | Server health check |
| POST | `/api/detect` | Detect food (multipart) |
| POST | `/api/detect/base64` | Detect food (base64) |
| GET | `/api/classes` | Get all food classes |
| GET | `/api/config` | Get configuration |
| POST | `/api/config` | Update configuration |

---

## ✅ Verify Your Setup

Run the test script:
```powershell
python test_api.py
```

All tests should pass:
```
✓ Health Check: PASSED
✓ Classes: PASSED
✓ Config: PASSED
```

---

## 💡 Quick Fix Checklist

- [ ] API server is running (`python food_detection_api.py`)
- [ ] Using correct base URL with `/api/` prefix
- [ ] Endpoint path is spelled correctly
- [ ] Using correct HTTP method (GET vs POST)
- [ ] For `/api/detect`: using multipart/form-data with 'image' field
- [ ] PC and mobile on same WiFi network
- [ ] Firewall allows connections on port 5000

---

## 📞 Still Having Issues?

The improved 404 error now shows:
- The URL you tried to access
- List of all available endpoints

Check the error response for details on what went wrong.
