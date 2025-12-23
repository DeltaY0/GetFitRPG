# 🍕 Food Detection System - Complete Setup

## ✅ Everything is Ready!

You now have a complete food detection system with:
- ✅ REST API for mobile apps
- ✅ GUI desktop application
- ✅ Command-line tools
- ✅ Fixed webcam support

---

## 🚀 Quick Start (Easiest Way)

### **Just double-click `START.bat`**

This will:
1. Start the API server
2. Wait for it to load
3. Launch the GUI app automatically

Then:
1. Click "📁 Select Image"
2. Choose a food picture
3. Click "🔍 Detect Food"
4. See the results!

---

## 🔧 Manual Start (If You Prefer)

### Step 1: Start API Server
Open a PowerShell/Terminal window:
```powershell
python food_detection_api.py
```

Wait until you see:
```
✓ Model loaded successfully! (101 classes)
* Running on http://127.0.0.1:5000
```

### Step 2: Start GUI Application
Open a NEW PowerShell/Terminal window:
```powershell
python food_detection_gui.py
```

---

## 📱 For Mobile App (Kotlin)

### Your API is running on:
```
http://192.168.1.50:5000/api/
```

### In your Kotlin code (`FoodDetectionAPI.kt`):
```kotlin
private const val BASE_URL = "http://192.168.1.50:5000/api/"
```

### Make sure:
- ✅ API server is running (`python food_detection_api.py`)
- ✅ Both PC and phone on same WiFi
- ✅ Windows Firewall allows connections on port 5000

---

## 📋 Available Tools

| File | Description | How to Use |
|------|-------------|------------|
| `START.bat` | **Start everything** | Double-click |
| `food_detection_api.py` | API server | `python food_detection_api.py` |
| `food_detection_gui.py` | Desktop GUI app | `python food_detection_gui.py` |
| `detect_image.py` | Command-line tool | `python detect_image.py pizza.jpg` |
| `Test_model.py` | Webcam test | `python Test_model.py` → Option 3 |
| `final_test.py` | API test suite | `python final_test.py` |

---

## 🐛 Troubleshooting

### "Cannot connect to API server"
**Problem:** API server is not running

**Solution:**
1. Open a terminal
2. Run: `python food_detection_api.py`
3. Wait for "Model loaded successfully"
4. Then use the GUI

**OR** just run `START.bat` - it does everything!

---

### GUI says "API Server not running"
**Problem:** API server stopped or crashed

**Solution:**
1. Close the API server window (if open)
2. Run `START.bat` again
3. OR manually start API: `python food_detection_api.py`

---

### Mobile app can't connect
**Problem:** Wrong IP address or server not accessible

**Solution:**
1. Check your PC's IP address:
   ```powershell
   ipconfig
   ```
   Look for IPv4 Address (e.g., 192.168.1.50)

2. Update Kotlin code:
   ```kotlin
   private const val BASE_URL = "http://YOUR_IP:5000/api/"
   ```

3. Verify both devices on same WiFi

4. Test from mobile browser first:
   ```
   http://YOUR_IP:5000/api/health
   ```

---

### Low confidence / Wrong predictions
**Problem:** Model isn't confident

**Solutions:**
- Use clear, well-lit photos
- Center the food in the image
- Take photo from above (like training data)
- Lower confidence threshold in `food_detection_api.py`:
  ```python
  CONFIDENCE_THRESHOLD = 0.2  # Lower = more predictions
  ```

---

## 📖 API Endpoints Reference

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/health` | GET | Check if server is running |
| `/api/detect` | POST | Detect food in image |
| `/api/detect/base64` | POST | Detect food (base64) |
| `/api/classes` | GET | Get all 101 food classes |
| `/api/config` | GET | Get current settings |
| `/api/config` | POST | Update settings |

---

## 💡 Usage Examples

### GUI App:
1. Run `START.bat`
2. Select image
3. Click Detect
4. See results with confidence scores!

### Command Line:
```powershell
python detect_image.py download.jpg
```

### From Kotlin App:
```kotlin
val detector = FoodDetectionHelper()
detector.detectFoodFromUri(context, imageUri) { response ->
    if (response?.success == true) {
        val food = response.top_prediction?.food_name
        val confidence = response.top_prediction?.confidence
        // Show results in your app
    }
}
```

---

## 🎯 What You Have

### Desktop App Features:
- ✅ Beautiful GUI interface
- ✅ Image preview
- ✅ Real-time detection
- ✅ Top 3 predictions with confidence
- ✅ Progress bars for each prediction
- ✅ Color-coded results
- ✅ Auto-detects API connection

### Mobile App Ready:
- ✅ REST API running on your network
- ✅ Complete Kotlin code provided
- ✅ Multipart file upload support
- ✅ Base64 image support
- ✅ JSON responses
- ✅ Error handling

### Model Features:
- ✅ 101 different food types
- ✅ YOLOv8 trained model
- ✅ Fast inference
- ✅ High accuracy

---

## 📞 Quick Reference

**Start Everything:**
```
Double-click START.bat
```

**API Server Only:**
```powershell
python food_detection_api.py
```

**GUI Only:**
```powershell
python food_detection_gui.py
```

**Test Detection:**
```powershell
python detect_image.py your_food_image.jpg
```

**Your Network IP:**
```powershell
ipconfig
```

---

## ✨ You're All Set!

Everything is working and ready to use:
1. ✅ Webcam fixed and tested
2. ✅ API server created and tested
3. ✅ Desktop GUI app created
4. ✅ Mobile integration ready (Kotlin code provided)
5. ✅ All documentation complete

**Just run `START.bat` and start detecting food!** 🚀
