package com.denisolek.cinema.defaults

import com.denisolek.cinema.domain.movie.infrastructure.MovieData
import com.denisolek.cinema.domain.movie.model.Movie
import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.movie.model.MovieLoaded.MovieLoadedRating
import com.denisolek.cinema.domain.movie.model.Rating.Companion.imdbRating
import com.denisolek.cinema.domain.movie.model.Rating.Companion.internalRating
import com.denisolek.cinema.domain.movie.model.RatingSource.IMDB
import com.denisolek.cinema.domain.movie.model.RatingSource.INTERNAL
import com.denisolek.cinema.domain.movie.model.ReleaseDate.Companion.releaseDate
import com.denisolek.cinema.domain.movie.model.Runtime.Companion.runtime
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.rightValue
import java.util.*

object MovieDefaults {
    val movieImdbID = "tt0232500"
    val movieId = MovieId(movieImdbID)
    val movieTitle = "The Fast and the Furious"
    val movieDescription = "Los Angeles police officer Brian O'Conner must decide where his loyalty really lies when he becomes enamored with the street racing world he has been sent undercover to destroy."
    val movieReleaseDate = "22 Jun 2001"
    val movieAwards = "11 wins & 18 nominations"
    val movieImdbRating = "6.8"
    val movieImdbVotes = "370,116"
    val movieInternalRating = "0.0"
    val movieInternalVotes = "0"
    val movieRuntime = "106 min"

    val movieLoadedId = UUID.randomUUID()
    val movieLoadedImdb = IMDB.name
    val movieLoadedInternal = INTERNAL.name

    val defaultMovieData = MovieData(
        id = movieImdbID,
        title = movieTitle,
        description = movieDescription,
        releaseDate = movieReleaseDate,
        awards = movieAwards,
        imdbRating = movieImdbRating,
        imdbVotes = movieImdbVotes,
        runtime = movieRuntime
    )

    val defaultMovie = Movie(
        id = movieId,
        title = movieTitle,
        description = movieDescription,
        releaseDate = releaseDate(movieReleaseDate).rightValue,
        awards = movieAwards,
        ratings = listOf(
            imdbRating(movieImdbRating, movieImdbVotes).rightValue,
            internalRating(movieInternalRating, movieInternalVotes).rightValue
        ),
        runtime = runtime(movieRuntime).rightValue
    )

    val defaultMovieLoaded = MovieLoaded(
        id = movieLoadedId,
        movieId = movieId.value,
        title = movieTitle,
        description = movieDescription,
        releaseDate = releaseDate(movieReleaseDate).rightValue.value,
        awards = movieAwards,
        ratings = listOf(
            MovieLoadedRating(imdbRating(movieImdbRating, movieImdbVotes).rightValue),
            MovieLoadedRating(internalRating(movieInternalRating, movieInternalVotes).rightValue)
        ),
        runtime = runtime(movieRuntime).rightValue.value
    )
}