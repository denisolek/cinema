package com.denisolek.cinema.defaults

import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieListingInfo
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieLoaded
import com.denisolek.cinema.defaults.ShowDefaults.defaultShowAdded
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.readmodel.model.ShowSchedule
import java.time.Duration.ofMinutes

object ReadmodelDefaults {
    val defaultMovieDetails = MovieDetails(
        id = defaultMovieLoaded.movieId,
        title = defaultMovieLoaded.title,
        description = defaultMovieLoaded.description,
        releaseDate = defaultMovieLoaded.releaseDate,
        awards = defaultMovieLoaded.awards,
        internalRating = 0.0,
        internalVotes = 0,
        imdbRating = defaultMovieLoaded.imdbRating,
        imdbVotes = defaultMovieLoaded.imdbVotes,
        runtime = ofMinutes(defaultMovieLoaded.runtime)
    )

    val defaultShowSchedule = ShowSchedule(
        showId = defaultShowAdded.showId,
        start = defaultShowAdded.start,
        end = defaultShowAdded.end,
        price = defaultShowAdded.price,
        currency = defaultShowAdded.currency,
        movieId = defaultMovieListingInfo.id,
        movieTitle = defaultMovieListingInfo.title,
        movieDescription = defaultMovieListingInfo.description,
        movieRuntime = defaultMovieListingInfo.runtime
    )
}