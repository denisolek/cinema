package com.denisolek.cinema.review

import io.kotest.core.spec.style.DescribeSpec

class ReviewSpec : DescribeSpec({
    describe("Reviewing a movie") {
        it("should emmit review added event")
        it("should save a review")
    }

    describe("Can't review a movie") {
        it("when it doesn't exist")
        it("if given stars are out of range")
    }

    describe("Reviewing already reviewed movie") {
        it("should update existing review")
        it("should emmit review updated event")
    }
})