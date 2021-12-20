package com.denisolek.cinema.domain.show.model

import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ShowId

data class Show(
    val id: ShowId,
    val movieId: MovieId,
    val showtime: Showtime,
    val price: Price
)