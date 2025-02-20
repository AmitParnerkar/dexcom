package com.dexcom.ai.services

import com.dexcom.ai.model.GlucoseData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service

@Service
class StartupDataLoader(
    private val resourceLoader: ResourceLoader,
    private val llmService: LLMService,
    private val objectMapper: ObjectMapper,
    private val logger: Logger = LoggerFactory.getLogger(StartupDataLoader::class.java)

) {

    @EventListener(ApplicationReadyEvent::class)
    fun trainWithInitialData() {
        val resource = resourceLoader.getResource("classpath:glucose-training-data.json")
        val inputStream = resource.inputStream

        val jsonNode: JsonNode = objectMapper.readTree(inputStream)

        // Extract glucose_data as List<GlucoseData>
        val glucoseDataList: List<GlucoseData> = objectMapper
            .convertValue(jsonNode.get("glucose_data"), Array<GlucoseData>::class.java)
            .toList()

        // Prepare request body
        val requestBody = mapOf("glucose_data" to glucoseDataList)

        llmService.trainModel(requestBody)

        logger.info("Dexcom model is trained with initial data")
    }

}
