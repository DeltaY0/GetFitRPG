# Food Detection API Documentation

## Overview
REST API for food detection using YOLOv8 model. Designed for integration with mobile applications (Kotlin/Android).

## Base URL
```
http://<YOUR_PC_IP>:5000/api
```

**Note:** Replace `<YOUR_PC_IP>` with your computer's local IP address. Ensure your mobile device is on the same WiFi network.

---

## Endpoints

### 1. Health Check
Check if the API server is running and model is loaded.

**Endpoint:** `GET /api/health`

**Response:**
```json
{
  "status": "online",
  "model_loaded": true,
  "num_classes": 101,
  "model_path": "D:\\college work\\term5\\Mobile\\Food detection\\best.pt"
}
```

---

### 2. Detect Food (Multipart Upload)
Upload an image file for food detection.

**Endpoint:** `POST /api/detect`

**Content-Type:** `multipart/form-data`

**Parameters:**
- `image` (file): Image file (JPEG, PNG, etc.)

**Response:**
```json
{
  "success": true,
  "message": "Food detected successfully",
  "predictions": [
    {
      "food_name": "pizza",
      "confidence": 95.5,
      "class_id": 67
    },
    {
      "food_name": "hamburger",
      "confidence": 2.3,
      "class_id": 45
    },
    {
      "food_name": "hot_dog",
      "confidence": 1.1,
      "class_id": 46
    }
  ],
  "top_prediction": {
    "food_name": "pizza",
    "confidence": 95.5,
    "class_id": 67
  }
}
```

---

### 3. Detect Food (Base64)
Send base64 encoded image for food detection.

**Endpoint:** `POST /api/detect/base64`

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "image": "data:image/jpeg;base64,/9j/4AAQSkZJRg..."
}
```
or
```json
{
  "image": "/9j/4AAQSkZJRg..."
}
```

**Response:** Same as `/api/detect`

---

### 4. Get All Food Classes
Get list of all food classes the model can detect.

**Endpoint:** `GET /api/classes`

**Response:**
```json
{
  "success": true,
  "total_classes": 101,
  "classes": [
    {"id": 0, "name": "apple_pie"},
    {"id": 1, "name": "baby_back_ribs"},
    {"id": 2, "name": "baklava"},
    ...
  ]
}
```

---

### 5. Get Configuration
Get current API configuration settings.

**Endpoint:** `GET /api/config`

**Response:**
```json
{
  "success": true,
  "config": {
    "confidence_threshold": 0.3,
    "top_k_predictions": 3,
    "num_classes": 101
  }
}
```

---

### 6. Update Configuration
Update API configuration settings.

**Endpoint:** `POST /api/config`

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "confidence_threshold": 0.5,
  "top_k_predictions": 5
}
```

**Response:**
```json
{
  "success": true,
  "message": "Configuration updated",
  "config": {
    "confidence_threshold": 0.5,
    "top_k_predictions": 5
  }
}
```

---

## Kotlin/Android Integration Example

### 1. Add Dependencies (build.gradle)
```kotlin
dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}
```

### 2. API Service Interface
```kotlin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

data class FoodPrediction(
    val food_name: String,
    val confidence: Double,
    val class_id: Int
)

data class DetectionResponse(
    val success: Boolean,
    val message: String,
    val predictions: List<FoodPrediction>,
    val top_prediction: FoodPrediction?
)

data class HealthResponse(
    val status: String,
    val model_loaded: Boolean,
    val num_classes: Int
)

data class Base64Request(
    val image: String
)

interface FoodDetectionAPI {
    
    @GET("health")
    fun healthCheck(): Call<HealthResponse>
    
    @Multipart
    @POST("detect")
    fun detectFood(
        @Part image: MultipartBody.Part
    ): Call<DetectionResponse>
    
    @POST("detect/base64")
    fun detectFoodBase64(
        @Body request: Base64Request
    ): Call<DetectionResponse>
}
```

### 3. Retrofit Setup
```kotlin
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Replace with your PC's IP address
    private const val BASE_URL = "http://192.168.1.100:5000/api/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    val api: FoodDetectionAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodDetectionAPI::class.java)
    }
}
```

### 4. Upload Image from Camera/Gallery
```kotlin
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class FoodDetectionHelper {
    
    fun detectFoodFromFile(imageFile: File, onResult: (DetectionResponse?) -> Unit) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        
        RetrofitClient.api.detectFood(body).enqueue(object : Callback<DetectionResponse> {
            override fun onResponse(
                call: Call<DetectionResponse>,
                response: Response<DetectionResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    Log.e("API", "Error: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }
            
            override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
                Log.e("API", "Failed: ${t.message}")
                onResult(null)
            }
        })
    }
    
    fun detectFoodFromUri(context: Context, uri: Uri, onResult: (DetectionResponse?) -> Unit) {
        // Convert URI to File
        val file = File(context.cacheDir, "temp_image.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        detectFoodFromFile(file, onResult)
    }
}
```

### 5. Usage in Activity/Fragment
```kotlin
class MainActivity : AppCompatActivity() {
    
    private val foodDetector = FoodDetectionHelper()
    
    private fun detectFood(imageFile: File) {
        // Show loading
        showLoading(true)
        
        foodDetector.detectFoodFromFile(imageFile) { response ->
            runOnUiThread {
                showLoading(false)
                
                if (response?.success == true) {
                    val topPrediction = response.top_prediction
                    if (topPrediction != null) {
                        showResult(
                            foodName = topPrediction.food_name,
                            confidence = topPrediction.confidence
                        )
                        
                        // Display all predictions
                        response.predictions.forEach { prediction ->
                            Log.d("Detection", 
                                "${prediction.food_name}: ${prediction.confidence}%")
                        }
                    }
                } else {
                    showError(response?.message ?: "Detection failed")
                }
            }
        }
    }
    
    private fun showResult(foodName: String, confidence: Double) {
        // Update UI with results
        textViewResult.text = "Detected: ${foodName.replace("_", " ")}"
        textViewConfidence.text = "Confidence: ${"%.1f".format(confidence)}%"
    }
}
```

### 6. Using Base64 (Alternative)
```kotlin
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun detectFoodFromBitmap(bitmap: Bitmap, onResult: (DetectionResponse?) -> Unit) {
    // Convert bitmap to base64
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    val imageBytes = outputStream.toByteArray()
    val base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    
    val request = Base64Request("data:image/jpeg;base64,$base64String")
    
    RetrofitClient.api.detectFoodBase64(request).enqueue(object : Callback<DetectionResponse> {
        override fun onResponse(
            call: Call<DetectionResponse>,
            response: Response<DetectionResponse>
        ) {
            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }
        
        override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
            Log.e("API", "Failed: ${t.message}")
            onResult(null)
        }
    })
}
```

---

## Testing the API

### Using cURL
```bash
# Health check
curl http://localhost:5000/api/health

# Upload image
curl -X POST http://localhost:5000/api/detect \
  -F "image=@/path/to/food_image.jpg"

# Get all classes
curl http://localhost:5000/api/classes
```

### Using Python
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

---

## Error Responses

All endpoints return error responses in this format:
```json
{
  "success": false,
  "message": "Error description here"
}
```

Common HTTP status codes:
- `200` - Success
- `400` - Bad Request (missing parameters, invalid data)
- `404` - Endpoint not found
- `500` - Internal Server Error

---

## Network Setup

### Find Your PC's IP Address

**Windows:**
```cmd
ipconfig
```
Look for "IPv4 Address" under your WiFi/Ethernet adapter (usually starts with 192.168.x.x)

**Linux/Mac:**
```bash
ifconfig
# or
ip addr show
```

### Allow Firewall Access
Make sure Windows Firewall allows Python to accept incoming connections on port 5000.

---

## Running the Server

```bash
# Install dependencies
pip install flask flask-cors ultralytics opencv-python pillow

# Run the server
python food_detection_api.py
```

The server will start on `http://0.0.0.0:5000`

Access from your mobile device using: `http://<YOUR_PC_IP>:5000`
