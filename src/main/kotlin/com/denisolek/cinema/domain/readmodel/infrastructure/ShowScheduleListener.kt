package com.denisolek.cinema.domain.readmodel.infrastructure

import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.show.model.ShowAdded
import com.denisolek.cinema.domain.show.model.ShowPriceUpdated
import com.denisolek.cinema.domain.show.model.ShowRemoved
import com.denisolek.cinema.domain.show.model.ShowTimeUpdated

interface ShowScheduleListener {
    fun handle(event: ShowAdded)
    fun handle(event: ShowTimeUpdated)
    fun handle(event: ShowPriceUpdated)
    fun handle(event: ShowRemoved)
    fun handle(event: MovieLoaded)
}