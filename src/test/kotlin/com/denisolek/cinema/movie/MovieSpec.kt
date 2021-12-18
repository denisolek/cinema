package com.denisolek.cinema.movie

import io.kotest.core.spec.style.DescribeSpec

class MovieSpec : DescribeSpec({
    describe("Loading single movie") {
        it("should emmit movie loaded event")
        it("should save a movie")
    }

    describe("Loading multiple movies") {
        it("should process all of them")
    }

    describe("Can't load a movie when it fails to fetch movie details") {
    }
})