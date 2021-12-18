package com.denisolek.cinema.api

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/movies"], produces = [APPLICATION_JSON_VALUE])
class MovieEndpoint