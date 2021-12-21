package com.denisolek.cinema.domain.review.infrastructure

import com.denisolek.cinema.domain.review.model.ReviewAdded
import com.denisolek.cinema.domain.review.model.ReviewUpdated

interface ReviewEventListener {
    fun handle(event: ReviewAdded)
    fun handle(event: ReviewUpdated)
}