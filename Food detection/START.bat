@echo off
echo ========================================
echo   Food Detection - Starting System
echo ========================================
echo.

REM Kill any existing Python processes
taskkill /F /IM python.exe >nul 2>&1

echo [1/2] Starting API Server...
start "Food Detection API" /MIN python food_detection_api.py

REM Wait for server to start
timeout /t 3 /nobreak >nul

echo [2/2] Testing API connection...
python -c "import requests; r = requests.get('http://localhost:5000/api/health', timeout=5); print('API Status:', r.json()['status'])" 2>nul

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   SUCCESS! API Server is running
    echo ========================================
    echo.
    echo [3/3] Starting GUI...
    python food_detection_gui.py
) else (
    echo.
    echo ========================================
    echo   ERROR: API Server failed to start
    echo ========================================
    echo.
    echo Please check:
    echo   1. Python is installed
    echo   2. All dependencies are installed
    echo   3. Model file exists: best.pt
    echo.
    pause
)
