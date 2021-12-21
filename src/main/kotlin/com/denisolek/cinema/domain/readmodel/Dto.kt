package com.denisolek.cinema.domain.readmodel

import java.time.Instant

data class FindShowSchedules(val from: Instant, val to: Instant)
data class FindMovieShows(val movieId: String, val from: Instant, val to: Instant)