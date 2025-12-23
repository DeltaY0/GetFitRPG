# Food Detection - Quick Start Guide

## 🎯 What's Been Created

1. **Enhanced Webcam Test** (`Test_model.py`) - Fixed webcam compatibility for Windows
2. **REST API Server** (`food_detection_api.py`) - Flask API for mobile app integration
3. **Complete Documentation** (`API_DOCUMENTATION.md`) - API reference and Kotlin examples
4. **Dependencies** (`requirements.txt`) - All required Python packages

---

## 🚀 Getting Started

### Step 1: Install Dependencies

```powershell
pip install -r requirements.txt
```

### Step 2: Test the Webcam (Optional)

```powershell
python Test_model.py
```

Select option 3 to test webcam functionality. Press 'q' to quit.

### Step 3: Start the API Server

```powershell
python food_detection_api.py
```

You should see:
```
🍕 FOOD DETECTION API SERVER
✓ Model loaded: D:\college work\term5\Mobile\Food detection\best.pt
✓ Number of food classes: 101
✓ Confidence threshold: 0.3
✓ Top predictions returned: 3

🌐 Server starting on http://0.0.0.0:5000
```

### Step 4: Find Your PC's IP Address

Open a new PowerShell window and run:
```powershell
ipconfig
```

Look for your WiFi adapter's IPv4 Address (e.g., `192.168.1.100`)

### Step 5: Test the API

Open a web browser and navigate to:
```
http://localhost:5000/api/health
```

You should see:
```json
{
  "status": "online",
  "model_loaded": true,
  "num_classes": 101
}
```

---

## 📱 Mobile App Integration (Kotlin)

### Quick Test from Browser

Before integrating with your Kotlin app, test from your mobile browser:

1. Make sure your phone is on the same WiFi network
2. Open browser on your phone
3. Navigate to: `http://<YOUR_PC_IP>:5000/api/health`
4. If you see the JSON response, the API is accessible!

### API Endpoints for Your App

**Main endpoint for food detection:**
```
POST http://<YOUR_PC_IP>:5000/api/detect
```

Upload image as `multipart/form-data` with field name `image`

**Response format:**
```json
{
  "success": true,
  "message": "Food detected successfully",
  "predictions": [
    {
      "food_name": "pizza",
      "confidence": 95.5,
      "class_id": 67
    }
  ],
  "top_prediction": {
    "food_name": "pizza",
    "confidence": 95.5,
    "class_id": 67
  }
}
```

### Kotlin Integration

See `API_DOCUMENTATION.md` for complete Kotlin/Android code examples including:
- Retrofit setup
- API service interface
- Image upload from camera/gallery
- Error handling
- UI updates

---

## 🔧 Configuration

### Adjust Confidence Threshold

Edit `food_detection_api.py`:
```python
CONFIDENCE_THRESHOLD = 0.3  # Lower = more predictions, Higher = fewer but more confident
```

Or update via API:
```bash
curl -X POST http://localhost:5000/api/config \
  -H "Content-Type: application/json" \
  -d '{"confidence_threshold": 0.5}'
```

### Change Number of Predictions

Edit `food_detection_api.py`:
```python
TOP_K_PREDICTIONS = 3  # Number of top predictions to return
```

---

## 🐛 Troubleshooting

### Webcam Not Working
- Make sure no other application is using the webcam
- Try closing Skype, Teams, or other video apps
- The code now tries DirectShow backend automatically

### Mobile Can't Connect to API
- Verify both devices are on the same WiFi network
- Check Windows Firewall settings (allow Python on port 5000)
- Verify the IP address is correct using `ipconfig`
- Try accessing `http://<PC_IP>:5000/api/health` from mobile browser first

### Model Not Found Error
- Verify `best.pt` exists in the project directory
- Check the path in `food_detection_api.py` matches your model location

### Low Confidence Predictions
- Lower the `CONFIDENCE_THRESHOLD` in the API
- Ensure good lighting when taking photos
- Make sure food is clearly visible and centered

---

## 📝 API Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Check server status |
| POST | `/api/detect` | Detect food (multipart upload) |
| POST | `/api/detect/base64` | Detect food (base64) |
| GET | `/api/classes` | Get all food classes |
| GET | `/api/config` | Get configuration |
| POST | `/api/config` | Update configuration |

---

## 🔒 Security Notes

⚠️ **This API is for development/local use only!**

- No authentication implemented
- Runs on local network only
- Don't expose to the internet without security measures
- For production, add:
  - API key authentication
  - Rate limiting
  - HTTPS/SSL
  - Input validation

---

## 💡 Tips

1. **Better Detection:**
   - Use good lighting
   - Keep food centered in frame
   - Avoid cluttered backgrounds
   - Take photos from above (like the training data)

2. **Performance:**
   - First prediction is slower (model loading)
   - Subsequent predictions are faster
   - Resize large images before sending

3. **Testing:**
   - Use the webcam test mode to see real-time predictions
   - Test with sample images first
   - Check the `test_results/` folder for annotated images

---

## 📞 Need Help?

Check the full documentation in `API_DOCUMENTATION.md` for:
- Detailed Kotlin code examples
- Complete API reference
- Error handling
- Network setup guide
