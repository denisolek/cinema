package com.denisolek.cinema.utils

import com.denisolek.cinema.defaults.HttpDefaults.omdbPort
import com.denisolek.cinema.http.OMDbStubs
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer

object Stubs {
    val omdb = OMDbStubs(omdbPort)
}

abstract class HttpStub(port: Int) {
    val wiremock = WireMockServer(port)
    val mapper = ObjectMapper().findAndRegisterModules()

    init {
        wiremock.start()
    }

    fun toJson(body: Any): String = mapper.writeValueAsString(body)
}