package com.denisolek.cinema.infrastructure.http

import com.denisolek.cinema.infrastructure.retry.RetryProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.time.Duration
import java.time.Duration.ofMillis

abstract class HttpClientProperties(
    val baseUrl: String,
    val basePort: Int,
    val timeout: TimeoutProperties,
    @NestedConfigurationProperty
    val retry: RetryProperties
) {
    fun prepareClient(): HttpClient = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = timeout.request.toMillis()
            connectTimeoutMillis = timeout.connect.toMillis()
            socketTimeoutMillis = timeout.socket.toMillis()
        }
        install(JsonFeature) {
            serializer = JacksonSerializer {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
        defaultRequest {
            host = baseUrl
            port = basePort
        }
    }
}

@ConstructorBinding
class TimeoutProperties(
    val request: Duration = ofMillis(800),
    val connect: Duration = ofMillis(600),
    val socket: Duration = ofMillis(600)
)