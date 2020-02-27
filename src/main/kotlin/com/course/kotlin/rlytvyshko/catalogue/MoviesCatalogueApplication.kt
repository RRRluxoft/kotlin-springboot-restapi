package com.course.kotlin.rlytvyshko.catalogue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoviesCatalogueApplication

fun main(args: Array<String>) {
    runApplication<MoviesCatalogueApplication>(*args) {
        addInitializers(
            Beans().init4()
        )
    }
}