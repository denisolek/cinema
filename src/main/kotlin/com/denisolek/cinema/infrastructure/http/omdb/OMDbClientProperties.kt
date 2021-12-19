package com.denisolek.cinema.infrastructure.http.omdb

import com.denisolek.cinema.infrastructure.http.HttpClientProperties
import com.denisolek.cinema.infrastructure.http.TimeoutProperties
import com.denisolek.cinema.infrastructure.retry.RetryProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("clients.omdb")
class OMDbClientProperties(
    baseUrl: String,
    basePort: Int = 0,
    timeout: TimeoutProperties = TimeoutProperties(),
    retry: RetryProperties = RetryProperties(),
    val apiKey: String,
    val concurrentOperationsLimit: Int = 10
) : HttpClientProperties(baseUrl, basePort, timeout, retry)