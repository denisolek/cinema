package com.denisolek.cinema.api

import com.denisolek.cinema.domain.movie.MovieListingInfo

data class MovieListingResponse(
    val movies: List<MovieListingInfo>
)

data class ReviewRequest(val stars: Int)