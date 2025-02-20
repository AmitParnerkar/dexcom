package com.dexcom.ai

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DexcomAiApplication

fun main(args: Array<String>) {
    runApplication<DexcomAiApplication>(*args)
}
