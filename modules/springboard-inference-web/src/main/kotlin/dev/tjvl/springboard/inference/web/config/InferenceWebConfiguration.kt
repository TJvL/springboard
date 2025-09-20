package dev.tjvl.springboard.inference.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
@Profile("inference")
class InferenceWebConfiguration : WebFluxConfigurer {
}
