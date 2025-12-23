from ultralytics import YOLO
import torch
import os

# Try to import psutil for system info (optional)
try:
    import psutil
    HAS_PSUTIL = True
except ImportError:
    HAS_PSUTIL = False
    print("⚠ psutil not installed. Install with: pip install psutil")

# ============= CONFIGURATION =============
YAML_PATH = r"D:\college work\Mobile\Food detection\yolo_dataset\food_data.yaml"
MODEL_SIZE = 'n'  # Options: 'n' (nano), 's' (small), 'm' (medium), 'l' (large), 'x' (xlarge)
EPOCHS = 100
IMAGE_SIZE = 640
BATCH_SIZE = 8  # Reduced for CPU efficiency (was 16)
PROJECT_NAME = 'food_detection'
DEVICE = 'cpu'  # Set to 'cpu' for CPU training
PATIENCE = 50  # Early stopping patience

# CPU Optimization Settings
NUM_WORKERS = 2  # Reduced workers to prevent CPU overload (default would be 4-8)
CACHE_IMAGES = False  # Don't cache to RAM to save memory
USE_MIXED_PRECISION = False  # Disable AMP on CPU (only works on GPU)
# =========================================

def check_system():
    """Check system requirements"""
    print("=" * 80)
    print("SYSTEM CHECK")
    print("=" * 80)
    
    # Check CPU info
    if HAS_PSUTIL:
        cpu_count = psutil.cpu_count(logical=False)  # Physical cores
        cpu_count_logical = psutil.cpu_count(logical=True)  # Logical cores
        ram_gb = psutil.virtual_memory().total / 1e9
        
        print(f"✓ CPU Cores: {cpu_count} physical, {cpu_count_logical} logical")
        print(f"✓ RAM: {ram_gb:.1f} GB")
    else:
        print("✓ System info: psutil not available")
    
    # Check CUDA availability
    if torch.cuda.is_available() and DEVICE != 'cpu':
        print(f"✓ GPU Available: {torch.cuda.get_device_name(0)}")
        print(f"✓ CUDA Version: {torch.version.cuda}")
        print(f"✓ GPU Memory: {torch.cuda.get_device_properties(0).total_memory / 1e9:.2f} GB")
    else:
        print("⚠ Training on CPU - optimized for efficiency")
        print(f"✓ CPU Optimization: Using {NUM_WORKERS} workers to prevent overload")
    
    # Check if YAML file exists
    if not os.path.exists(YAML_PATH):
        print(f"\n✗ Error: YAML file not found: {YAML_PATH}")
        print("Please run the dataset restructuring script first!")
        return False
    else:
        print(f"✓ Dataset configuration found: {YAML_PATH}")
    
    print("=" * 80 + "\n")
    return True

def train_model():
    """Train YOLOv8 model"""
    
    print("=" * 80)
    print("TRAINING CONFIGURATION")
    print("=" * 80)
    print(f"Model: YOLOv8{MODEL_SIZE}")
    print(f"Dataset: {YAML_PATH}")
    print(f"Epochs: {EPOCHS}")
    print(f"Image Size: {IMAGE_SIZE}")
    print(f"Batch Size: {BATCH_SIZE}")
    print(f"Device: {DEVICE}")
    print(f"Early Stopping Patience: {PATIENCE}")
    print("=" * 80 + "\n")
    
    # Load pretrained model
    print(f"Loading YOLOv8{MODEL_SIZE} pretrained model...")
    model = YOLO(f'yolov8{MODEL_SIZE}.pt')
    print("✓ Model loaded successfully\n")
    
    # Train the model
    print("Starting training with CPU optimizations...")
    print("-" * 80)
    
    try:
        results = model.train(
            data=YAML_PATH,
            epochs=EPOCHS,
            imgsz=IMAGE_SIZE,
            batch=BATCH_SIZE,
            name=PROJECT_NAME,
            patience=PATIENCE,
            save=True,
            device=DEVICE,
            
            # CPU OPTIMIZATIONS
            workers=NUM_WORKERS,  # Reduced workers to prevent CPU overload
            cache=CACHE_IMAGES,  # Don't cache images in RAM
            amp=USE_MIXED_PRECISION,  # Disable AMP on CPU
            rect=False,  # Rectangular training (slightly faster)
            
            # Training options
            plots=True,  # Create training plots
            verbose=True,  # Verbose output
            save_period=-1,  # Don't save intermediate checkpoints (saves disk I/O)
            
            # Optimization parameters - tuned for CPU
            optimizer='SGD',  # SGD is faster than Adam on CPU
            lr0=0.01,  # Initial learning rate
            lrf=0.01,  # Final learning rate
            momentum=0.937,
            weight_decay=0.0005,
            warmup_epochs=3.0,  # Warmup epochs
            warmup_momentum=0.8,
            warmup_bias_lr=0.1,
            
            # REDUCED AUGMENTATION for faster training on CPU
            hsv_h=0.01,  # Reduced hue augmentation (was 0.015)
            hsv_s=0.5,  # Reduced saturation (was 0.7)
            hsv_v=0.3,  # Reduced value (was 0.4)
            degrees=0.0,  # No rotation (expensive on CPU)
            translate=0.05,  # Reduced translation (was 0.1)
            scale=0.3,  # Reduced scaling (was 0.5)
            shear=0.0,  # No shear (expensive)
            perspective=0.0,  # No perspective (expensive)
            flipud=0.0,  # No vertical flip
            fliplr=0.5,  # Keep horizontal flip (cheap operation)
            mosaic=0.5,  # Reduced mosaic frequency (was 1.0, expensive)
            mixup=0.0,  # No mixup (expensive)
            copy_paste=0.0,  # No copy-paste augmentation
            
            # Close mosaic early to improve convergence
            close_mosaic=10,  # Disable mosaic in last 10 epochs
        )
        
        print("-" * 80)
        print("\n✓ Training completed successfully!")
        
        # Display results location
        print("\n" + "=" * 80)
        print("TRAINING RESULTS")
        print("=" * 80)
        print(f"Model weights saved to: runs/detect/{PROJECT_NAME}/weights/")
        print(f"  - best.pt (best model)")
        print(f"  - last.pt (last epoch)")
        print(f"\nTraining plots saved to: runs/detect/{PROJECT_NAME}/")
        print("=" * 80)
        
        return True
        
    except Exception as e:
        print(f"\n✗ Training failed with error: {e}")
        return False

def validate_model():
    """Validate the trained model"""
    best_model_path = f"runs/detect/{PROJECT_NAME}/weights/best.pt"
    
    if not os.path.exists(best_model_path):
        print("\n⚠ Best model not found for validation")
        return
    
    print("\n" + "=" * 80)
    print("VALIDATING BEST MODEL")
    print("=" * 80)
    
    model = YOLO(best_model_path)
    metrics = model.val(data=YAML_PATH)
    
    print("\nValidation Metrics:")
    print(f"  mAP50: {metrics.box.map50:.4f}")
    print(f"  mAP50-95: {metrics.box.map:.4f}")
    print(f"  Precision: {metrics.box.mp:.4f}")
    print(f"  Recall: {metrics.box.mr:.4f}")
    print("=" * 80)

def test_inference():
    """Test inference on a sample image"""
    best_model_path = f"runs/detect/{PROJECT_NAME}/weights/best.pt"
    
    if not os.path.exists(best_model_path):
        print("\n⚠ Best model not found for testing")
        return
    
    print("\n" + "=" * 80)
    print("INFERENCE TEST")
    print("=" * 80)
    print("To test your model on an image, use:")
    print(f"\n  from ultralytics import YOLO")
    print(f"  model = YOLO('{best_model_path}')")
    print(f"  results = model.predict('path/to/image.jpg', save=True)")
    print(f"  results[0].show()  # Display results")
    print("=" * 80)

def main():
    print("\n")
    print("=" * 80)
    print("YOLOv8 FOOD DETECTION TRAINING")
    print("=" * 80)
    print("\n")
    
    # System check
    if not check_system():
        return
    
    # Train model
    success = train_model()
    
    if success:
        # Validate model
        validate_model()
        
        # Show inference instructions
        test_inference()
        
        print("\n✓ All steps completed successfully!")
        print("\nYour model is ready for use!")
    else:
        print("\n✗ Training was not successful. Please check the errors above.")

if __name__ == "__main__":
    main()