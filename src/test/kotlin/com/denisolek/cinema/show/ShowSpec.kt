package com.denisolek.cinema.show

import io.kotest.core.spec.style.DescribeSpec

class ShowSpec : DescribeSpec({
    describe("Adding a show") {
        it("should emmit show added event")
        it("should save a show")
    }

    describe("Can't add a show") {
        it("when provided movie doesn't exist")
        it("when it's showtime overlaps another show")
    }

    describe("Updating showtime") {

    }
})