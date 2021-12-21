package com.denisolek.cinema.defaults

import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieLoaded
import com.denisolek.cinema.domain.readmodel.model.MovieDetails

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
        runtime = defaultMovieLoaded.runtime
    )
}