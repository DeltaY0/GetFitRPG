import os
import shutil
from pathlib import Path
import random
from PIL import Image

# ============= CONFIGURATION =============
SOURCE_PATH = r"D:\college work\Mobile\Food detection\DataSets\archive (3)\images"
DESTINATION_PATH = r"D:\college work\Mobile\Food detection\yolo_dataset"
TRAIN_SPLIT = 0.8  # 80% train, 20% validation
RANDOM_SEED = 42
# =========================================

random.seed(RANDOM_SEED)

def create_directory_structure(base_path):
    """Create YOLO directory structure"""
    directories = [
        f"{base_path}/images/train",
        f"{base_path}/images/val",
        f"{base_path}/labels/train",
        f"{base_path}/labels/val"
    ]
    for directory in directories:
        os.makedirs(directory, exist_ok=True)
    print("✓ Directory structure created")

def get_food_classes(source_path):
    """Get all food class folders"""
    food_folders = [f for f in os.listdir(source_path) 
                   if os.path.isdir(os.path.join(source_path, f))]
    food_folders.sort()
    return food_folders

def get_image_files(folder_path):
    """Get all image files from a folder"""
    valid_extensions = ('.jpg', '.jpeg', '.png', '.bmp', '.JPG', '.JPEG', '.PNG', '.BMP')
    images = [f for f in os.listdir(folder_path) 
             if f.endswith(valid_extensions)]
    return images

def create_yolo_label(class_id, output_path):
    """Create YOLO format label file (full image annotation)"""
    with open(output_path, 'w') as f:
        # YOLO format: class_id x_center y_center width height (normalized 0-1)
        # Full image: center at (0.5, 0.5), size (1.0, 1.0)
        f.write(f"{class_id} 0.5 0.5 1.0 1.0\n")

def process_images(food_folders, source_path, dest_path, train_split):
    """Process all images and create labels"""
    
    total_train = 0
    total_val = 0
    class_distribution = []
    
    print("\nProcessing images...")
    print("-" * 80)
    
    for class_id, food_name in enumerate(food_folders):
        food_folder = os.path.join(source_path, food_name)
        
        # Get all images
        images = get_image_files(food_folder)
        
        if len(images) == 0:
            print(f"⚠ Warning: No images found in {food_name}")
            continue
        
        # Shuffle and split
        random.shuffle(images)
        split_idx = int(len(images) * train_split)
        train_images = images[:split_idx]
        val_images = images[split_idx:]
        
        # Process training images
        for idx, img_name in enumerate(train_images):
            try:
                # Create unique filename
                img_extension = os.path.splitext(img_name)[1]
                new_img_name = f"{food_name}_{idx:04d}{img_extension}"
                
                # Copy image
                src_img = os.path.join(food_folder, img_name)
                dst_img = os.path.join(dest_path, "images", "train", new_img_name)
                shutil.copy2(src_img, dst_img)
                
                # Create label
                label_name = f"{food_name}_{idx:04d}.txt"
                label_path = os.path.join(dest_path, "labels", "train", label_name)
                create_yolo_label(class_id, label_path)
                
            except Exception as e:
                print(f"  ✗ Error processing {img_name}: {e}")
                continue
        
        # Process validation images
        for idx, img_name in enumerate(val_images):
            try:
                img_extension = os.path.splitext(img_name)[1]
                new_img_name = f"{food_name}_val_{idx:04d}{img_extension}"
                
                src_img = os.path.join(food_folder, img_name)
                dst_img = os.path.join(dest_path, "images", "val", new_img_name)
                shutil.copy2(src_img, dst_img)
                
                label_name = f"{food_name}_val_{idx:04d}.txt"
                label_path = os.path.join(dest_path, "labels", "val", label_name)
                create_yolo_label(class_id, label_path)
                
            except Exception as e:
                print(f"  ✗ Error processing {img_name}: {e}")
                continue
        
        total_train += len(train_images)
        total_val += len(val_images)
        class_distribution.append({
            'class': food_name,
            'id': class_id,
            'train': len(train_images),
            'val': len(val_images),
            'total': len(images)
        })
        
        print(f"✓ {class_id+1:3d}. {food_name:30s} | Train: {len(train_images):4d} | Val: {len(val_images):4d} | Total: {len(images):4d}")
    
    print("-" * 80)
    print(f"\n✓ Dataset processed successfully!")
    print(f"  Total training images: {total_train}")
    print(f"  Total validation images: {total_val}")
    print(f"  Total classes: {len(class_distribution)}")
    
    return class_distribution

def create_yaml_file(food_folders, dest_path):
    """Create YAML configuration file for YOLO"""
    yaml_path = os.path.join(dest_path, "food_data.yaml")
    
    with open(yaml_path, 'w') as f:
        f.write(f"# Food Detection Dataset Configuration\n")
        f.write(f"path: {dest_path}\n")
        f.write(f"train: images/train\n")
        f.write(f"val: images/val\n")
        f.write(f"\n")
        f.write(f"# Number of classes\n")
        f.write(f"nc: {len(food_folders)}\n")
        f.write(f"\n")
        f.write(f"# Class names\n")
        f.write(f"names: {food_folders}\n")
    
    print(f"\n✓ YAML configuration file created: {yaml_path}")
    return yaml_path

def create_class_reference(class_distribution, dest_path):
    """Create a reference file with class IDs and names"""
    ref_path = os.path.join(dest_path, "class_reference.txt")
    
    with open(ref_path, 'w') as f:
        f.write("Class ID | Class Name              | Train | Val   | Total\n")
        f.write("-" * 70 + "\n")
        for item in class_distribution:
            f.write(f"{item['id']:8d} | {item['class']:23s} | {item['train']:5d} | {item['val']:5d} | {item['total']:5d}\n")
    
    print(f"✓ Class reference file created: {ref_path}")

def main():
    print("=" * 80)
    print("YOLO DATASET RESTRUCTURING SCRIPT")
    print("=" * 80)
    print(f"\nSource: {SOURCE_PATH}")
    print(f"Destination: {DESTINATION_PATH}")
    print(f"Train/Val Split: {TRAIN_SPLIT*100:.0f}% / {(1-TRAIN_SPLIT)*100:.0f}%")
    
    # Check if source path exists
    if not os.path.exists(SOURCE_PATH):
        print(f"\n✗ Error: Source path does not exist: {SOURCE_PATH}")
        return
    
    # Create destination directory structure
    create_directory_structure(DESTINATION_PATH)
    
    # Get food classes
    food_folders = get_food_classes(SOURCE_PATH)
    print(f"\n✓ Found {len(food_folders)} food classes")
    
    # Process images and create labels
    class_distribution = process_images(food_folders, SOURCE_PATH, DESTINATION_PATH, TRAIN_SPLIT)
    
    # Create YAML configuration file
    yaml_path = create_yaml_file(food_folders, DESTINATION_PATH)
    
    # Create class reference file
    create_class_reference(class_distribution, DESTINATION_PATH)
    
    print("\n" + "=" * 80)
    print("DATASET RESTRUCTURING COMPLETE!")
    print("=" * 80)
    print("\nNext steps:")
    print("1. Review the dataset structure in:", DESTINATION_PATH)
    print("2. Check the YAML file:", yaml_path)
    print("3. Train your model using the YAML file")
    print("\nExample training command:")
    print(f"   from ultralytics import YOLO")
    print(f"   model = YOLO('yolov8n.pt')")
    print(f"   model.train(data='{yaml_path}', epochs=100, imgsz=640)")

if __name__ == "__main__":
    main()