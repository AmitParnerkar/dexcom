package com.dexcom.ai.services
import com.dexcom.ai.model.GlucoseData
import com.dexcom.ai.model.PredictRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import com.fasterxml.jackson.databind.ObjectMapper

@Service
class LLMService(
    private val objectMapper: ObjectMapper // Injected via constructor
) {

    private val client = OkHttpClient()

    @Value("\${llm-service.url}")
    private lateinit var pythonLLMServiceUrl: String

    fun predict(request: PredictRequest): String? {
        try {
            // Convert entire request object to JSON
            val jsonPayload = objectMapper.writeValueAsString(request)
            val requestBody = jsonPayload.toRequestBody("application/json".toMediaType())

            // Make HTTP POST request to Python service
            val httpRequest = Request.Builder()
                .url("$pythonLLMServiceUrl/predict")
                .post(requestBody)
                .build()

            client.newCall(httpRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    throw RuntimeException("Unexpected response code: $response")
                }

                return response.body?.string()
            }
        } catch (e: Exception) {
            throw RuntimeException("Error generating predictions: ${e.message}", e)
        }
    }


    fun trainModel(data: Map<String, List<GlucoseData>>) {
        try {
            val jsonPayload = objectMapper.writeValueAsString(data)
            val requestBody = jsonPayload.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("$pythonLLMServiceUrl/train")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw RuntimeException("Unexpected code $response")
                }

                val responseBody = response.body?.string()
                val responseMap: Map<String, String> = objectMapper.readValue(responseBody!!)
                println("Train endpoint response: ${responseMap["message"]}")
            }
        } catch (e: Exception) {
            throw RuntimeException("Error training model: ${e.message}", e)
        }
    }
}



