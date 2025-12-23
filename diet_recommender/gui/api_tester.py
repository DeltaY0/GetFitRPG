"""
Simple GUI for testing Diet Recommender API
Built with tkinter - no additional dependencies required
"""
import tkinter as tk
from tkinter import ttk, scrolledtext, messagebox
import requests
import json
import threading
from typing import Optional


class DietRecommenderGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Diet Recommender API Tester")
        self.root.geometry("900x700")
        
        # API Configuration
        self.api_url = tk.StringVar(value="http://localhost:8000")
        
        # Create UI
        self.create_widgets()
        
    def create_widgets(self):
        """Create all GUI widgets"""
        # Main container with padding
        main_frame = ttk.Frame(self.root, padding="10")
        main_frame.grid(row=0, column=0, sticky=(tk.W, tk.E, tk.N, tk.S))
        
        # Configure grid weights
        self.root.columnconfigure(0, weight=1)
        self.root.rowconfigure(0, weight=1)
        main_frame.columnconfigure(0, weight=1)
        main_frame.rowconfigure(2, weight=1)
        
        # Title
        title = ttk.Label(main_frame, text="Diet Recommender API Tester", 
                         font=('Arial', 16, 'bold'))
        title.grid(row=0, column=0, pady=10)
        
        # API URL Configuration
        url_frame = ttk.LabelFrame(main_frame, text="API Configuration", padding="5")
        url_frame.grid(row=1, column=0, sticky=(tk.W, tk.E), pady=5)
        
        ttk.Label(url_frame, text="API URL:").grid(row=0, column=0, padx=5)
        ttk.Entry(url_frame, textvariable=self.api_url, width=40).grid(row=0, column=1, padx=5)
        ttk.Button(url_frame, text="Test Connection", 
                  command=self.test_connection).grid(row=0, column=2, padx=5)
        
        # Notebook for different tabs
        self.notebook = ttk.Notebook(main_frame)
        self.notebook.grid(row=2, column=0, sticky=(tk.W, tk.E, tk.N, tk.S), pady=5)
        
        # Create tabs
        self.create_quick_recommend_tab()
        self.create_full_recommend_tab()
        self.create_predict_tab()
        
        # Results area
        results_frame = ttk.LabelFrame(main_frame, text="Results", padding="5")
        results_frame.grid(row=3, column=0, sticky=(tk.W, tk.E, tk.N, tk.S), pady=5)
        results_frame.rowconfigure(0, weight=1)
        results_frame.columnconfigure(0, weight=1)
        
        self.results_text = scrolledtext.ScrolledText(results_frame, height=15, 
                                                      wrap=tk.WORD)
        self.results_text.grid(row=0, column=0, sticky=(tk.W, tk.E, tk.N, tk.S))
        
        # Status bar
        self.status_var = tk.StringVar(value="Ready")
        status_bar = ttk.Label(main_frame, textvariable=self.status_var, 
                              relief=tk.SUNKEN)
        status_bar.grid(row=4, column=0, sticky=(tk.W, tk.E), pady=5)
    
    def create_quick_recommend_tab(self):
        """Create quick recommendation tab"""
        frame = ttk.Frame(self.notebook, padding="10")
        self.notebook.add(frame, text="Quick Recommend")
        
        # Create scrollable frame
        canvas = tk.Canvas(frame)
        scrollbar = ttk.Scrollbar(frame, orient="vertical", command=canvas.yview)
        scrollable_frame = ttk.Frame(canvas)
        
        scrollable_frame.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )
        
        canvas.create_window((0, 0), window=scrollable_frame, anchor="nw")
        canvas.configure(yscrollcommand=scrollbar.set)
        
        # Input fields
        row = 0
        
        # Age
        ttk.Label(scrollable_frame, text="Age:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.quick_age = ttk.Spinbox(scrollable_frame, from_=1, to=120, width=10)
        self.quick_age.set(30)
        self.quick_age.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Weight
        ttk.Label(scrollable_frame, text="Weight (kg):").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.quick_weight = ttk.Spinbox(scrollable_frame, from_=30, to=200, width=10)
        self.quick_weight.set(70)
        self.quick_weight.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Height
        ttk.Label(scrollable_frame, text="Height (cm):").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.quick_height = ttk.Spinbox(scrollable_frame, from_=100, to=250, width=10)
        self.quick_height.set(170)
        self.quick_height.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Gender
        ttk.Label(scrollable_frame, text="Gender:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.quick_gender = ttk.Combobox(scrollable_frame, values=["Male", "Female", "Other"], 
                                        width=15, state="readonly")
        self.quick_gender.set("Male")
        self.quick_gender.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Activity Level
        ttk.Label(scrollable_frame, text="Activity Level:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.quick_activity = ttk.Combobox(scrollable_frame, 
                                          values=["Sedentary", "Moderate", "Active"],
                                          width=15, state="readonly")
        self.quick_activity.set("Moderate")
        self.quick_activity.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Dietary Restrictions
        ttk.Label(scrollable_frame, text="Dietary Restrictions:").grid(row=row, column=0, 
                                                            sticky=tk.W, pady=2)
        self.quick_restrictions = ttk.Entry(scrollable_frame, width=30)
        self.quick_restrictions.insert(0, "None")
        self.quick_restrictions.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Allergies
        ttk.Label(scrollable_frame, text="Allergies:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.quick_allergies = ttk.Entry(scrollable_frame, width=30)
        self.quick_allergies.insert(0, "None")
        self.quick_allergies.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Disliked Foods
        ttk.Label(scrollable_frame, text="Disliked Foods:").grid(row=row, column=0, sticky=tk.W, pady=2)
        ttk.Label(scrollable_frame, text="(e.g., eggs, meat, fish)", 
                 font=('Arial', 8, 'italic')).grid(row=row, column=2, sticky=tk.W, pady=2, padx=5)
        self.quick_dislikes = ttk.Entry(scrollable_frame, width=30)
        self.quick_dislikes.insert(0, "None")
        self.quick_dislikes.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Days
        ttk.Label(scrollable_frame, text="Days:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.quick_days = ttk.Spinbox(scrollable_frame, from_=1, to=30, width=10)
        self.quick_days.set(7)
        self.quick_days.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Submit button
        ttk.Button(scrollable_frame, text="Get Recommendation", 
                  command=self.quick_recommend).grid(row=row, column=0, columnspan=2, 
                                                     pady=10)
        
        # Pack canvas and scrollbar
        canvas.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")
    
    def create_full_recommend_tab(self):
        """Create full recommendation tab"""
        frame = ttk.Frame(self.notebook, padding="10")
        self.notebook.add(frame, text="Full Recommend")
        
        # Create scrollable frame
        canvas = tk.Canvas(frame)
        scrollbar = ttk.Scrollbar(frame, orient="vertical", command=canvas.yview)
        scrollable_frame = ttk.Frame(canvas)
        
        scrollable_frame.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )
        
        canvas.create_window((0, 0), window=scrollable_frame, anchor="nw")
        canvas.configure(yscrollcommand=scrollbar.set)
        
        row = 0
        
        # Basic Info
        ttk.Label(scrollable_frame, text="Basic Information", 
                 font=('Arial', 10, 'bold')).grid(row=row, column=0, columnspan=2, 
                                                 sticky=tk.W, pady=5)
        row += 1
        
        # Age
        ttk.Label(scrollable_frame, text="Age:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.full_age = ttk.Spinbox(scrollable_frame, from_=1, to=120, width=10)
        self.full_age.set(30)
        self.full_age.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Weight
        ttk.Label(scrollable_frame, text="Weight (kg):").grid(row=row, column=0, 
                                                             sticky=tk.W, pady=2)
        self.full_weight = ttk.Spinbox(scrollable_frame, from_=30, to=200, width=10)
        self.full_weight.set(75)
        self.full_weight.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Height
        ttk.Label(scrollable_frame, text="Height (cm):").grid(row=row, column=0, 
                                                             sticky=tk.W, pady=2)
        self.full_height = ttk.Spinbox(scrollable_frame, from_=100, to=250, width=10)
        self.full_height.set(175)
        self.full_height.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Gender
        ttk.Label(scrollable_frame, text="Gender:").grid(row=row, column=0, 
                                                        sticky=tk.W, pady=2)
        self.full_gender = ttk.Combobox(scrollable_frame, 
                                       values=["Male", "Female", "Other"],
                                       width=15, state="readonly")
        self.full_gender.set("Male")
        self.full_gender.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Health Info
        ttk.Label(scrollable_frame, text="Health Information", 
                 font=('Arial', 10, 'bold')).grid(row=row, column=0, columnspan=2,
                                                 sticky=tk.W, pady=5)
        row += 1
        
        # Disease Type
        ttk.Label(scrollable_frame, text="Disease Type:").grid(row=row, column=0,
                                                              sticky=tk.W, pady=2)
        self.full_disease = ttk.Entry(scrollable_frame, width=30)
        self.full_disease.insert(0, "None")
        self.full_disease.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Activity Level
        ttk.Label(scrollable_frame, text="Activity Level:").grid(row=row, column=0,
                                                                sticky=tk.W, pady=2)
        self.full_activity = ttk.Combobox(scrollable_frame,
                                         values=["Sedentary", "Moderate", "Active"],
                                         width=15, state="readonly")
        self.full_activity.set("Moderate")
        self.full_activity.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Exercise Hours
        ttk.Label(scrollable_frame, text="Exercise Hours/Week:").grid(row=row, column=0,
                                                                     sticky=tk.W, pady=2)
        self.full_exercise = ttk.Spinbox(scrollable_frame, from_=0, to=50, width=10)
        self.full_exercise.set(3)
        self.full_exercise.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Diet Adherence
        ttk.Label(scrollable_frame, text="Diet Adherence (%):").grid(row=row, column=0,
                                                                    sticky=tk.W, pady=2)
        self.full_adherence = ttk.Spinbox(scrollable_frame, from_=0, to=100, width=10)
        self.full_adherence.set(75)
        self.full_adherence.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Nutrient Imbalance
        ttk.Label(scrollable_frame, text="Nutrient Imbalance:").grid(row=row, column=0,
                                                                    sticky=tk.W, pady=2)
        self.full_imbalance = ttk.Spinbox(scrollable_frame, from_=0, to=100, width=10)
        self.full_imbalance.set(10)
        self.full_imbalance.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Dietary Info
        ttk.Label(scrollable_frame, text="Dietary Preferences",
                 font=('Arial', 10, 'bold')).grid(row=row, column=0, columnspan=2,
                                                 sticky=tk.W, pady=5)
        row += 1
        
        # Restrictions
        ttk.Label(scrollable_frame, text="Restrictions:").grid(row=row, column=0,
                                                              sticky=tk.W, pady=2)
        self.full_restrictions = ttk.Entry(scrollable_frame, width=30)
        self.full_restrictions.insert(0, "None")
        self.full_restrictions.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Allergies
        ttk.Label(scrollable_frame, text="Allergies:").grid(row=row, column=0,
                                                           sticky=tk.W, pady=2)
        self.full_allergies = ttk.Entry(scrollable_frame, width=30)
        self.full_allergies.insert(0, "None")
        self.full_allergies.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Disliked Foods
        ttk.Label(scrollable_frame, text="Disliked Foods:").grid(row=row, column=0,
                                                                 sticky=tk.W, pady=2)
        ttk.Label(scrollable_frame, text="(comma-separated)",
                 font=('Arial', 8, 'italic')).grid(row=row, column=2, sticky=tk.W, pady=2)
        self.full_dislikes = ttk.Entry(scrollable_frame, width=30)
        self.full_dislikes.insert(0, "None")
        self.full_dislikes.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Plan Settings
        ttk.Label(scrollable_frame, text="Plan Settings",
                 font=('Arial', 10, 'bold')).grid(row=row, column=0, columnspan=2,
                                                 sticky=tk.W, pady=5)
        row += 1
        
        # Days
        ttk.Label(scrollable_frame, text="Days:").grid(row=row, column=0,
                                                      sticky=tk.W, pady=2)
        self.full_days = ttk.Spinbox(scrollable_frame, from_=1, to=30, width=10)
        self.full_days.set(7)
        self.full_days.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Meals per day
        ttk.Label(scrollable_frame, text="Meals per Day:").grid(row=row, column=0,
                                                               sticky=tk.W, pady=2)
        self.full_meals = ttk.Spinbox(scrollable_frame, from_=1, to=6, width=10)
        self.full_meals.set(3)
        self.full_meals.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # No repeat days
        ttk.Label(scrollable_frame, text="No Repeat Days:").grid(row=row, column=0,
                                                                sticky=tk.W, pady=2)
        self.full_no_repeat = ttk.Spinbox(scrollable_frame, from_=0, to=14, width=10)
        self.full_no_repeat.set(3)
        self.full_no_repeat.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        # Submit button
        ttk.Button(scrollable_frame, text="Get Full Recommendation",
                  command=self.full_recommend).grid(row=row, column=0, columnspan=2,
                                                    pady=10)
        
        canvas.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")
    
    def create_predict_tab(self):
        """Create diet type prediction tab"""
        frame = ttk.Frame(self.notebook, padding="10")
        self.notebook.add(frame, text="Predict Diet Type")
        
        row = 0
        
        ttk.Label(frame, text="Enter patient details to predict diet type",
                 font=('Arial', 10, 'italic')).grid(row=row, column=0, columnspan=2,
                                                    pady=10)
        row += 1
        
        # Basic fields
        ttk.Label(frame, text="Age:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.pred_age = ttk.Spinbox(frame, from_=1, to=120, width=10)
        self.pred_age.set(35)
        self.pred_age.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        ttk.Label(frame, text="Weight (kg):").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.pred_weight = ttk.Spinbox(frame, from_=30, to=200, width=10)
        self.pred_weight.set(80)
        self.pred_weight.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        ttk.Label(frame, text="Height (cm):").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.pred_height = ttk.Spinbox(frame, from_=100, to=250, width=10)
        self.pred_height.set(175)
        self.pred_height.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        ttk.Label(frame, text="Gender:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.pred_gender = ttk.Combobox(frame, values=["Male", "Female", "Other"],
                                       width=15, state="readonly")
        self.pred_gender.set("Male")
        self.pred_gender.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        ttk.Label(frame, text="Activity Level:").grid(row=row, column=0, sticky=tk.W, pady=2)
        self.pred_activity = ttk.Combobox(frame,
                                         values=["Sedentary", "Moderate", "Active"],
                                         width=15, state="readonly")
        self.pred_activity.set("Moderate")
        self.pred_activity.grid(row=row, column=1, sticky=tk.W, pady=2)
        row += 1
        
        ttk.Button(frame, text="Predict Diet Type",
                  command=self.predict_diet).grid(row=row, column=0, columnspan=2,
                                                  pady=10)
    
    def test_connection(self):
        """Test API connection"""
        self.status_var.set("Testing connection...")
        
        def _test():
            try:
                response = requests.get(f"{self.api_url.get()}/health", timeout=5)
                if response.status_code == 200:
                    data = response.json()
                    self.display_result({
                        "status": "Connection successful!",
                        "model_loaded": data.get("model_loaded"),
                        "feature_count": data.get("feature_count"),
                        "diet_types": data.get("diet_types")
                    })
                    self.status_var.set("Connected")
                else:
                    messagebox.showerror("Connection Error",
                                       f"API returned status {response.status_code}")
                    self.status_var.set("Connection failed")
            except Exception as e:
                messagebox.showerror("Connection Error", str(e))
                self.status_var.set("Connection failed")
        
        threading.Thread(target=_test, daemon=True).start()
    
    def quick_recommend(self):
        """Get quick recommendation"""
        self.status_var.set("Getting recommendation...")
        
        def _recommend():
            try:
                # Combine allergies and disliked foods
                allergies = self.quick_allergies.get()
                dislikes = self.quick_dislikes.get()
                combined_allergies = self._combine_restrictions(allergies, dislikes)
                
                data = {
                    "age": int(self.quick_age.get()),
                    "weight_kg": float(self.quick_weight.get()),
                    "height_cm": float(self.quick_height.get()),
                    "gender": self.quick_gender.get(),
                    "activity_level": self.quick_activity.get(),
                    "dietary_restrictions": self.quick_restrictions.get(),
                    "allergies": combined_allergies,
                    "days": int(self.quick_days.get())
                }
                
                response = requests.post(
                    f"{self.api_url.get()}/api/v1/quick-recommend",
                    json=data,
                    timeout=30
                )
                
                if response.status_code == 200:
                    result = response.json()
                    self.display_recommendation(result)
                    self.status_var.set("Recommendation received")
                else:
                    messagebox.showerror("Error",
                                       f"API error: {response.status_code}\n{response.text}")
                    self.status_var.set("Request failed")
            except Exception as e:
                messagebox.showerror("Error", str(e))
                self.status_var.set("Request failed")
        
        threading.Thread(target=_recommend, daemon=True).start()
    
    def full_recommend(self):
        """Get full recommendation"""
        self.status_var.set("Getting full recommendation...")
        
        def _recommend():
            try:
                # Combine allergies and disliked foods
                allergies = self.full_allergies.get()
                dislikes = self.full_dislikes.get()
                combined_allergies = self._combine_restrictions(allergies, dislikes)
                
                data = {
                    "patient": {
                        "Age": int(self.full_age.get()),
                        "Weight_kg": float(self.full_weight.get()),
                        "Height_cm": float(self.full_height.get()),
                        "Gender": self.full_gender.get(),
                        "Disease_Type": self.full_disease.get(),
                        "Physical_Activity_Level": self.full_activity.get(),
                        "Weekly_Exercise_Hours": float(self.full_exercise.get()),
                        "Adherence_to_Diet_Plan": float(self.full_adherence.get()),
                        "Dietary_Nutrient_Imbalance_Score": float(self.full_imbalance.get()),
                        "Dietary_Restrictions": self.full_restrictions.get(),
                        "Allergies": combined_allergies
                    },
                    "days": int(self.full_days.get()),
                    "meals_per_day": int(self.full_meals.get()),
                    "no_repeat_days": int(self.full_no_repeat.get())
                }
                
                response = requests.post(
                    f"{self.api_url.get()}/api/v1/recommend",
                    json=data,
                    timeout=60
                )
                
                if response.status_code == 200:
                    result = response.json()
                    self.display_recommendation(result)
                    self.status_var.set("Recommendation received")
                else:
                    messagebox.showerror("Error",
                                       f"API error: {response.status_code}\n{response.text}")
                    self.status_var.set("Request failed")
            except Exception as e:
                messagebox.showerror("Error", str(e))
                self.status_var.set("Request failed")
        
        threading.Thread(target=_recommend, daemon=True).start()
    
    def predict_diet(self):
        """Predict diet type"""
        self.status_var.set("Predicting diet type...")
        
        def _predict():
            try:
                # Calculate BMI
                height_m = float(self.pred_height.get()) / 100
                bmi = float(self.pred_weight.get()) / (height_m ** 2)
                
                data = {
                    "Age": int(self.pred_age.get()),
                    "Weight_kg": float(self.pred_weight.get()),
                    "Height_cm": float(self.pred_height.get()),
                    "Gender": self.pred_gender.get(),
                    "Physical_Activity_Level": self.pred_activity.get(),
                    "Disease_Type": "None",
                    "Weekly_Exercise_Hours": 3.0,
                    "Adherence_to_Diet_Plan": 75.0,
                    "Dietary_Nutrient_Imbalance_Score": 10.0,
                    "Dietary_Restrictions": "None",
                    "Allergies": "None"
                }
                
                response = requests.post(
                    f"{self.api_url.get()}/api/v1/predict-diet-type",
                    json=data,
                    timeout=10
                )
                
                if response.status_code == 200:
                    result = response.json()
                    self.display_result(result)
                    self.status_var.set("Prediction complete")
                else:
                    messagebox.showerror("Error",
                                       f"API error: {response.status_code}\n{response.text}")
                    self.status_var.set("Request failed")
            except Exception as e:
                messagebox.showerror("Error", str(e))
                self.status_var.set("Request failed")
        
        threading.Thread(target=_predict, daemon=True).start()
    
    def _combine_restrictions(self, allergies: str, dislikes: str) -> str:
        """Combine allergies and disliked foods into one string"""
        items = []
        
        # Add allergies
        if allergies and allergies.lower() not in ['none', '']:
            items.extend([x.strip() for x in allergies.split(',') if x.strip()])
        
        # Add disliked foods
        if dislikes and dislikes.lower() not in ['none', '']:
            items.extend([x.strip() for x in dislikes.split(',') if x.strip()])
        
        # Return combined or 'None'
        return ', '.join(items) if items else 'None'
    
    def display_result(self, data):
        """Display JSON result"""
        self.results_text.delete('1.0', tk.END)
        self.results_text.insert('1.0', json.dumps(data, indent=2))
    
    def display_recommendation(self, data):
        """Display formatted recommendation"""
        self.results_text.delete('1.0', tk.END)
        
        output = []
        output.append("=" * 80)
        output.append("DIET RECOMMENDATION RESULT")
        output.append("=" * 80)
        output.append(f"\nPatient ID: {data.get('patient_id')}")
        output.append(f"Target Calories: {data.get('target_calories'):.0f} kcal/day")
        output.append(f"Dietary Restrictions: {data.get('dietary_restrictions')}")
        output.append(f"Allergies: {data.get('allergies')}")
        output.append(f"Total Days: {data.get('total_days')}")
        output.append("\n" + "-" * 80)
        
        for day_plan in data.get('meal_plan', []):
            output.append(f"\nDAY {day_plan['day']} - Total: {day_plan['total_calories']:.0f} kcal")
            output.append("-" * 40)
            for i, meal in enumerate(day_plan['meals'], 1):
                output.append(f"\n  Meal {i}:")
                output.append(f"    Recipe: {meal['recipe_name']}")
                output.append(f"    Cuisine: {meal['cuisine_type']}")
                output.append(f"    Calories: {meal['calories']:.0f} kcal")
                if meal.get('protein'):
                    output.append(f"    Protein: {meal['protein']:.1f}g")
                if meal.get('carbs'):
                    output.append(f"    Carbs: {meal['carbs']:.1f}g")
                if meal.get('fat'):
                    output.append(f"    Fat: {meal['fat']:.1f}g")
        
        output.append("\n" + "=" * 80)
        
        self.results_text.insert('1.0', '\n'.join(output))


def main():
    root = tk.Tk()
    app = DietRecommenderGUI(root)
    root.mainloop()


if __name__ == "__main__":
    main()
