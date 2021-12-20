package com.denisolek.cinema.defaults

import com.denisolek.cinema.defaults.MovieDefaults.movieAwards
import com.denisolek.cinema.defaults.MovieDefaults.movieDescription
import com.denisolek.cinema.defaults.MovieDefaults.movieImdbID
import com.denisolek.cinema.defaults.MovieDefaults.movieImdbRating
import com.denisolek.cinema.defaults.MovieDefaults.movieImdbVotes
import com.denisolek.cinema.defaults.MovieDefaults.movieReleaseDate
import com.denisolek.cinema.defaults.MovieDefaults.movieRuntime
import com.denisolek.cinema.defaults.MovieDefaults.movieTitle
import com.denisolek.cinema.infrastructure.http.omdb.OMDbMovieResponse


object HttpDefaults {
    val omdbApiKey = "someApiKey"
    val omdbBaseUrl = "localhost"
    val omdbPort = 5000
    val defaultOMDbResponse = OMDbMovieResponse(
        title = movieTitle,
        plot = movieDescription,
        released = movieReleaseDate,
        awards = movieAwards,
        imdbRating = movieImdbRating,
        imdbVotes = movieImdbVotes,
        imdbID = movieImdbID,
        runtime = movieRuntime
    )
}