# dexcom-ai
Predict Glucose level for a user

## POST /api/dexcom-ai/predict

Input JSON document for a User:
```JSON
{
  "user_id": "user_12345",
  "current_glucose": 110,
  "last_meal_intake": 90,
  "recent_exercise_intensity": "moderate",
  "medication_taken": 2,
  "sleep_quality": "good",
  "user_baseline": {
    "age": 35,
    "weight": 96,
    "diabetes_type": "Type 1"
  }
}
```

Prediction JSON for the User:
```JSON
{
	"predictions": [
		{
			"ds": "Wed, 12 Feb 2025 13:00:00 GMT",
			"yhat": -350.27362747938093
		},
		{
			"ds": "Wed, 12 Feb 2025 14:00:00 GMT",
			"yhat": -418.485287479381
		},
		{
			"ds": "Wed, 12 Feb 2025 15:00:00 GMT",
			"yhat": -486.69694747938104
		},
		{
			"ds": "Wed, 12 Feb 2025 16:00:00 GMT",
			"yhat": -554.908607479381
		},
		{
			"ds": "Wed, 12 Feb 2025 17:00:00 GMT",
			"yhat": -623.1202674793809
		},
		{
			"ds": "Wed, 12 Feb 2025 18:00:00 GMT",
			"yhat": -691.3319274793809
		},
		{
			"ds": "Wed, 12 Feb 2025 19:00:00 GMT",
			"yhat": -759.5435874793809
		},
		{
			"ds": "Wed, 12 Feb 2025 20:00:00 GMT",
			"yhat": -827.755247479381
		},
		{
			"ds": "Wed, 12 Feb 2025 21:00:00 GMT",
			"yhat": -895.966907479381
		},
		{
			"ds": "Wed, 12 Feb 2025 22:00:00 GMT",
			"yhat": -964.1785674793811
		},
		{
			"ds": "Wed, 12 Feb 2025 23:00:00 GMT",
			"yhat": -1032.390227479381
		},
		{
			"ds": "Thu, 13 Feb 2025 00:00:00 GMT",
			"yhat": -1100.601887479381
		}
	],
	"user_id": "user_12345"
}
``` 
