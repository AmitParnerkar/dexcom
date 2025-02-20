package com.dexcom.ai.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class PredictRequest(
    @Schema(
        description = "Unique user ID",
        example = "user_12345"
    )
    @JsonProperty("user_id") val userId: String,

    @Schema(
        description = "Current glucose level (mg/dL)",
        example = "110"
    )
    @JsonProperty("current_glucose") val currentGlucose: Int,

    @Schema(
        description = "Time since last meal intake (minutes)",
        example = "90"
    )
    @JsonProperty("last_meal_intake") val lastMealIntake: Int,

    @Schema(
        description = "Recent exercise intensity",
        example = "moderate"
    )
    @JsonProperty("recent_exercise_intensity") val recentExerciseIntensity: String,

    @Schema(
        description = "Medication taken in the last few hours (mg)",
        example = "2"
    )
    @JsonProperty("medication_taken") val medicationTaken: Int,

    @Schema(
        description = "Quality of sleep (poor, average, good)",
        example = "good"
    )
    @JsonProperty("sleep_quality") val sleepQuality: String,

    @Schema(
        description = "User's baseline glucose parameters",
        implementation = UserBaseline::class
    )
    @JsonProperty("user_baseline") val userBaseline: UserBaseline
)

data class PredictionResponse(
    @Schema(
        description = "List of predicted glucose levels for the next 12 hours",
        example = "[{\"ds\": \"2025-02-13T10:00:00Z\", \"yhat\": 120.5}]"
    )
    @JsonProperty("predictions") val predictions: List<Prediction>? = null,

    @Schema(
        description = "User ID associated with the prediction",
        example = "user_12345"
    )
    @JsonProperty("user_id") val userId: String? = null,

    @Schema(
        description = "Error message in case of failure",
        example = "Invalid input data"
    )
    @JsonProperty("error") val error: String? = null
)

data class Prediction(
    @Schema(
        description = "Timestamp of the prediction",
        example = "2025-02-13T10:00:00Z"
    )
    @JsonProperty("ds") val timestamp: String,

    @Schema(
        description = "Predicted glucose level (mg/dL)",
        example = "120.5"
    )
    @JsonProperty("yhat") val predictedGlucose: Double
)

data class GlucoseData @JsonCreator constructor(
    @Schema(description = "Timestamp of glucose measurement in ISO format", example = "2025-02-16T23:00:00Z")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    val timestamp: String,

    @Schema(description = "Glucose level in mg/dL", example = "159")
    val glucose_level: Int,

    @Schema(description = "Meal intake in grams", example = "67")
    val meal_intake: Int,

    @Schema(description = "Exercise intensity level", example = "Intense")
    val exercise_intensity: String,

    @Schema(description = "Medication taken (units)", example = "10")
    val medication: Int,

    @Schema(description = "Sleep quality category", example = "Poor")
    val sleep_quality: String,

    @Schema(description = "User baseline information")
    val user_baseline: UserBaseline
)

data class UserBaseline @JsonCreator constructor(
    @Schema(description = "User's age", example = "35")
    val age: Int,

    @Schema(description = "User's weight in kg", example = "96")
    val weight: Int,

    @Schema(description = "Diabetes type", example = "Type 1")
    val diabetes_type: String
)

data class ContactForm(
    val name: String,
    val email: String,
    val message: String
)


