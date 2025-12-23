# Files to Remove - Cleanup Guide

## Redundant/Obsolete Files

The following files can be safely removed as they have been replaced or are no longer needed:

### 1. Old API Files (Replaced by `api/main.py`)
- `api.py` - Old API implementation, replaced by organized `api/main.py`

### 2. Old Test Files (Replaced by `api/test_api.py`)
- `test_api_call.py` - Basic API test, replaced by comprehensive test suite
- `test_api_custom.py` - Custom API test, functionality merged into main test
- `test_job_flow.py` - Job flow test, not needed for mobile API

### 3. Temporary/Demo Files
- `tmp_ilp_test.py` - Temporary ILP testing file
- `p0002_plan.json` - Sample output file
- `run_with_synthetic_patient.py` - Demo script, functionality available in scripts/

### 4. ILP Planner (Optional - Move to ai/ if needed)
- `planner_ilp.py` - Should be moved to `ai/` folder if ILP planning is used
- `ilp_run.py` - ILP runner, move to scripts/ if needed

### 5. Old README
- `README.md` - Replace with `README_NEW.md`

## Cleanup Commands

```powershell
# Remove redundant files
Remove-Item "api.py"
Remove-Item "test_api_call.py"
Remove-Item "test_api_custom.py"
Remove-Item "test_job_flow.py"
Remove-Item "tmp_ilp_test.py"
Remove-Item "p0002_plan.json"
Remove-Item "run_with_synthetic_patient.py"

# Move ILP files if needed (otherwise delete)
Move-Item "planner_ilp.py" "ai/planner_ilp.py"
Move-Item "ilp_run.py" "scripts/ilp_run.py"

# Replace README
Remove-Item "README.md"
Rename-Item "README_NEW.md" "README.md"

# Clean pycache
Remove-Item -Recurse -Force "__pycache__"
Remove-Item -Recurse -Force ".pytest_cache"
```

## Files to Keep

### Core Modules (Organized in folders)
- `api/main.py` - Main API server
- `api/test_api.py` - API test suite
- `ai/train.py` - Model training
- `ai/predict.py` - Prediction logic
- `ai/recommend.py` - Recommendation engine
- `ai/planner.py` - Meal planner
- `ai/model_utils.py` - Model utilities
- `utils/preprocess.py` - Data preprocessing
- `gui/api_tester.py` - GUI tester

### Scripts
- `scripts/run_demo.py`
- `scripts/run_for_patient.py`
- `scripts/run_uvicorn.py`

### Startup Scripts
- `start_api.py` - Start API server
- `quick_start.py` - Quick start with GUI

### Data & Models
- `models/` - All model files
- `data/` - Data files
- `tests/` - Unit tests

### Configuration
- `requirements.txt` - Dependencies
- `README.md` - Documentation
