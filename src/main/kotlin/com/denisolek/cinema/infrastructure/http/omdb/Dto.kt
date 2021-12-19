package com.denisolek.cinema.infrastructure.http.omdb

import com.denisolek.cinema.domain.movie.infrastructure.MovieData
import com.fasterxml.jackson.annotation.JsonProperty

data class OMDbMovieResponse(
    @JsonProperty("Title")
    val title: String,
    @JsonProperty("Plot")
    val plot: String,
    @JsonProperty("Released")
    val released: String,
    @JsonProperty("Awards")
    val awards: String,
    val imdbRating: String,
    val imdbVotes: String,
    val imdbID: String,
    @JsonProperty("Runtime")
    val runtime: String
) {
    fun toMovieData(): MovieData = MovieData(
        id = imdbID,
        title = title,
        description = plot,
        releaseDate = released,
        awards = awards,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        runtime = runtime
    )
}