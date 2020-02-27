package com.course.kotlin.rlytvyshko.catalogue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.FixedLocaleResolver
import java.util.*

class WebTestConfig private constructor() {
    companion object {
        private val LOCALE = Locale.ENGLISH
        fun fixedLocalResolver(): LocaleResolver {
            return FixedLocaleResolver(LOCALE)
        }

        fun objectMapperHttpMessageConverter(): MappingJackson2HttpMessageConverter {
            val converter = MappingJackson2HttpMessageConverter()
            converter.objectMapper = objectMapper()
            return converter
        }

        fun objectMapper(): ObjectMapper {
            return Jackson2ObjectMapperBuilder()
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .featuresToEnable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                    .modulesToInstall(Jdk8Module(), KotlinModule(), JavaTimeModule())
                    .build()
        }
    }
}
