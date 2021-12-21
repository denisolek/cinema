package com.denisolek.cinema.domain.readmodel.infrastructure

import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.review.model.SummedReviewChanged

interface MovieDetailsListener {
    fun handle(event: MovieLoaded)
    fun handle(event: SummedReviewChanged)
}