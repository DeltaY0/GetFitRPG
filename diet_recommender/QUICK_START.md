#  QUICK START GUIDE - Diet Recommender API

## Prerequisites
- Python 3.8 or higher
- pip package manager
- Virtual environment (recommended)

## Installation & Setup

### 1. Navigate to Project Directory
```powershell
cd "d:\college work\term5\Mobile\diet_recommender"
```

### 2. Create Virtual Environment (Recommended)
```powershell
python -m venv venv
```

### 3. Activate Virtual Environment
```powershell
.\venv\Scripts\Activate.ps1
```

### 4. Install Dependencies
```powershell
pip install -r requirements.txt
```

### 5. Verify Installation
```powershell
python -c "import fastapi, pandas, sklearn; print('All dependencies installed!')"
```

## Running the Application

###  Option 1: Quick Start (Easiest - API + GUI)
```powershell
python quick_start.py
```
This will:
- Start the API server in a new window
- Open the GUI tester
- You can immediately test the API

###  Option 2: Start API Server Only
```powershell
python start_api.py
```
Then visit: http://localhost:8000/docs

###  Option 3: Manual Start with Uvicorn
```powershell
uvicorn api.main:app --reload --host 0.0.0.0 --port 8000
```

## Testing the API

### Method 1: Automated Test Script
```powershell
# Make sure API is running first!
python api/test_api.py
```

### Method 2: GUI Tester
```powershell
python gui/api_tester.py
```

### Method 3: Browser (Swagger UI)
1. Start the API server
2. Open browser to: http://localhost:8000/docs
3. Try the endpoints interactively

### Method 4: Command Line (curl or PowerShell)
```powershell
# Health check
curl http://localhost:8000/health

# Quick recommendation
curl -X POST http://localhost:8000/api/v1/quick-recommend `
  -H "Content-Type: application/json" `
  -d '{
    "age": 30,
    "weight_kg": 70,
    "height_cm": 170,
    "gender": "Male",
    "activity_level": "Moderate",
    "dietary_restrictions": "None",
    "allergies": "None",
    "days": 7
  }'
```

## Troubleshooting

### Issue: "Module not found" errors
**Solution:**
```powershell
# Make sure you're in the project directory
cd "d:\college work\term5\Mobile\diet_recommender"

# Make sure virtual environment is activated
.\venv\Scripts\Activate.ps1

# Reinstall dependencies
pip install -r requirements.txt
```

### Issue: "Model files not found"
**Solution:**
```powershell
# Train the model first
python ai/train.py
```
Note: This requires the dataset files in `../DataSets/` directory

### Issue: "Port 8000 already in use"
**Solution:**
```powershell
# Option 1: Find and kill the process
Get-Process -Id (Get-NetTCPConnection -LocalPort 8000).OwningProcess | Stop-Process

# Option 2: Use a different port
uvicorn api.main:app --port 8080
```

### Issue: GUI window doesn't open
**Solution:**
```powershell
# Install tkinter (usually comes with Python)
# On Windows, tkinter should be included

# Test tkinter
python -c "import tkinter; print('Tkinter OK')"
```

### Issue: API returns 500 errors
**Check:**
1. Are the model files present in `models/` directory?
2. Are the data files present in `data/` directory?
3. Check the console for detailed error messages
4. Check the API documentation at `/docs`

## Project Structure Reference

```
diet_recommender/
 api/
    main.py              ← Main API server
    test_api.py          ← API tests
    __init__.py
 ai/
    train.py             ← Train the model
    predict.py           ← Prediction logic
    recommend.py         ← Recommendation engine
    planner.py           ← Meal planner
    planner_ilp.py       ← ILP planner (optional)
    model_utils.py       ← Model utilities
    __init__.py
 utils/
    preprocess.py        ← Data preprocessing
    __init__.py
 gui/
    api_tester.py        ← GUI application
    __init__.py
 scripts/
    run_demo.py
    run_for_patient.py
    run_uvicorn.py
    ilp_run.py
 models/                   ← Trained models (created by train.py)
 data/                     ← Data files
 tests/                    ← Unit tests
 start_api.py             ← Start API server
 quick_start.py           ← Quick start script
 requirements.txt         ← Dependencies
 README.md                ← Full documentation
```

## API Endpoints Summary

| Endpoint | Method | Purpose | Complexity |
|----------|--------|---------|------------|
| `/health` | GET | Check API status | Simple |
| `/api/v1/quick-recommend` | POST | Quick recommendation | Easy |
| `/api/v1/recommend` | POST | Full recommendation | Medium |
| `/api/v1/predict-diet-type` | POST | Predict diet type | Easy |

## Common Use Cases

### Use Case 1: Test if API is working
```powershell
# Start API
python start_api.py

# In another terminal, test health endpoint
curl http://localhost:8000/health
```

### Use Case 2: Get a quick recommendation
```powershell
# Start API
python start_api.py

# Run GUI tester
python gui/api_tester.py

# Fill in the Quick Recommend tab and click submit
```

### Use Case 3: Run all tests
```powershell
# Start API first
python start_api.py

# In another terminal, run tests
python api/test_api.py
```

### Use Case 4: Integrate with Kotlin app
1. Start API: `python start_api.py`
2. Find your IP address: `ipconfig` (look for IPv4 Address)
3. In Kotlin app, use: `http://YOUR_IP:8000/api/v1/quick-recommend`
4. See README.md for Kotlin code examples

## Next Steps

1.  Start the API server
2.  Test with GUI or automated tests
3.  Review API documentation at `/docs`
4.  Integrate with your Kotlin mobile app
5.  Customize as needed

## Support Files

- `README.md` - Complete documentation
- `IMPLEMENTATION_SUMMARY.md` - What was built
- `CLEANUP_GUIDE.md` - Cleanup reference
- `/docs` endpoint - Interactive API documentation

## Success Indicators

 API server starts without errors
 Health endpoint returns `"status": "healthy"`
 Model files exist in `models/` directory
 Test script passes all tests
 GUI opens and connects successfully

---

**You're all set! **

If you encounter any issues, check the troubleshooting section above or review the README.md file.

For API documentation, always refer to: http://localhost:8000/docs (when server is running)
