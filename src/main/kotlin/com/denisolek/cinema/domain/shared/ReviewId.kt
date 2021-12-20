package com.denisolek.cinema.domain.shared

import java.util.*
import java.util.UUID.randomUUID

data class ReviewId(val value: UUID) {
    constructor() : this(randomUUID())
}