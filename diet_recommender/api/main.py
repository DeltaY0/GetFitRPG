"""
FastAPI application for Diet Recommender - Mobile App Integration
Provides endpoints for Kotlin mobile app to get diet recommendations
"""
from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field, validator
from typing import Optional, List, Dict, Any
import pandas as pd
import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))

from ai.predict import predict_and_recommend, load_artifacts, patient_row_to_features
from utils.preprocess import compute_daily_needs, load_patients, load_recipes, clean_recipes
from ai.recommend import recommend_top_n, filter_by_allergies_and_restrictions
from ai.planner import make_30_day_plan
import traceback

app = FastAPI(
    title="Diet Recommender API",
    description="AI-powered personalized diet recommendation system for mobile applications",
    version="1.0.0"
)

# CORS configuration for mobile app
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify your mobile app's domain
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Request Models
class PatientInput(BaseModel):
    """Patient information for diet recommendation"""
    Age: int = Field(..., ge=1, le=120, description="Age in years")
    Weight_kg: float = Field(..., gt=0, le=500, description="Weight in kilograms")
    Height_cm: float = Field(..., gt=0, le=300, description="Height in centimeters")
    Gender: str = Field(..., description="Gender: Male, Female, or Other")
    Disease_Type: str = Field(default="None", description="Primary disease type if any")
    Physical_Activity_Level: str = Field(
        default="Moderate", 
        description="Activity level: Sedentary, Moderate, Active"
    )
    Weekly_Exercise_Hours: float = Field(default=0, ge=0, le=168, description="Exercise hours per week")
    Adherence_to_Diet_Plan: float = Field(default=75, ge=0, le=100, description="Diet adherence percentage")
    Dietary_Nutrient_Imbalance_Score: float = Field(default=0, ge=0, le=100, description="Nutrient imbalance score")
    Dietary_Restrictions: str = Field(default="None", description="Dietary restrictions (e.g., Vegetarian, Vegan)")
    Allergies: str = Field(default="None", description="Food allergies (comma-separated)")
    Daily_Caloric_Intake: Optional[float] = Field(None, ge=0, description="Target daily calories (optional)")
    
    @validator('Gender')
    def validate_gender(cls, v):
        valid_genders = ['Male', 'Female', 'Other', 'M', 'F']
        if v not in valid_genders:
            return 'Male'  # default
        return v
    
    @validator('Physical_Activity_Level')
    def validate_activity(cls, v):
        valid_levels = ['Sedentary', 'Moderate', 'Active']
        if v not in valid_levels:
            return 'Moderate'  # default
        return v


class RecommendationRequest(BaseModel):
    """Request for personalized diet recommendation"""
    patient: PatientInput
    days: int = Field(default=7, ge=1, le=30, description="Number of days for meal plan")
    meals_per_day: int = Field(default=3, ge=1, le=6, description="Number of meals per day")
    no_repeat_days: int = Field(default=3, ge=0, le=14, description="Days before repeating same recipe")


class QuickRecommendRequest(BaseModel):
    """Quick recommendation using essential parameters only"""
    age: int = Field(..., ge=1, le=120)
    weight_kg: float = Field(..., gt=0, le=500)
    height_cm: float = Field(..., gt=0, le=300)
    gender: str = Field(default="Male")
    activity_level: str = Field(default="Moderate")
    dietary_restrictions: str = Field(default="None")
    allergies: str = Field(default="None")
    days: int = Field(default=7, ge=1, le=30)


# Response Models
class MealInfo(BaseModel):
    recipe_name: str
    cuisine_type: str
    calories: float
    protein: Optional[float] = None
    carbs: Optional[float] = None
    fat: Optional[float] = None


class DayPlan(BaseModel):
    day: int
    total_calories: float
    meals: List[MealInfo]


class RecommendationResponse(BaseModel):
    patient_id: str
    target_calories: float
    dietary_restrictions: str
    allergies: str
    total_days: int
    meal_plan: List[DayPlan]


# Health Check
@app.get("/", tags=["Health"])
async def root():
    """API health check"""
    return {
        "status": "healthy",
        "service": "Diet Recommender API",
        "version": "1.0.0",
        "endpoints": {
            "recommend": "/api/v1/recommend",
            "quick_recommend": "/api/v1/quick-recommend",
            "predict_diet_type": "/api/v1/predict-diet-type"
        }
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """Detailed health check with model status"""
    try:
        clf, le, feature_cols, encoders = load_artifacts()
        return {
            "status": "healthy",
            "model_loaded": True,
            "feature_count": len(feature_cols),
            "diet_types": list(le.classes_)
        }
    except Exception as e:
        return {
            "status": "unhealthy",
            "model_loaded": False,
            "error": str(e)
        }


@app.post("/api/v1/recommend", response_model=RecommendationResponse, tags=["Recommendations"])
async def get_recommendation(request: RecommendationRequest):
    """
    Get personalized diet recommendation with full meal plan
    
    This endpoint takes detailed patient information and returns a complete
    meal plan for the specified number of days.
    """
    try:
        # Convert patient input to dataframe
        patient_dict = request.patient.dict()
        patient_dict['Patient_ID'] = '<mobile_user>'
        
        # Calculate BMI if not provided
        if 'BMI' not in patient_dict:
            height_m = patient_dict['Height_cm'] / 100
            patient_dict['BMI'] = patient_dict['Weight_kg'] / (height_m ** 2)
        
        # Compute daily needs
        patient_df = compute_daily_needs(pd.DataFrame([patient_dict]))
        patient_row = patient_df.iloc[0]
        
        # Load model and predict
        clf, le, feature_cols, encoders = load_artifacts()
        X = patient_row_to_features(patient_row, feature_cols, encoders)
        
        pred_enc = clf.predict(X)[0]
        pred_label = le.inverse_transform([pred_enc])[0]
        
        if hasattr(clf, 'predict_proba'):
            probs = clf.predict_proba(X)[0]
            label_probs = {lab: float(p) for lab, p in zip(le.classes_, probs)}
        else:
            label_probs = {pred_label: 1.0}
        
        # Get recipe recommendations
        recipes = load_recipes()
        recipes = clean_recipes(recipes)
        
        candidates = recommend_top_n(
            recipes, 
            patient_row, 
            n=500, 
            diet_label_probs=label_probs
        )
        plan_pool = filter_by_allergies_and_restrictions(candidates, patient_row)
        
        # Generate meal plan
        plans = make_30_day_plan(
            plan_pool, 
            patient_row, 
            days=request.days,
            meals_per_day=request.meals_per_day,
            no_repeat_within_days=request.no_repeat_days
        )
        
        # Format response
        meal_plan = []
        for i, day in enumerate(plans):
            day_total = float(day['calories'].sum()) if not day.empty else 0
            meals = []
            for _, r in day.iterrows():
                meal = MealInfo(
                    recipe_name=r['Recipe_name'],
                    cuisine_type=r.get('Cuisine_type', ''),
                    calories=float(r['calories']),
                    protein=float(r.get('Protein(g)', 0)) if pd.notna(r.get('Protein(g)')) else None,
                    carbs=float(r.get('Carbs(g)', 0)) if pd.notna(r.get('Carbs(g)')) else None,
                    fat=float(r.get('Fat(g)', 0)) if pd.notna(r.get('Fat(g)')) else None
                )
                meals.append(meal)
            
            meal_plan.append(DayPlan(
                day=i + 1,
                total_calories=day_total,
                meals=meals
            ))
        
        return RecommendationResponse(
            patient_id=patient_row.get('Patient_ID', '<mobile_user>'),
            target_calories=float(patient_row.get('target_calories', 0)),
            dietary_restrictions=str(patient_row.get('Dietary_Restrictions', 'None')),
            allergies=str(patient_row.get('Allergies', 'None')),
            total_days=request.days,
            meal_plan=meal_plan
        )
        
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error generating recommendation: {str(e)}\n{traceback.format_exc()}"
        )


@app.post("/api/v1/quick-recommend", tags=["Recommendations"])
async def quick_recommend(request: QuickRecommendRequest):
    """
    Quick recommendation with minimal required fields
    
    Simplified endpoint for mobile apps that need fast recommendations
    with fewer input parameters.
    """
    try:
        # Create full patient object from quick inputs
        height_m = request.height_cm / 100
        bmi = request.weight_kg / (height_m ** 2)
        
        patient_dict = {
            'Patient_ID': '<quick_user>',
            'Age': request.age,
            'Weight_kg': request.weight_kg,
            'Height_cm': request.height_cm,
            'BMI': bmi,
            'Gender': request.gender,
            'Disease_Type': 'None',
            'Physical_Activity_Level': request.activity_level,
            'Weekly_Exercise_Hours': 3.0,  # default
            'Adherence_to_Diet_Plan': 75.0,  # default
            'Dietary_Nutrient_Imbalance_Score': 0.0,
            'Dietary_Restrictions': request.dietary_restrictions,
            'Allergies': request.allergies
        }
        
        # Use the full recommendation logic
        full_request = RecommendationRequest(
            patient=PatientInput(**patient_dict),
            days=request.days,
            meals_per_day=3,
            no_repeat_days=3
        )
        
        return await get_recommendation(full_request)
        
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error in quick recommendation: {str(e)}"
        )


@app.post("/api/v1/predict-diet-type", tags=["Predictions"])
async def predict_diet_type(patient: PatientInput):
    """
    Predict recommended diet type for a patient
    
    Returns the predicted diet category (e.g., Low-Carb, Mediterranean, etc.)
    along with confidence scores for all diet types.
    """
    try:
        patient_dict = patient.dict()
        patient_dict['Patient_ID'] = '<prediction_user>'
        
        # Calculate BMI
        height_m = patient_dict['Height_cm'] / 100
        patient_dict['BMI'] = patient_dict['Weight_kg'] / (height_m ** 2)
        
        # Compute daily needs
        patient_df = compute_daily_needs(pd.DataFrame([patient_dict]))
        patient_row = patient_df.iloc[0]
        
        # Load model and predict
        clf, le, feature_cols, encoders = load_artifacts()
        X = patient_row_to_features(patient_row, feature_cols, encoders)
        
        pred_enc = clf.predict(X)[0]
        pred_label = le.inverse_transform([pred_enc])[0]
        
        # Get probabilities
        if hasattr(clf, 'predict_proba'):
            probs = clf.predict_proba(X)[0]
            all_predictions = {
                lab: float(p) for lab, p in zip(le.classes_, probs)
            }
            # Sort by probability
            all_predictions = dict(sorted(all_predictions.items(), key=lambda x: x[1], reverse=True))
        else:
            all_predictions = {pred_label: 1.0}
        
        return {
            "predicted_diet_type": pred_label,
            "confidence": float(max(all_predictions.values())),
            "all_predictions": all_predictions,
            "target_calories": float(patient_row.get('target_calories', 0)),
            "bmi": float(patient_dict['BMI']),
            "bmi_category": get_bmi_category(patient_dict['BMI'])
        }
        
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error predicting diet type: {str(e)}"
        )


def get_bmi_category(bmi: float) -> str:
    """Categorize BMI value"""
    if bmi < 18.5:
        return "Underweight"
    elif bmi < 25:
        return "Normal"
    elif bmi < 30:
        return "Overweight"
    else:
        return "Obese"


# For running with uvicorn
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
