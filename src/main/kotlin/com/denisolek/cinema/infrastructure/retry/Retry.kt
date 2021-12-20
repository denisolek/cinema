package com.denisolek.cinema.infrastructure.retry

import arrow.fx.coroutines.Schedule
import arrow.fx.coroutines.Schedule.Companion.doWhile
import arrow.fx.coroutines.Schedule.Companion.exponential
import arrow.fx.coroutines.Schedule.Companion.recurs
import arrow.fx.coroutines.retry
import java.time.Duration
import kotlin.reflect.KClass

suspend fun <T> retry(config: RetryProperties = RetryProperties(), block: suspend () -> T): T {
    return retry(config.retryableExceptions, config.retries, config.exponentialBackoff, block)
}

suspend fun <T> retry(
    retryableExceptions: List<KClass<out Throwable>>,
    retries: Int,
    backOffExponential: Duration,
    block: suspend () -> T
): T = retrySchedule(retryableExceptions, retries, backOffExponential)
    .retry { block() }

private fun retrySchedule(
    retryableExceptions: List<KClass<out Throwable>>,
    retries: Int,
    backOffExponential: Duration
): Schedule<Throwable, Throwable> =
    exponentialRecur(retries, backOffExponential) zipRight doWhile { retryableExceptionOccurred(retryableExceptions, it) }

private fun exponentialRecur(retries: Int, exponential: Duration) = recurs<Throwable>(retries).and(exponential(exponential.toNanos().toDouble()))
private fun retryableExceptionOccurred(retryOn: List<KClass<out Throwable>>, throwable: Throwable) = retryOn.any { it == throwable::class }