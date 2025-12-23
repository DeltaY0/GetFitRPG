"""
Quick start script - Launches API server and GUI tester
"""
import subprocess
import sys
import time
from pathlib import Path
import os

def main():
    print("=" * 80)
    print("  DIET RECOMMENDER - QUICK START")
    print("=" * 80)
    print("\n1. Starting API Server...")
    print("-" * 80)
    
    # Start API server in background
    api_process = subprocess.Popen(
        [sys.executable, "start_api.py"],
        cwd=Path(__file__).parent,
        creationflags=subprocess.CREATE_NEW_CONSOLE if os.name == 'nt' else 0
    )
    
    print("API server starting in new window...")
    print("  API URL: http://localhost:8000")
    print("  Docs: http://localhost:8000/docs")
    
    # Wait for API to start
    print("\nWaiting for API to start (5 seconds)...")
    time.sleep(5)
    
    print("\n2. Launching GUI Tester...")
    print("-" * 80)
    print("Opening GUI application...")
    
    # Launch GUI
    try:
        gui_process = subprocess.Popen(
            [sys.executable, "gui/api_tester.py"],
            cwd=Path(__file__).parent
        )
        
        print("\n" + "=" * 80)
        print("  SYSTEM RUNNING")
        print("=" * 80)
        print("\nAPI Server: Running on http://localhost:8000")
        print("GUI Tester: Open")
        print("\nTo stop:")
        print("  - Close the GUI window")
        print("  - Close the API server window")
        print("  - Or press CTRL+C here")
        print("\n" + "=" * 80)
        
        # Wait for GUI to close
        gui_process.wait()
        
    except KeyboardInterrupt:
        print("\n\nShutting down...")
    finally:
        print("Cleaning up...")
        try:
            api_process.terminate()
        except:
            pass

if __name__ == "__main__":
    main()
