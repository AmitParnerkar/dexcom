package com.dexcom.ai.controller

import com.dexcom.ai.model.GlucoseData
import com.dexcom.ai.model.PredictRequest
import com.dexcom.ai.model.PredictionResponse
import com.dexcom.ai.services.LLMService
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired

@RestController

@RequestMapping("/api/dexcom-ai")
@Tag(name = "Glucose Predictor API", description = "Smart AI based predictor for Dexcom G7 data")
class PredictController @Autowired constructor(
    private val llmService: LLMService,
    private val objectMapper: ObjectMapper
) {

    @Operation(
        summary = "Predict Glucose level for user",
        description = "12-hour prediction of glucose levels",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Predictions with hourly data",
                content = [Content(schema = Schema(implementation = PredictionResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error predicting glucose levels",
                content = [Content(schema = Schema(implementation = PredictionResponse::class))]
            )
        ]
    )
    @PostMapping("/predict")
    fun predict(@RequestBody request: PredictRequest): ResponseEntity<PredictionResponse> {
        return try {
            val jsonResponse = llmService.predict(request)

            val response = jsonResponse?.let {
                val predictions = objectMapper.readValue(it, PredictionResponse::class.java)
                ResponseEntity.ok(predictions)
            } ?: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()

            response
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(PredictionResponse(error = "Error predicting glucose levels: ${e.message}"))
        }
    }

    @Operation(
        summary = "Train model with glucose data",
        description = "Fine-tune a locally hosted Prophet model with glucose & base line data",
        responses = [
            ApiResponse(description = "Model is trained with data", responseCode = "200"),
            ApiResponse(description = "Invalid input", responseCode = "400", content = [Content()])
        ]
    )
    @PostMapping("/train-model")
    fun trainModel(
        @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "List of glucose data",
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = GlucoseData::class))
                )
            ]
        )
        request: List<GlucoseData>
    ): ResponseEntity<Void> {
        return try {
            val requestBody = mapOf("glucose_data" to request)

            llmService.trainModel(requestBody)

            return ResponseEntity.ok().build()

        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
