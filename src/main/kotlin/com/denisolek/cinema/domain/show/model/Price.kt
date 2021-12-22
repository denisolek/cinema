package com.denisolek.cinema.domain.show.model

import arrow.core.Either
import com.denisolek.cinema.domain.shared.ValidationError.PriceValidationError
import com.denisolek.cinema.domain.shared.ValidationError.PriceValidationError.InvalidPrice
import com.denisolek.cinema.domain.shared.ValidationError.PriceValidationError.NegativePrice
import com.denisolek.cinema.domain.show.model.Currency.USD
import mu.KotlinLogging.logger
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP

data class Price private constructor(
    val amount: BigDecimal,
    val currency: Currency
) {
    val asDouble: Double
        get() = amount.toDouble()

    companion object {
        private val log = logger {}

        fun persistedPrice(amount: BigDecimal, currency: Currency) = Price(amount, currency)

        fun price(amount: BigDecimal, currency: Currency = USD): Either<PriceValidationError, Price> = Either.catch {
            if (priceIsNegative(amount)) throw NegativePriceException()
            Price(amount.setScale(2, HALF_UP), currency)
        }.mapLeft { ex: Throwable ->
            log.error(ex) { ex.message }
            when (ex) {
                is NegativePriceException -> NegativePrice(amount)
                else -> InvalidPrice(amount, currency)
            }
        }

        private fun priceIsNegative(amount: BigDecimal): Boolean = amount < ZERO
    }
}

enum class Currency {
    USD, EUR
}

class NegativePriceException : Throwable()