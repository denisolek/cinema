package com.denisolek.cinema.application.config

import com.denisolek.cinema.domain.authentication.AuthenticationFacade
import com.denisolek.cinema.domain.authentication.infrastructure.AuthenticationRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthenticationConfiguration {

    @Bean
    fun authenticationFacade(authenticationRepository: AuthenticationRepository) = AuthenticationFacade(authenticationRepository)
}

class DefaultUser(val token: String, val userId: String, val roles: Set<String>)