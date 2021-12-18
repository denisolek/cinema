package com.denisolek.cinema.domain.shared

sealed class Failure(val reason: String) {
    class IOError(reason: String) : Failure(reason)
    class ValidationError(reason: String) : Failure(reason)
}