from ultralytics import YOLO
import torch
import os
import yaml
import shutil
from pathlib import Path

# Try to import psutil for system info (optional)
try:
    import psutil
    HAS_PSUTIL = True
except ImportError:
    HAS_PSUTIL = False
    print("⚠ psutil not installed. Install with: pip install psutil")

# ============= CONFIGURATION =============
ORIGINAL_YAML = r"D:\college work\Mobile\Food detection\yolo_dataset\food_data.yaml"
SUBSET_YAML = r"D:\college work\Mobile\Food detection\yolo_dataset\food_data_subset.yaml"
MODEL_SIZE = 'n'
EPOCHS = 100  # Increased for better learning
IMAGE_SIZE = 640  # Back to standard size for better accuracy
BATCH_SIZE = 8
PROJECT_NAME = 'food_detection_improved'
DEVICE = 'cpu'
PATIENCE = 50  # More patience before early stopping

# SUBSET SETTINGS - Use only X images per class
IMAGES_PER_CLASS = 300  # Increased from 100 for better accuracy
TRAIN_SPLIT = 0.8  # 80 train, 20 val

# CPU Optimization
NUM_WORKERS = 2
CACHE_IMAGES = False
USE_MIXED_PRECISION = False
# =========================================

def create_subset_dataset():
    """Create a smaller dataset with fewer images per class"""
    print("\n" + "=" * 80)
    print("CREATING SUBSET DATASET")
    print("=" * 80)
    
    # Read original YAML
    with open(ORIGINAL_YAML, 'r') as f:
        original_config = yaml.safe_load(f)
    
    original_path = original_config['path']
    train_path = os.path.join(original_path, original_config['train'])
    val_path = os.path.join(original_path, original_config['val'])
    
    # Create subset directories
    subset_path = os.path.join(original_path, "subset")
    subset_train_img = os.path.join(subset_path, "images", "train")
    subset_train_lbl = os.path.join(subset_path, "labels", "train")
    subset_val_img = os.path.join(subset_path, "images", "val")
    subset_val_lbl = os.path.join(subset_path, "labels", "val")
    
    for dir_path in [subset_train_img, subset_train_lbl, subset_val_img, subset_val_lbl]:
        os.makedirs(dir_path, exist_ok=True)
    
    print(f"📁 Created subset directories in: {subset_path}")
    print(f"📊 Configuration: {IMAGES_PER_CLASS} images per class")
    print(f"   Train/Val split: {TRAIN_SPLIT*100:.0f}/{(1-TRAIN_SPLIT)*100:.0f}")
    
    # Get all image files grouped by class
    import glob
    from collections import defaultdict
    
    all_train_images = glob.glob(os.path.join(original_path, "images", "train", "*.jpg"))
    
    # Group images by class
    class_images = defaultdict(list)
    for img_path in all_train_images:
        img_name = os.path.basename(img_path)
        # Extract class name (format: classname_*.jpg)
        class_name = '_'.join(img_name.split('_')[:-1])  # Remove last part after underscore
        class_images[class_name].append(img_path)
    
    total_train = 0
    total_val = 0
    
    print(f"\n🔍 Found {len(class_images)} classes")
    print("📋 Selecting images...")
    
    for class_name, images in class_images.items():
        # Take only IMAGES_PER_CLASS images
        selected = images[:IMAGES_PER_CLASS]
        
        # Split into train/val
        train_count = int(len(selected) * TRAIN_SPLIT)
        train_images = selected[:train_count]
        val_images = selected[train_count:]
        
        # Copy train images
        for img_path in train_images:
            img_name = os.path.basename(img_path)
            label_name = img_name.replace('.jpg', '.txt')
            
            # Copy image
            shutil.copy2(img_path, os.path.join(subset_train_img, img_name))
            
            # Copy label
            original_label = img_path.replace('/images/', '/labels/').replace('.jpg', '.txt')
            original_label = original_label.replace('\\images\\', '\\labels\\')
            if os.path.exists(original_label):
                shutil.copy2(original_label, os.path.join(subset_train_lbl, label_name))
            
            total_train += 1
        
        # Copy val images
        for img_path in val_images:
            img_name = os.path.basename(img_path)
            label_name = img_name.replace('.jpg', '.txt')
            
            shutil.copy2(img_path, os.path.join(subset_val_img, img_name))
            
            original_label = img_path.replace('/images/', '/labels/').replace('.jpg', '.txt')
            original_label = original_label.replace('\\images\\', '\\labels\\')
            if os.path.exists(original_label):
                shutil.copy2(original_label, os.path.join(subset_val_lbl, label_name))
            
            total_val += 1
    
    print(f"\n✅ Subset created:")
    print(f"   Training images: {total_train:,}")
    print(f"   Validation images: {total_val:,}")
    print(f"   Total: {total_train + total_val:,}")
    print(f"   Reduction: {100 * (1 - (total_train + total_val) / len(all_train_images)):.1f}%")
    
    # Create subset YAML
    subset_config = {
        'path': subset_path,
        'train': 'images/train',
        'val': 'images/val',
        'nc': original_config['nc'],
        'names': original_config['names']
    }
    
    with open(SUBSET_YAML, 'w') as f:
        yaml.dump(subset_config, f, default_flow_style=False)
    
    print(f"\n✓ Subset YAML created: {SUBSET_YAML}")
    print("=" * 80 + "\n")
    
    return SUBSET_YAML

def check_system():
    """Check system requirements"""
    print("=" * 80)
    print("SYSTEM CHECK")
    print("=" * 80)
    
    # Check CPU info
    if HAS_PSUTIL:
        cpu_count = psutil.cpu_count(logical=False)
        cpu_count_logical = psutil.cpu_count(logical=True)
        ram_gb = psutil.virtual_memory().total / 1e9
        
        print(f"✓ CPU Cores: {cpu_count} physical, {cpu_count_logical} logical")
        print(f"✓ RAM: {ram_gb:.1f} GB")
    else:
        print("✓ System info: psutil not available")
    
    if torch.cuda.is_available():
        print(f"✓ GPU Available: {torch.cuda.get_device_name(0)}")
    else:
        print("⚠ Training on CPU - using FAST mode with subset")
        print(f"✓ CPU Optimization: Using {NUM_WORKERS} workers")
    
    if not os.path.exists(ORIGINAL_YAML):
        print(f"\n✗ Error: YAML file not found: {ORIGINAL_YAML}")
        return False
    else:
        print(f"✓ Dataset configuration found")
    
    print("=" * 80 + "\n")
    return True

def train_model(yaml_path):
    """Train YOLOv8 model on subset"""
    
    print("=" * 80)
    print("FAST TRAINING CONFIGURATION")
    print("=" * 80)
    print(f"Model: YOLOv8{MODEL_SIZE}")
    print(f"Dataset: Subset ({IMAGES_PER_CLASS} images/class)")
    print(f"Epochs: {EPOCHS}")
    print(f"Image Size: {IMAGE_SIZE}")
    print(f"Batch Size: {BATCH_SIZE}")
    print(f"Device: {DEVICE}")
    print("=" * 80 + "\n")
    
    # Load model
    print(f"Loading YOLOv8{MODEL_SIZE}...")
    model = YOLO(f'yolov8{MODEL_SIZE}.pt')
    print("✓ Model loaded\n")
    
    print("🚀 Starting FAST training...")
    print("-" * 80)
    
    try:
        results = model.train(
            data=yaml_path,
            epochs=EPOCHS,
            imgsz=IMAGE_SIZE,
            batch=BATCH_SIZE,
            name=PROJECT_NAME,
            patience=PATIENCE,
            save=True,
            device=DEVICE,
            
            # CPU OPTIMIZATIONS
            workers=NUM_WORKERS,
            cache=CACHE_IMAGES,
            amp=USE_MIXED_PRECISION,
            rect=False,
            
            plots=True,
            verbose=True,
            save_period=-1,
            
            # Better optimizer settings
            optimizer='AdamW',  # Better for learning
            lr0=0.001,
            lrf=0.01,
            momentum=0.937,
            weight_decay=0.0005,
            
            # BETTER augmentation for accuracy (moderate, not too heavy for CPU)
            hsv_h=0.015,  # Color variation
            hsv_s=0.7,
            hsv_v=0.4,
            degrees=10.0,  # Small rotation
            translate=0.1,
            scale=0.5,
            shear=0.0,  # Keep disabled for CPU
            perspective=0.0,
            flipud=0.0,
            fliplr=0.5,  # Horizontal flip is cheap and useful
            mosaic=1.0,  # Full mosaic for better learning
            mixup=0.1,  # Small mixup
            copy_paste=0.0,
            close_mosaic=10,  # Keep mosaic longer
        )
        
        print("-" * 80)
        print("\n✅ Training completed!")
        print(f"\nModel saved to: runs/detect/{PROJECT_NAME}/weights/best.pt")
        print("=" * 80)
        
        return True
        
    except Exception as e:
        print(f"\n✗ Training failed: {e}")
        return False

def main():
    print("\n" + "=" * 80)
    print("YOLOv8 FAST TRAINING MODE (CPU Optimized)")
    print("=" * 80)
    print("\n⚡ This uses a SUBSET of your data for faster training")
    print(f"   {IMAGES_PER_CLASS} images per class × 101 classes")
    print(f"   = ~{IMAGES_PER_CLASS * 101:,} total images (vs 101,000 original)")
    print(f"\n⏱️  Estimated time per epoch: 15-30 minutes (vs 8-12 hours)")
    print(f"   Total training time: ~12-25 hours (vs 800-1200 hours)\n")
    
    if not check_system():
        return
    
    # Create subset if needed
    if not os.path.exists(SUBSET_YAML):
        print("📦 Creating subset dataset (this may take a few minutes)...\n")
        yaml_path = create_subset_dataset()
    else:
        print("✓ Subset dataset already exists\n")
        yaml_path = SUBSET_YAML
    
    # Train
    success = train_model(yaml_path)
    
    if success:
        print("\n✅ Training completed successfully!")
        print(f"\n💡 Your model is trained on {IMAGES_PER_CLASS} images per class")
        print("   This is sufficient for good performance!")
        print(f"\n📁 Model location: runs/detect/{PROJECT_NAME}/weights/best.pt")
    else:
        print("\n✗ Training failed")

if __name__ == "__main__":
    main()
