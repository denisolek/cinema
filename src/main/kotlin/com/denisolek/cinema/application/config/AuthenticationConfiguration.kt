package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.authentication.AuthenticationFacade
import com.denisolek.cinema.infrastructure.persistance.InMemoryAuthenticationRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthenticationConfiguration {
    @Bean
    fun authenticationFacade() = AuthenticationFacade(InMemoryAuthenticationRepository())
}