package com.denisolek.cinema

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Sample : FunSpec({
    test("String length should return the length of the string") {
        "sammy".length shouldBe 5
        "".length shouldBe 0
    }
})