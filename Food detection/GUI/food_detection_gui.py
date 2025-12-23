"""
Simple GUI app to test food detection
Upload an image and see the results from the API
"""
import tkinter as tk
from tkinter import filedialog, messagebox
from PIL import Image, ImageTk
import requests
import os

class FoodDetectionApp:
    def __init__(self, root):
        self.root = root
        self.root.title("🍕 Food Detection App")
        self.root.geometry("800x700")
        self.root.configure(bg='#f0f0f0')
        
        # API URL
        self.api_url = "http://localhost:5000/api/detect"
        self.api_health_url = "http://localhost:5000/api/health"
        
        # Title
        title = tk.Label(
            root, 
            text="🍕 Food Detection App", 
            font=('Arial', 24, 'bold'),
            bg='#f0f0f0',
            fg='#333'
        )
        title.pack(pady=20)
        
        # Image display area
        self.image_frame = tk.Frame(root, bg='white', width=600, height=400, relief=tk.SUNKEN, borderwidth=2)
        self.image_frame.pack(pady=10)
        self.image_frame.pack_propagate(False)
        
        self.image_label = tk.Label(self.image_frame, text="No image loaded", bg='white', fg='gray')
        self.image_label.pack(expand=True)
        
        # Buttons frame
        button_frame = tk.Frame(root, bg='#f0f0f0')
        button_frame.pack(pady=10)
        
        # Select Image button
        self.select_btn = tk.Button(
            button_frame,
            text="📁 Select Image",
            font=('Arial', 14, 'bold'),
            bg='#4CAF50',
            fg='white',
            padx=20,
            pady=10,
            command=self.select_image,
            cursor='hand2'
        )
        self.select_btn.pack(side=tk.LEFT, padx=10)
        
        # Detect button
        self.detect_btn = tk.Button(
            button_frame,
            text="🔍 Detect Food",
            font=('Arial', 14, 'bold'),
            bg='#2196F3',
            fg='white',
            padx=20,
            pady=10,
            command=self.detect_food,
            state=tk.DISABLED,
            cursor='hand2'
        )
        self.detect_btn.pack(side=tk.LEFT, padx=10)
        
        # Results frame
        results_frame = tk.Frame(root, bg='#f0f0f0')
        results_frame.pack(pady=20, fill=tk.BOTH, expand=True, padx=20)
        
        tk.Label(
            results_frame,
            text="Results:",
            font=('Arial', 16, 'bold'),
            bg='#f0f0f0',
            fg='#333'
        ).pack(anchor=tk.W)
        
        # Results text area
        self.results_text = tk.Text(
            results_frame,
            height=8,
            font=('Arial', 12),
            bg='white',
            fg='#333',
            relief=tk.SUNKEN,
            borderwidth=2,
            padx=10,
            pady=10,
            wrap=tk.WORD
        )
        self.results_text.pack(fill=tk.BOTH, expand=True, pady=5)
        
        # Add scrollbar
        scrollbar = tk.Scrollbar(results_frame, command=self.results_text.yview)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        self.results_text.config(yscrollcommand=scrollbar.set)
        
        # Status bar
        self.status_label = tk.Label(
            root,
            text="Ready. Please select an image.",
            font=('Arial', 10),
            bg='#333',
            fg='white',
            anchor=tk.W,
            padx=10,
            pady=5
        )
        self.status_label.pack(side=tk.BOTTOM, fill=tk.X)
        
        self.current_image_path = None
        
        # Check API connection on startup
        self.root.after(500, self.check_api_connection)
    
    def check_api_connection(self):
        """Check if API server is running"""
        try:
            response = requests.get(self.api_health_url, timeout=2)
            if response.status_code == 200:
                data = response.json()
                self.status_label.config(
                    text=f"✅ Connected to API - {data['num_classes']} food classes available",
                    bg='#4CAF50'
                )
            else:
                self.show_api_error()
        except:
            self.show_api_error()
    
    def show_api_error(self):
        """Show API connection error"""
        self.status_label.config(
            text="❌ API Server not running! Please start: python food_detection_api.py",
            bg='#f44336'
        )
        messagebox.showwarning(
            "API Server Not Running",
            "Cannot connect to the API server!\n\n"
            "Please start the API server first:\n\n"
            "1. Open a new terminal/PowerShell window\n"
            "2. Navigate to the project folder\n"
            "3. Run: python food_detection_api.py\n\n"
            "Or simply double-click START.bat to start everything!"
        )
        
    def select_image(self):
        """Open file dialog to select an image"""
        file_path = filedialog.askopenfilename(
            title="Select a food image",
            filetypes=[
                ("Image files", "*.jpg *.jpeg *.png *.bmp *.gif"),
                ("All files", "*.*")
            ]
        )
        
        if file_path:
            self.current_image_path = file_path
            self.display_image(file_path)
            self.detect_btn.config(state=tk.NORMAL)
            self.status_label.config(text=f"Image loaded: {os.path.basename(file_path)}")
            self.results_text.delete(1.0, tk.END)
            self.results_text.insert(1.0, "Image loaded. Click 'Detect Food' to analyze.")
    
    def display_image(self, image_path):
        """Display the selected image"""
        try:
            # Load and resize image
            img = Image.open(image_path)
            
            # Resize to fit in frame
            img.thumbnail((580, 380), Image.Resampling.LANCZOS)
            
            # Convert to PhotoImage
            photo = ImageTk.PhotoImage(img)
            
            # Update label
            self.image_label.config(image=photo, text="")
            self.image_label.image = photo  # Keep a reference
            
        except Exception as e:
            messagebox.showerror("Error", f"Failed to load image: {e}")
    
    def detect_food(self):
        """Send image to API and get results"""
        if not self.current_image_path:
            messagebox.showwarning("No Image", "Please select an image first!")
            return
        
        # Update status
        self.status_label.config(text="Detecting food... Please wait.")
        self.detect_btn.config(state=tk.DISABLED)
        self.root.update()
        
        try:
            # Send image to API
            with open(self.current_image_path, 'rb') as f:
                files = {'image': f}
                response = requests.post(self.api_url, files=files, timeout=30)
            
            if response.status_code == 200:
                data = response.json()
                self.display_results(data)
                self.status_label.config(text="✅ Detection complete!")
            else:
                error_msg = f"API Error {response.status_code}: {response.text}"
                self.results_text.delete(1.0, tk.END)
                self.results_text.insert(1.0, f"❌ Error:\n{error_msg}")
                self.status_label.config(text="❌ Detection failed")
                
        except requests.exceptions.ConnectionError:
            self.status_label.config(text="❌ Server not running", bg='#f44336')
            messagebox.showerror(
                "Connection Error",
                "Cannot connect to API server!\n\n"
                "The API server is not running or not accessible.\n\n"
                "To start the API server:\n"
                "• Double-click START.bat\n"
                "• OR open terminal and run: python food_detection_api.py\n\n"
                "Make sure you see:\n"
                "✓ Model loaded successfully\n"
                "✓ Running on http://127.0.0.1:5000"
            )
            self.results_text.config(state=tk.NORMAL)
            self.results_text.delete(1.0, tk.END)
            self.results_text.insert(1.0, 
                "❌ CONNECTION ERROR\n\n"
                "The API server is not running!\n\n"
                "Steps to fix:\n"
                "1. Open a new terminal window\n"
                "2. Run: python food_detection_api.py\n"
                "3. Wait for 'Model loaded successfully'\n"
                "4. Then try detecting again\n\n"
                "OR just double-click START.bat!"
            )
            self.results_text.config(state=tk.DISABLED)
        except Exception as e:
            messagebox.showerror("Error", f"Failed to detect food: {e}")
            self.status_label.config(text="❌ Error occurred")
        finally:
            self.detect_btn.config(state=tk.NORMAL)
    
    def display_results(self, data):
        """Display detection results"""
        self.results_text.config(state=tk.NORMAL)
        self.results_text.delete(1.0, tk.END)
        
        if data['success']:
            # Top prediction
            top = data['top_prediction']
            result_text = f"🍽️  DETECTED FOOD\n"
            result_text += f"{'='*60}\n\n"
            result_text += f"🥇 Top Result: {top['food_name'].replace('_', ' ').title()}\n"
            result_text += f"   Confidence: {top['confidence']:.2f}%\n"
            result_text += f"\n{'='*60}\n"
            result_text += f"📊 All Predictions:\n\n"
            
            # All predictions
            for i, pred in enumerate(data['predictions'], 1):
                food = pred['food_name'].replace('_', ' ').title()
                conf = pred['confidence']
                
                # Progress bar
                bar_length = int(conf / 2)  # Scale to 50 chars max
                bar = '█' * bar_length + '░' * (50 - bar_length)
                
                result_text += f"{i}. {food}\n"
                result_text += f"   {bar} {conf:.2f}%\n\n"
            
            self.results_text.insert(1.0, result_text)
            
            # Configure tags for better visibility
            self.results_text.tag_add("title", "1.0", "2.0")
            self.results_text.tag_config("title", font=('Arial', 14, 'bold'), foreground="#4CAF50")
            
            self.results_text.tag_add("top", "4.0", "5.0")
            self.results_text.tag_config("top", font=('Arial', 13, 'bold'), foreground="#2196F3")
            
        else:
            result_text = f"❌ Detection Failed\n\n"
            result_text += f"{data['message']}\n\n"
            result_text += f"Try:\n"
            result_text += f"• Use a different image\n"
            result_text += f"• Make sure the food is clearly visible\n"
            result_text += f"• Ensure good lighting in the image\n"
            self.results_text.insert(1.0, result_text)
            
            self.results_text.tag_add("error", "1.0", "1.end")
            self.results_text.tag_config("error", font=('Arial', 14, 'bold'), foreground="#f44336")
        
        self.results_text.config(state=tk.DISABLED)

def main():
    root = tk.Tk()
    app = FoodDetectionApp(root)
    root.mainloop()

if __name__ == "__main__":
    main()
