package com.denisolek.cinema.infrastructure.http

import com.denisolek.cinema.infrastructure.retry.RetryProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.time.Duration
import java.time.Duration.ofMillis

abstract class HttpClientProperties(
    val url: String,
    val timeout: TimeoutProperties,
    @NestedConfigurationProperty
    val retry: RetryProperties
)

@ConstructorBinding
class TimeoutProperties(
    val request: Duration = ofMillis(500),
    val connect: Duration = ofMillis(300),
    val socket: Duration = ofMillis(300)
)