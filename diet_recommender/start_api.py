"""
Startup script for Diet Recommender API
Runs the FastAPI server using uvicorn
"""
import sys
import os
from pathlib import Path

# Add parent directory to path
sys.path.insert(0, str(Path(__file__).parent.parent))

if __name__ == "__main__":
    import uvicorn
    
    print("=" * 80)
    print("  DIET RECOMMENDER API SERVER")
    print("=" * 80)
    print("\nStarting API server...")
    print("API Documentation: http://localhost:8000/docs")
    print("Alternative Docs: http://localhost:8000/redoc")
    print("Health Check: http://localhost:8000/health")
    print("\nPress CTRL+C to stop the server\n")
    print("=" * 80 + "\n")
    
    uvicorn.run(
        "api.main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info"
    )
