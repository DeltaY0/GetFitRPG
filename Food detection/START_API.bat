@echo off
REM Quick Start Script for Food Detection API

echo ========================================
echo   Food Detection API - Quick Start
echo ========================================
echo.

REM Check if Python is installed
python --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Python is not installed or not in PATH
    echo Please install Python 3.8 or higher
    pause
    exit /b 1
)

echo [1/3] Checking dependencies...
pip show flask >nul 2>&1
if errorlevel 1 (
    echo Installing dependencies...
    pip install -r requirements.txt
) else (
    echo Dependencies OK
)

echo.
echo [2/3] Finding your IP address...
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /c:"IPv4 Address"') do (
    set IP=%%a
    goto :found
)
:found
set IP=%IP:~1%
echo Your PC IP: %IP%
echo.
echo Access API from mobile: http://%IP%:5000/api/health
echo.

echo [3/3] Starting API server...
echo.
echo Press Ctrl+C to stop the server
echo ========================================
echo.

python food_detection_api.py

pause
