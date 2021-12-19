package com.denisolek.cinema.http

import com.denisolek.cinema.infrastructure.http.omdb.OMDbMovieResponse
import com.denisolek.cinema.movie.MovieDefaults.movieAwards
import com.denisolek.cinema.movie.MovieDefaults.movieDescription
import com.denisolek.cinema.movie.MovieDefaults.movieImdbID
import com.denisolek.cinema.movie.MovieDefaults.movieImdbRating
import com.denisolek.cinema.movie.MovieDefaults.movieImdbVotes
import com.denisolek.cinema.movie.MovieDefaults.movieReleaseDate
import com.denisolek.cinema.movie.MovieDefaults.movieRuntime
import com.denisolek.cinema.movie.MovieDefaults.movieTitle


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