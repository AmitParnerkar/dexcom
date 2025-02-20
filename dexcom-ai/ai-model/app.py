from flask import Flask, request, jsonify
import pandas as pd
from prophet import Prophet
import pickle
import os

app = Flask(__name__)
MODEL_FILE = "glucose_forecast.pkl"
DATA_FILE = "glucose_data.csv"  # Store historical data separately

# Mapping categorical values to numeric
EXERCISE_MAP = {"None": 0, "Light": 1, "Moderate": 2, "Heavy": 3}
SLEEP_MAP = {"Poor": 0, "Moderate": 1, "Good": 2}


@app.route("/")
def home():
    return "Glucose Prediction API is Running"


@app.route("/train", methods=["POST"])
def train():
    try:
        data = request.json
        glucose_data = data.get("glucose_data", [])

        if not glucose_data:
            return jsonify({"error": "No glucose data provided"}), 400

        # Convert JSON to DataFrame
        df_new = pd.DataFrame(glucose_data)
        df_new["timestamp"] = pd.to_datetime(df_new["timestamp"]).dt.tz_localize(None)

        df_new.rename(columns={"timestamp": "ds", "glucose_level": "y"}, inplace=True)

        # Feature Engineering
        df_new["meal_intake"] = df_new["meal_intake"].fillna(0)
        df_new["exercise_intensity"] = df_new["exercise_intensity"].map(EXERCISE_MAP).fillna(0)
        df_new["medication"] = df_new["medication"].fillna(0)
        df_new["sleep_quality"] = df_new["sleep_quality"].map(SLEEP_MAP).fillna(1)
        df_new["age"] = df_new["user_baseline"].apply(lambda x: x["age"] if x else 0)
        df_new["weight"] = df_new["user_baseline"].apply(lambda x: x["weight"] if x else 0)

        # Drop nested JSON data
        df_new.drop(columns=["user_baseline"], inplace=True)

        # Load existing data if available
        if os.path.exists(DATA_FILE):
            df_old = pd.read_csv(DATA_FILE)
            df_old["ds"] = pd.to_datetime(df_old["ds"])  # Ensure correct datetime format
            df = pd.concat([df_old, df_new]).drop_duplicates().sort_values("ds")
        else:
            df = df_new

        # Save updated dataset
        df.to_csv(DATA_FILE, index=False)

        # Train model
        model = Prophet()
        for feature in ["meal_intake", "exercise_intensity", "medication", "sleep_quality", "age", "weight"]:
            model.add_regressor(feature)

        model.fit(df)

        # Save trained model
        with open(MODEL_FILE, "wb") as f:
            pickle.dump(model, f)

        return jsonify({"message": "Model trained successfully with new data!"})

    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route("/predict", methods=["POST"])
def predict():
    try:

        if not os.path.exists(MODEL_FILE):
            return jsonify({"error": "Model not found. Train the model first!"}), 400

        data = request.json
        user_id = data.get("user_id", "unknown_user")

        # Extract user-specific input
        user_input = {
            "current_glucose": data["current_glucose"],
            "meal_intake": data["last_meal_intake"],
            "exercise_intensity": EXERCISE_MAP.get(
                data["recent_exercise_intensity"], 0
            ),
            "medication": data["medication_taken"],
            "sleep_quality": SLEEP_MAP.get(data["sleep_quality"], 1),
            "age": data["user_baseline"]["age"],
            "weight": data["user_baseline"]["weight"],
        }

        # Load trained model
        with open(MODEL_FILE, "rb") as f:
            model = pickle.load(f)

        # Create future dataframe
        future = model.make_future_dataframe(periods=12, freq="H")
        for key, value in user_input.items():
            future[key] = value

        forecast = model.predict(future)
        predictions = forecast[["ds", "yhat"]].tail(12).to_dict(orient="records")

        return jsonify({"user_id": user_id, "predictions": predictions})

    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5080)
