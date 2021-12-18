package com.denisolek.cinema.infrastructure.retry

import io.ktor.client.features.*
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration
import java.time.Duration.ofMillis
import kotlin.reflect.KClass

val defaultRetryableExceptions = listOf(ServerResponseException::class)

@ConstructorBinding
class RetryProperties(
    val retryableExceptions: List<KClass<out Throwable>> = defaultRetryableExceptions,
    val retries: Int = 2,
    val exponentialBackoff: Duration = ofMillis(200)
)