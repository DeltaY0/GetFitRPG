from ultralytics import YOLO
from PIL import Image
import os
import glob
import cv2
import numpy as np

# ============= CONFIGURATION =============
MODEL_PATH = r"D:\college work\Mobile\Food detection\best.pt"  # Updated for new model
TEST_IMAGES_DIR = r"D:\college work\Mobile\Food detection\test_images"  # Put test images here
CONFIDENCE_THRESHOLD = 0.3  # Lower threshold since model predicts full images
SAVE_RESULTS = True  # Save annotated images
OUTPUT_DIR = r"D:\college work\Mobile\Food detection\test_results"
TOP_K_PREDICTIONS = 3  # Show top 3 predictions for each image

# Display settings
DISPLAY_WIDTH = 1200  # Maximum display width (resizes if larger)
DISPLAY_HEIGHT = 900  # Maximum display height (resizes if larger)
FONT_SIZE_MULTIPLIER = 1.0  # Adjust text size (1.0 = default)
# =========================================

def resize_image_for_display(img, max_width=DISPLAY_WIDTH, max_height=DISPLAY_HEIGHT):
    """Resize image to fit display while maintaining aspect ratio"""
    h, w = img.shape[:2]
    
    # Calculate scale to fit within max dimensions
    scale = min(max_width / w, max_height / h, 1.0)  # Don't upscale
    
    if scale < 1.0:
        new_w = int(w * scale)
        new_h = int(h * scale)
        img_resized = cv2.resize(img, (new_w, new_h), interpolation=cv2.INTER_AREA)
        return img_resized, scale
    
    return img, 1.0

def test_model():
    """Test the trained model on images"""
    
    print("\n" + "=" * 80)
    print("FOOD CLASSIFICATION MODEL TESTING")
    print("=" * 80)
    
    # Check if model exists
    if not os.path.exists(MODEL_PATH):
        print(f"\n❌ ERROR: Model not found at {MODEL_PATH}")
        print("\nPlease check:")
        print("  1. Training completed successfully")
        print("  2. Model path is correct")
        return
    
    print(f"✓ Model found: {MODEL_PATH}")
    
    # Load model
    print("\n📥 Loading model...")
    try:
        model = YOLO(MODEL_PATH)
        print("✓ Model loaded successfully!")
        print(f"✓ Classes: {len(model.names)}")
        print(f"✓ Model type: YOLOv8 (trained for full-image classification)")
    except Exception as e:
        print(f"❌ Failed to load model: {e}")
        return
    
    # Create output directory
    if SAVE_RESULTS:
        os.makedirs(OUTPUT_DIR, exist_ok=True)
        print(f"✓ Output directory: {OUTPUT_DIR}")
    
    print("=" * 80)
    
    # Get test images
    if os.path.exists(TEST_IMAGES_DIR):
        image_extensions = ['*.jpg', '*.jpeg', '*.png', '*.bmp']
        test_images = []
        for ext in image_extensions:
            test_images.extend(glob.glob(os.path.join(TEST_IMAGES_DIR, ext)))
        
        if not test_images:
            print(f"\n⚠️  No images found in {TEST_IMAGES_DIR}")
            print("\nPlease add test images to the directory!")
            return
        
        print(f"\n🔍 Found {len(test_images)} test images")
        print("-" * 80)
        
        # Process each image
        for idx, img_path in enumerate(test_images, 1):
            print(f"\n[{idx}/{len(test_images)}] Processing: {os.path.basename(img_path)}")
            
            try:
                # Run inference
                results = model.predict(
                    source=img_path,
                    conf=CONFIDENCE_THRESHOLD,
                    verbose=False
                )
                
                # Display results
                result = results[0]
                
                if len(result.boxes) == 0:
                    print("   ⚠️  No confident predictions (try lowering CONFIDENCE_THRESHOLD)")
                else:
                    # Get all predictions sorted by confidence
                    predictions = []
                    for box in result.boxes:
                        class_id = int(box.cls[0])
                        confidence = float(box.conf[0])
                        class_name = model.names[class_id]
                        predictions.append((class_name, confidence))
                    
                    # Sort by confidence (highest first)
                    predictions.sort(key=lambda x: x[1], reverse=True)
                    
                    # Show top predictions
                    print(f"   ✓ Top {min(TOP_K_PREDICTIONS, len(predictions))} prediction(s):")
                    for i, (food, conf) in enumerate(predictions[:TOP_K_PREDICTIONS], 1):
                        print(f"      {i}. {food}: {conf:.2%} confidence")
                    
                    # Save annotated image
                    if SAVE_RESULTS:
                        img = cv2.imread(img_path)
                        h, w = img.shape[:2]
                        
                        # Calculate adaptive font size based on image dimensions
                        base_font_scale = min(w, h) / 800 * FONT_SIZE_MULTIPLIER
                        
                        # Add top prediction as text
                        top_food, top_conf = predictions[0]
                        label = f"{top_food}: {top_conf:.1%}"
                        
                        # Add background rectangle for text
                        font = cv2.FONT_HERSHEY_SIMPLEX
                        font_scale = base_font_scale * 1.2
                        thickness = max(2, int(font_scale * 2))
                        padding = int(10 * base_font_scale)
                        
                        (text_w, text_h), _ = cv2.getTextSize(label, font, font_scale, thickness)
                        
                        # Larger, clearer background
                        cv2.rectangle(img, (padding, padding), 
                                    (padding + text_w + padding*2, padding + text_h + padding*2), 
                                    (0, 255, 0), -1)
                        # Add border
                        cv2.rectangle(img, (padding, padding), 
                                    (padding + text_w + padding*2, padding + text_h + padding*2), 
                                    (0, 200, 0), 3)
                        
                        cv2.putText(img, label, (padding*2, padding + text_h + padding//2), 
                                  font, font_scale, (0, 0, 0), thickness)
                        
                        # Add top 3 predictions in a cleaner box
                        y_offset = padding*3 + text_h + padding*3
                        box_height = (TOP_K_PREDICTIONS * int(40 * base_font_scale)) + padding*2
                        
                        # Background box for predictions
                        cv2.rectangle(img, (padding, y_offset), 
                                    (padding + int(400 * base_font_scale), y_offset + box_height),
                                    (50, 50, 50), -1)
                        cv2.rectangle(img, (padding, y_offset), 
                                    (padding + int(400 * base_font_scale), y_offset + box_height),
                                    (255, 255, 255), 2)
                        
                        y_text = y_offset + padding + int(25 * base_font_scale)
                        for i, (food, conf) in enumerate(predictions[:TOP_K_PREDICTIONS], 1):
                            text = f"{i}. {food}: {conf:.1%}"
                            cv2.putText(img, text, (padding*2, y_text), cv2.FONT_HERSHEY_SIMPLEX, 
                                      base_font_scale * 0.7, (255, 255, 255), max(1, int(base_font_scale * 2)))
                            y_text += int(35 * base_font_scale)
                        
                        # Save at high quality
                        output_path = os.path.join(OUTPUT_DIR, f"result_{os.path.basename(img_path)}")
                        cv2.imwrite(output_path, img, [cv2.IMWRITE_JPEG_QUALITY, 95])
                
            except Exception as e:
                print(f"   ❌ Error processing image: {e}")
        
        print("\n" + "=" * 80)
        if SAVE_RESULTS:
            print(f"✅ Results saved to: {OUTPUT_DIR}")
            print("   Check the annotated images with predictions!")
        print("=" * 80)
    
    else:
        print(f"\n⚠️  Test images directory not found: {TEST_IMAGES_DIR}")
        print("\nPlease create the directory and add test images:")
        print(f"  mkdir '{TEST_IMAGES_DIR}'")
        print(f"  # Then add .jpg, .png images to test")
        print("\nOr test on a single image (see test_single_image() below)")

def test_single_image(image_path):
    """Test model on a single image"""
    
    print("\n" + "=" * 80)
    print("TESTING SINGLE IMAGE")
    print("=" * 80)
    
    if not os.path.exists(MODEL_PATH):
        print(f"❌ Model not found: {MODEL_PATH}")
        return
    
    if not os.path.exists(image_path):
        print(f"❌ Image not found: {image_path}")
        return
    
    print(f"✓ Model: {MODEL_PATH}")
    print(f"✓ Image: {image_path}")
    
    # Load model
    model = YOLO(MODEL_PATH)
    
    # Run inference
    print("\n🔍 Running food classification...")
    results = model.predict(
        source=image_path,
        conf=CONFIDENCE_THRESHOLD,
        verbose=False
    )
    
    result = results[0]
    
    print("\n" + "-" * 80)
    print("RESULTS:")
    print("-" * 80)
    
    if len(result.boxes) == 0:
        print("❌ No confident predictions")
        print(f"   (Try lowering CONFIDENCE_THRESHOLD from {CONFIDENCE_THRESHOLD})")
    else:
        # Get all predictions sorted by confidence
        predictions = []
        for box in result.boxes:
            class_id = int(box.cls[0])
            confidence = float(box.conf[0])
            class_name = model.names[class_id]
            predictions.append((class_name, confidence))
        
        # Sort by confidence
        predictions.sort(key=lambda x: x[1], reverse=True)
        
        print(f"✓ Top {min(TOP_K_PREDICTIONS, len(predictions))} prediction(s):\n")
        
        for idx, (food, conf) in enumerate(predictions[:TOP_K_PREDICTIONS], 1):
            print(f"{idx}. {food}")
            print(f"   Confidence: {conf:.2%}\n")
    
    print("=" * 80)
    
    # Display the image with predictions
    print("\n📷 Displaying image with prediction...")
    print("   Press any key to close the window...")
    
    try:
        # Read and annotate image
        img = cv2.imread(image_path)
        h, w = img.shape[:2]
        
        # Calculate adaptive font size
        base_font_scale = min(w, h) / 600 * FONT_SIZE_MULTIPLIER
        
        if len(result.boxes) > 0:
            # Get predictions
            predictions = []
            for box in result.boxes:
                class_id = int(box.cls[0])
                confidence = float(box.conf[0])
                class_name = model.names[class_id]
                predictions.append((class_name, confidence))
            
            predictions.sort(key=lambda x: x[1], reverse=True)
            
            # Add top prediction as large text
            top_food, top_conf = predictions[0]
            label = f"{top_food}: {top_conf:.1%}"
            
            font = cv2.FONT_HERSHEY_SIMPLEX
            font_scale = base_font_scale * 1.5
            thickness = max(2, int(font_scale * 2))
            padding = int(15 * base_font_scale)
            
            # Add background rectangle
            (text_w, text_h), _ = cv2.getTextSize(label, font, font_scale, thickness)
            cv2.rectangle(img, (padding, padding), 
                        (padding + text_w + padding*2, padding + text_h + padding*2), 
                        (0, 255, 0), -1)
            cv2.rectangle(img, (padding, padding), 
                        (padding + text_w + padding*2, padding + text_h + padding*2), 
                        (0, 200, 0), 3)
            cv2.putText(img, label, (padding*2, padding + text_h + padding//2), 
                      font, font_scale, (0, 0, 0), thickness)
            
            # Add all top predictions in a cleaner box
            y_offset = padding*3 + text_h + padding*3
            box_height = (TOP_K_PREDICTIONS * int(40 * base_font_scale)) + padding*2
            
            cv2.rectangle(img, (padding, y_offset), 
                        (padding + int(500 * base_font_scale), y_offset + box_height),
                        (50, 50, 50), -1)
            cv2.rectangle(img, (padding, y_offset), 
                        (padding + int(500 * base_font_scale), y_offset + box_height),
                        (255, 255, 255), 2)
            
            y_text = y_offset + padding + int(25 * base_font_scale)
            for i, (food, conf) in enumerate(predictions[:TOP_K_PREDICTIONS], 1):
                text = f"{i}. {food}: {conf:.1%}"
                cv2.putText(img, text, (padding*2, y_text), cv2.FONT_HERSHEY_SIMPLEX, 
                          base_font_scale * 0.8, (255, 255, 255), max(2, int(base_font_scale * 2)))
                y_text += int(35 * base_font_scale)
        
        # Resize for display if too large
        display_img, scale = resize_image_for_display(img)
        
        # Display
        cv2.imshow('Food Classification Result', display_img)
        cv2.waitKey(0)
        cv2.destroyAllWindows()
        
        # Save result at original resolution
        if SAVE_RESULTS:
            os.makedirs(OUTPUT_DIR, exist_ok=True)
            output_path = os.path.join(OUTPUT_DIR, f"result_{os.path.basename(image_path)}")
            cv2.imwrite(output_path, img, [cv2.IMWRITE_JPEG_QUALITY, 95])
            print(f"\n✓ Result saved to: {output_path}")
            if scale < 1.0:
                print(f"   (Display was resized by {scale:.1%} to fit screen, saved file is full resolution)")
        
    except Exception as e:
        print(f"⚠️  Could not display image: {e}")

def test_webcam():
    """Test model with webcam (real-time classification)"""
    
    print("\n" + "=" * 80)
    print("WEBCAM TESTING (Real-time Food Classification)")
    print("=" * 80)
    print("\n⚠️  Press 'q' to quit\n")
    
    if not os.path.exists(MODEL_PATH):
        print(f"❌ Model not found: {MODEL_PATH}")
        return
    
    try:
        model = YOLO(MODEL_PATH)
        
        # Open webcam
        cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)  # Use DirectShow on Windows for better compatibility
        
        if not cap.isOpened():
            print("❌ Could not open webcam!")
            print("   Trying alternative method...")
            cap = cv2.VideoCapture(0)
            if not cap.isOpened():
                print("❌ Still could not open webcam!")
                return
        
        print("✓ Webcam opened")
        print("✓ Point camera at food items")
        print("✓ Press 'q' to quit\n")
        
        # Get frame size for adaptive text
        ret, test_frame = cap.read()
        if ret:
            h, w = test_frame.shape[:2]
            base_font_scale = min(w, h) / 600 * FONT_SIZE_MULTIPLIER
        else:
            base_font_scale = 1.0
        
        while True:
            ret, frame = cap.read()
            if not ret:
                break
            
            # Run inference
            results = model.predict(
                source=frame,
                conf=CONFIDENCE_THRESHOLD,
                verbose=False
            )
            
            result = results[0]
            
            # Annotate frame
            if len(result.boxes) > 0:
                # Get top predictions
                predictions = []
                for box in result.boxes:
                    class_id = int(box.cls[0])
                    confidence = float(box.conf[0])
                    class_name = model.names[class_id]
                    predictions.append((class_name, confidence))
                
                predictions.sort(key=lambda x: x[1], reverse=True)
                
                # Display top prediction with background
                top_food, top_conf = predictions[0]
                label = f"{top_food}: {top_conf:.1%}"
                
                font = cv2.FONT_HERSHEY_SIMPLEX
                font_scale = base_font_scale * 1.2
                thickness = max(2, int(font_scale * 2))
                padding = int(10 * base_font_scale)
                
                (text_w, text_h), _ = cv2.getTextSize(label, font, font_scale, thickness)
                cv2.rectangle(frame, (padding, padding), 
                            (padding + text_w + padding*2, padding + text_h + padding*2), 
                            (0, 255, 0), -1)
                cv2.rectangle(frame, (padding, padding), 
                            (padding + text_w + padding*2, padding + text_h + padding*2), 
                            (0, 200, 0), 3)
                cv2.putText(frame, label, (padding*2, padding + text_h + padding//2), 
                          font, font_scale, (0, 0, 0), thickness)
                
                # Display top 3 in a box
                y_offset = padding*3 + text_h + padding*3
                box_height = (TOP_K_PREDICTIONS * int(35 * base_font_scale)) + padding*2
                
                cv2.rectangle(frame, (padding, y_offset), 
                            (padding + int(400 * base_font_scale), y_offset + box_height),
                            (50, 50, 50), -1)
                cv2.rectangle(frame, (padding, y_offset), 
                            (padding + int(400 * base_font_scale), y_offset + box_height),
                            (255, 255, 255), 2)
                
                y_text = y_offset + padding + int(25 * base_font_scale)
                for i, (food, conf) in enumerate(predictions[:TOP_K_PREDICTIONS], 1):
                    text = f"{i}. {food}: {conf:.1%}"
                    cv2.putText(frame, text, (padding*2, y_text), cv2.FONT_HERSHEY_SIMPLEX, 
                              base_font_scale * 0.7, (255, 255, 255), max(1, int(base_font_scale * 2)))
                    y_text += int(30 * base_font_scale)
            else:
                cv2.putText(frame, "No food detected", (10, 30), 
                          cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 0, 255), 2)
            
            # Resize for display if needed
            display_frame, _ = resize_image_for_display(frame)
            
            # Show frame
            cv2.imshow('Food Classification - Press Q to quit', display_frame)
            
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break
        
        cap.release()
        cv2.destroyAllWindows()
        print("\n✓ Webcam closed")
            
    except KeyboardInterrupt:
        print("\n\n✓ Stopped by user")
    except Exception as e:
        print(f"\n❌ Error: {e}")
        print("\nMake sure you have a webcam connected!")

def show_model_info():
    """Display detailed model information"""
    
    print("\n" + "=" * 80)
    print("MODEL INFORMATION")
    print("=" * 80)
    
    if not os.path.exists(MODEL_PATH):
        print(f"❌ Model not found: {MODEL_PATH}")
        return
    
    model = YOLO(MODEL_PATH)
    
    print(f"\n📊 Model Details:")
    print(f"   Path: {MODEL_PATH}")
    print(f"   Task: {model.task}")
    print(f"   Number of classes: {len(model.names)}")
    
    print(f"\n🏷️  Food Classes ({len(model.names)} total):")
    print("-" * 80)
    
    # Show first 20 classes
    for i in range(min(20, len(model.names))):
        print(f"   {i:3d}: {model.names[i]}")
    
    if len(model.names) > 20:
        print(f"   ... and {len(model.names) - 20} more classes")
    
    print("\n" + "=" * 80)

def main():
    print("\n" + "=" * 80)
    print("🍕 FOOD CLASSIFICATION MODEL TESTER")
    print("=" * 80)
    print("\n📌 Note: This model classifies the ENTIRE image as one food type")
    print("   It's trained on full-image labels, not bounding boxes.")
    
    print("\nOptions:")
    print("  1. Test on folder of images")
    print("  2. Test on single image")
    print("  3. Test with webcam (real-time)")
    print("  4. Show model information")
    print("  5. Exit")
    
    while True:
        choice = input("\nSelect option (1-5): ").strip()
        
        if choice == '1':
            test_model()
            break
        elif choice == '2':
            img_path = input("Enter image path: ").strip()
            test_single_image(img_path)
            break
        elif choice == '3':
            test_webcam()
            break
        elif choice == '4':
            show_model_info()
            break
        elif choice == '5':
            print("\n👋 Goodbye!")
            break
        else:
            print("❌ Invalid option. Please select 1-5.")

if __name__ == "__main__":
    main()
