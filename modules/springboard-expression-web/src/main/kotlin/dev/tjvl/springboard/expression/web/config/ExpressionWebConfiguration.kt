package dev.tjvl.springboard.expression.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
@Profile("expression")
class ExpressionWebConfiguration : WebFluxConfigurer {
}
