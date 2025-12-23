# Diet Recommender - AI-Powered Personalized Diet Planning System

A complete AI-powered diet recommendation system with a REST API designed for mobile applications (Kotlin/Android integration).

##  Project Structure

```
diet_recommender/
 api/                          # REST API endpoints
    main.py                  # FastAPI application (Mobile-ready)
    test_api.py              # API test suite
 ai/                           # AI/ML components
    train.py                 # Model training
    predict.py               # Prediction & recommendation logic
    recommend.py             # Recipe recommendation engine
    planner.py               # Meal plan generation
    planner_ilp.py          # ILP-based planner (optional)
    model_utils.py           # Model utilities
 utils/                        # Utility functions
    preprocess.py            # Data preprocessing
 gui/                          # GUI applications
    api_tester.py            # GUI for testing API
 scripts/                      # Helper scripts
    run_demo.py
    run_for_patient.py
    run_uvicorn.py
 models/                       # Trained models & encoders
    rf_diet_recommender.joblib
    label_encoder.joblib
    Gender_encoder.joblib
    Disease_Type_encoder.joblib
    Physical_Activity_Level_encoder.joblib
    feature_cols.json
 data/                         # Data files
    ingredient_synonyms.json
 tests/                        # Unit tests
    test_preprocess.py
    test_recommend_filter.py
    test_planner_variety.py
 requirements.txt              # Python dependencies
```

##  Quick Start

### Installation

1. **Create and activate a virtual environment:**
```powershell
python -m venv venv
.\venv\Scripts\Activate.ps1
```

2. **Install dependencies:**
```powershell
pip install -r requirements.txt
```

3. **Train the model (if models don't exist):**
```powershell
python ai/train.py
```

### Running the API Server

**Option 1: Direct Python**
```powershell
cd api
python main.py
```

**Option 2: Using Uvicorn**
```powershell
uvicorn api.main:app --reload --host 0.0.0.0 --port 8000
```

The API will be available at `http://localhost:8000`

### Testing the API

**1. Run the automated test suite:**
```powershell
python api/test_api.py
```

**2. Use the GUI tester:**
```powershell
python gui/api_tester.py
```

**3. Access the interactive API documentation:**
- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc

##  API Endpoints for Mobile Integration

### 1. Health Check
```
GET /health
```
Returns API status and model information.

### 2. Quick Recommendation (Simplified)
```
POST /api/v1/quick-recommend
```
**Request Body:**
```json
{
  "age": 30,
  "weight_kg": 70,
  "height_cm": 170,
  "gender": "Male",
  "activity_level": "Moderate",
  "dietary_restrictions": "None",
  "allergies": "None",
  "days": 7
}
```

### 3. Full Recommendation
```
POST /api/v1/recommend
```
**Request Body:**
```json
{
  "patient": {
    "Age": 30,
    "Weight_kg": 70,
    "Height_cm": 170,
    "Gender": "Male",
    "Disease_Type": "None",
    "Physical_Activity_Level": "Moderate",
    "Weekly_Exercise_Hours": 3.5,
    "Adherence_to_Diet_Plan": 80,
    "Dietary_Nutrient_Imbalance_Score": 10,
    "Dietary_Restrictions": "Vegetarian",
    "Allergies": "Nuts"
  },
  "days": 7,
  "meals_per_day": 3,
  "no_repeat_days": 3
}
```

**Response:**
```json
{
  "patient_id": "<mobile_user>",
  "target_calories": 2200,
  "dietary_restrictions": "Vegetarian",
  "allergies": "Nuts",
  "total_days": 7,
  "meal_plan": [
    {
      "day": 1,
      "total_calories": 2180,
      "meals": [
        {
          "recipe_name": "Grilled Chicken Salad",
          "cuisine_type": "Mediterranean",
          "calories": 450,
          "protein": 35.2,
          "carbs": 25.5,
          "fat": 15.3
        }
        // ... more meals
      ]
    }
    // ... more days
  ]
}
```

### 4. Predict Diet Type
```
POST /api/v1/predict-diet-type
```
Returns recommended diet type based on patient profile.

##  Kotlin/Android Integration Example

```kotlin
// Data classes
data class QuickRecommendRequest(
    val age: Int,
    val weight_kg: Float,
    val height_cm: Float,
    val gender: String,
    val activity_level: String,
    val dietary_restrictions: String,
    val allergies: String,
    val days: Int
)

data class MealInfo(
    val recipe_name: String,
    val cuisine_type: String,
    val calories: Float,
    val protein: Float?,
    val carbs: Float?,
    val fat: Float?
)

data class DayPlan(
    val day: Int,
    val total_calories: Float,
    val meals: List<MealInfo>
)

data class RecommendationResponse(
    val patient_id: String,
    val target_calories: Float,
    val dietary_restrictions: String,
    val allergies: String,
    val total_days: Int,
    val meal_plan: List<DayPlan>
)

// API Service (Retrofit)
interface DietAPI {
    @POST("api/v1/quick-recommend")
    suspend fun getQuickRecommendation(
        @Body request: QuickRecommendRequest
    ): RecommendationResponse
}

// Usage
val api = Retrofit.Builder()
    .baseUrl("http://YOUR_SERVER_IP:8000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(DietAPI::class.java)

val request = QuickRecommendRequest(
    age = 30,
    weight_kg = 70f,
    height_cm = 170f,
    gender = "Male",
    activity_level = "Moderate",
    dietary_restrictions = "None",
    allergies = "None",
    days = 7
)

val response = api.getQuickRecommendation(request)
```

##  Running Tests

```powershell
# Run all unit tests
pytest tests/

# Run specific test file
pytest tests/test_preprocess.py

# Run API tests
python api/test_api.py
```

##  Model Training

The system uses a Random Forest classifier trained on patient data to predict diet types:

**Features used:**
- Age, Weight, Height, BMI
- Gender, Disease Type, Physical Activity Level
- Weekly Exercise Hours
- Diet Adherence
- Nutrient Imbalance Score
- Target Calories

**To retrain the model:**
```powershell
python ai/train.py
```

##  GUI Testing Tool

A simple tkinter-based GUI is provided for manual API testing:

```powershell
python gui/api_tester.py
```

Features:
- Quick recommendation form
- Full recommendation form with all parameters
- Diet type prediction
- Live API connection testing
- Formatted result display

##  Security Notes

For production deployment:
1. Update CORS settings in `api/main.py` to restrict allowed origins
2. Add authentication/API keys
3. Use HTTPS
4. Implement rate limiting
5. Validate and sanitize all inputs

##  Notes

- The model requires training data in `../DataSets/` directory
- Ensure all model files exist in the `models/` directory
- The API runs on port 8000 by default
- For ILP-based planning, ensure the `planner_ilp.py` module is available

##  Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests
5. Submit a pull request

##  License

This project is for educational purposes.

---

**For support or questions, please refer to the API documentation at `/docs` when the server is running.**
