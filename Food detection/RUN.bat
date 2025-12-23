@echo off
REM Food Detection - Complete Startup Script
REM This starts the API server and GUI application

title Food Detection System

echo.
echo ========================================
echo   Food Detection System - Starting
echo ========================================
echo.

REM Navigate to project directory
cd /d "%~dp0"

REM Kill any existing Python processes to avoid conflicts
echo [Step 1/3] Cleaning up old processes...
taskkill /F /IM python.exe >nul 2>&1
timeout /t 1 /nobreak >nul

REM Start API server in a new window
echo [Step 2/3] Starting API Server...
start "Food Detection API Server" python food_detection_api.py

REM Wait for API to start (5 seconds)
echo [Step 3/3] Waiting for API to load...
timeout /t 5 /nobreak >nul

REM Test if API is running
python -c "import requests; requests.get('http://localhost:5000/api/health', timeout=2)" >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   SUCCESS! API is running
    echo ========================================
    echo.
    echo Starting GUI application...
    echo.
    REM Start GUI in the current window
    python food_detection_gui.py
) else (
    echo.
    echo ========================================
    echo   ERROR: API failed to start
    echo ========================================
    echo.
    echo Please check:
    echo   - Python is installed
    echo   - Dependencies installed: pip install -r requirements.txt
    echo   - Model file exists: best.pt
    echo.
    pause
)
