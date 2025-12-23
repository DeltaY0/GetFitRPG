from flask import Flask, request, jsonify
from flask_cors import CORS
from ultralytics import YOLO
import cv2
import numpy as np
import base64
import os
from PIL import Image
import io
import traceback

app = Flask(__name__)
CORS(app)

# ============= CONFIGURATION =============
MODEL_PATH = r"D:\college work\term5\Mobile\Food detection\best.pt"
CONFIDENCE_THRESHOLD = 0.3
TOP_K_PREDICTIONS = 3
# =========================================

# Load model at startup
print("Loading food detection model...")
if not os.path.exists(MODEL_PATH):
    raise FileNotFoundError(f"Model not found at {MODEL_PATH}")

model = YOLO(MODEL_PATH)
print(f"✓ Model loaded successfully! ({len(model.names)} classes)")

def process_image(image_array):
    """Process image and return predictions"""
    try:
        # Make sure image is in the right format
        if len(image_array.shape) == 2:
            # Grayscale to BGR
            image_array = cv2.cvtColor(image_array, cv2.COLOR_GRAY2BGR)
        elif image_array.shape[2] == 4:
            # RGBA to BGR
            image_array = cv2.cvtColor(image_array, cv2.COLOR_RGBA2BGR)
        
        # Run inference
        results = model.predict(
            source=image_array,
            conf=CONFIDENCE_THRESHOLD,
            verbose=False
        )
        
        result = results[0]
        
        # Check if we have any detections
        if result.probs is not None:
            # Classification model
            top5_indices = result.probs.top5
            top5_conf = result.probs.top5conf.cpu().numpy()
            
            predictions = []
            for idx, conf in zip(top5_indices, top5_conf):
                predictions.append({
                    'food_name': model.names[int(idx)],
                    'confidence': round(float(conf) * 100, 2),
                    'class_id': int(idx)
                })
            
            return {
                'success': True,
                'message': 'Food detected successfully',
                'predictions': predictions[:TOP_K_PREDICTIONS],
                'top_prediction': predictions[0] if predictions else None
            }
        elif len(result.boxes) > 0:
            # Detection model
            predictions = []
            for box in result.boxes:
                class_id = int(box.cls[0])
                confidence = float(box.conf[0])
                class_name = model.names[class_id]
                predictions.append({
                    'food_name': class_name,
                    'confidence': round(confidence * 100, 2),
                    'class_id': class_id
                })
            
            predictions.sort(key=lambda x: x['confidence'], reverse=True)
            
            return {
                'success': True,
                'message': 'Food detected successfully',
                'predictions': predictions[:TOP_K_PREDICTIONS],
                'top_prediction': predictions[0] if predictions else None
            }
        else:
            return {
                'success': False,
                'message': 'No confident predictions',
                'predictions': []
            }
        
    except Exception as e:
        print(f"Error in process_image: {str(e)}")
        traceback.print_exc()
        return {
            'success': False,
            'message': f'Error processing image: {str(e)}',
            'predictions': []
        }

@app.route('/', methods=['GET'])
def root():
    """Root endpoint"""
    return jsonify({
        'success': True,
        'message': 'Food Detection API',
        'version': '1.0',
        'endpoints': {
            'health': 'GET /api/health',
            'detect': 'POST /api/detect',
            'detect_base64': 'POST /api/detect/base64',
            'classes': 'GET /api/classes',
            'config': 'GET /api/config'
        }
    })

@app.route('/api', methods=['GET'])
def api_root():
    """API root"""
    return root()

@app.route('/api/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'online',
        'model_loaded': True,
        'num_classes': len(model.names),
        'model_path': MODEL_PATH
    })

@app.route('/api/detect', methods=['POST'])
def detect_food():
    """Detect food from uploaded image"""
    try:
        if 'image' not in request.files:
            return jsonify({
                'success': False,
                'message': 'No image file provided. Send image as multipart/form-data with key "image"'
            }), 400
        
        file = request.files['image']
        
        if file.filename == '':
            return jsonify({
                'success': False,
                'message': 'Empty filename'
            }), 400
        
        # Read image
        image_bytes = file.read()
        image = Image.open(io.BytesIO(image_bytes))
        
        # Convert to numpy array
        image_np = np.array(image)
        
        # Convert RGB to BGR if needed
        if len(image_np.shape) == 3 and image_np.shape[2] == 3:
            image_np = cv2.cvtColor(image_np, cv2.COLOR_RGB2BGR)
        
        # Process
        result = process_image(image_np)
        return jsonify(result), 200
        
    except Exception as e:
        print(f"Error in detect_food: {str(e)}")
        traceback.print_exc()
        return jsonify({
            'success': False,
            'message': f'Error: {str(e)}'
        }), 500

@app.route('/api/detect/base64', methods=['POST'])
def detect_food_base64():
    """Detect food from base64 encoded image"""
    try:
        data = request.get_json()
        
        if not data or 'image' not in data:
            return jsonify({
                'success': False,
                'message': 'No image data provided. Send JSON with "image" field containing base64 string'
            }), 400
        
        base64_string = data['image']
        
        # Remove header if present
        if ',' in base64_string:
            base64_string = base64_string.split(',')[1]
        
        # Decode
        image_bytes = base64.b64decode(base64_string)
        image = Image.open(io.BytesIO(image_bytes))
        
        # Convert to numpy
        image_np = np.array(image)
        
        if len(image_np.shape) == 3 and image_np.shape[2] == 3:
            image_np = cv2.cvtColor(image_np, cv2.COLOR_RGB2BGR)
        
        # Process
        result = process_image(image_np)
        return jsonify(result), 200
        
    except Exception as e:
        print(f"Error in detect_food_base64: {str(e)}")
        traceback.print_exc()
        return jsonify({
            'success': False,
            'message': f'Error: {str(e)}'
        }), 500

@app.route('/api/classes', methods=['GET'])
def get_classes():
    """Get all food classes"""
    try:
        classes = [{'id': i, 'name': name} for i, name in model.names.items()]
        return jsonify({
            'success': True,
            'total_classes': len(classes),
            'classes': classes
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

@app.route('/api/config', methods=['GET'])
def get_config():
    """Get configuration"""
    return jsonify({
        'success': True,
        'config': {
            'confidence_threshold': CONFIDENCE_THRESHOLD,
            'top_k_predictions': TOP_K_PREDICTIONS,
            'num_classes': len(model.names)
        }
    })

@app.route('/api/config', methods=['POST'])
def update_config():
    """Update configuration"""
    global CONFIDENCE_THRESHOLD, TOP_K_PREDICTIONS
    
    try:
        data = request.get_json()
        
        if 'confidence_threshold' in data:
            CONFIDENCE_THRESHOLD = float(data['confidence_threshold'])
        
        if 'top_k_predictions' in data:
            TOP_K_PREDICTIONS = int(data['top_k_predictions'])
        
        return jsonify({
            'success': True,
            'message': 'Configuration updated',
            'config': {
                'confidence_threshold': CONFIDENCE_THRESHOLD,
                'top_k_predictions': TOP_K_PREDICTIONS
            }
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'message': f'Error: {str(e)}'
        }), 500

@app.errorhandler(404)
def not_found(error):
    return jsonify({
        'success': False,
        'message': 'Endpoint not found',
        'requested_url': request.url,
        'tip': 'All API endpoints must start with /api/ (e.g., /api/health, /api/detect)',
        'available_endpoints': {
            'GET /': 'API info',
            'GET /api': 'API info',
            'GET /api/health': 'Health check',
            'POST /api/detect': 'Detect food (multipart)',
            'POST /api/detect/base64': 'Detect food (base64)',
            'GET /api/classes': 'Get all classes',
            'GET /api/config': 'Get config',
            'POST /api/config': 'Update config'
        }
    }), 404

@app.errorhandler(405)
def method_not_allowed(error):
    return jsonify({
        'success': False,
        'message': 'Method not allowed',
        'tip': '/api/detect requires POST request with image file, not GET',
        'your_method': request.method,
        'your_url': request.url
    }), 405

@app.errorhandler(500)
def internal_error(error):
    return jsonify({
        'success': False,
        'message': 'Internal server error'
    }), 500

if __name__ == '__main__':
    print("\n" + "=" * 80)
    print("🍕 FOOD DETECTION API SERVER")
    print("=" * 80)
    print(f"\n✓ Model loaded: {MODEL_PATH}")
    print(f"✓ Number of food classes: {len(model.names)}")
    print(f"✓ Confidence threshold: {CONFIDENCE_THRESHOLD}")
    print(f"✓ Top predictions: {TOP_K_PREDICTIONS}")
    print("\n📡 API Endpoints:")
    print("   GET  /api/health          - Health check")
    print("   POST /api/detect          - Detect food (multipart/form-data)")
    print("   POST /api/detect/base64   - Detect food (base64)")
    print("   GET  /api/classes         - Get all food classes")
    print("   GET  /api/config          - Get configuration")
    print("   POST /api/config          - Update configuration")
    print("\n🌐 Server starting on http://0.0.0.0:5000")
    print("   Local: http://localhost:5000/api/health")
    print("   Network: http://<YOUR_IP>:5000/api/health")
    print("\n⚠️  Make sure firewall allows connections on port 5000")
    print("=" * 80 + "\n")
    
    app.run(host='0.0.0.0', port=5000, debug=False, threaded=True)
