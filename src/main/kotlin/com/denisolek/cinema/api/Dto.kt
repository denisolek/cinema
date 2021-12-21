package com.denisolek.cinema.api

import com.denisolek.cinema.domain.movie.MovieListingInfo
import com.denisolek.cinema.domain.readmodel.model.ShowSchedule
import java.math.BigDecimal
import java.time.Instant

data class MovieListingResponse(val movies: List<MovieListingInfo>)

data class ReviewRequest(val stars: Int)

data class AddShowRequest(val movieId: String, val start: Instant, val price: BigDecimal)

data class UpdateShowRequest(val start: Instant, val price: BigDecimal)

data class ShowSchedulesResponse(val schedules: List<ShowSchedule>)