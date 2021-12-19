package com.denisolek.cinema.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer

abstract class HttpStub(port: Int) {
    val wiremock = WireMockServer(port)
    val mapper = ObjectMapper().findAndRegisterModules()

    init {
        wiremock.start()
    }

    fun toJson(body: Any): String = mapper.writeValueAsString(body)
}