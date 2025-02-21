plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.dexcom.ai"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2024.0.0"

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Actuator support
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Resilience4j
    implementation("org.springframework.boot:spring-boot-starter-aop") // Required for aspects
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2") // Use the latest version

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0") // Use the latest version

    // Hazelcast
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.hazelcast:hazelcast-spring:5.3.1")

    // Email support
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Load .env file
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
