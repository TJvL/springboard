package dev.tjvl.springboard.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages =
        ["dev.tjvl.springboard.inference.web", "dev.tjvl.springboard.expression.web"],
)
class SpringboardApplication

fun main(args: Array<String>) {
    runApplication<SpringboardApplication>(*args)
}
