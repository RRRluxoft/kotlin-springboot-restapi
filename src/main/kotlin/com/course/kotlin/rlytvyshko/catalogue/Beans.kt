package com.course.kotlin.rlytvyshko.catalogue

import io.swagger.annotations.Api
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.beans
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import java.math.BigDecimal

@Api(value = "Swagger information", tags = ["All of them"])
@Configuration
class Beans {

    @Bean
    fun init4() = beans {
        println("Initializer started ")
        bean("initit") {
            val userRepo = ref<UserRepository>()
            val movieRepo = ref<MovieRepository>()
            val movie01 = Movie("Snatch", "Guy Ritchie", "UK", 2000, BigDecimal("19.98"))
            val movie02 = Movie("Snatch2", "Guy Ritchie", "UK", 2022, BigDecimal("89.98"))
            movieRepo.saveAll(listOf(movie01, movie02)).let { println(it) }

            val user01 = User("kotlin02", "kotlin2@kt.kt", 33, mutableListOf())
            userRepo.save(user01).let { println(it) }
        }
        bean("routes") {
            val userRepo = ref<UserRepository>()
            val movieRepo = ref<MovieRepository>()
            router {
                ("/movies").nest {
                    GET("/") { _ ->
                        ServerResponse.ok().body(movieRepo.findAll(
                                PageRequest.of(0, 2, Sort.by("year").and(Sort.by("title")))
                        ))
                    }
                    GET("/{id}") { req ->
                        val title = req.pathVariable("id")
                        ServerResponse.ok().body(movieRepo.findByTitleContainingIgnoreCase(title, PageRequest.of(0, 2)))
                    }
                }

                (accept(MediaType.APPLICATION_JSON) and "/users").nest {
                    GET("/") { _ ->
                        ServerResponse.ok().body(userRepo.findAll(
                                PageRequest.of(0, 2, Sort.by("userId"))))
                    }
                    GET("/{id}") { req ->
                        val id = req.pathVariable("id")
                        ServerResponse.ok().body(userRepo.findById(id.toLong()))
                    }
                    GET("/{id}/profile") { req ->
                        val id = req.pathVariable("id")
                        ServerResponse.ok().body(userRepo.findByNickNameLike(id)
//                                    ?: "User doesnt exist\n${userRepo.findAll(Sort.by("userId"))}"
                        )
                    }
                    GET("/{id}/orders") { req ->
                        val nick = (req.pathVariable("id"))
                        val user = userRepo.findByNickNameLike(nick).orElseThrow { throw IllegalArgumentException("User doesnt exist") }
                        ServerResponse.ok().body {
                            "User ${user.nickName} ordered\n ${user.movies.forEach { println(it) }}"
                        }
                    }
                    POST("/add") { req ->
                        ServerResponse.ok().body(userRepo.save(req.body(User::class.java)))
                    }
                }
            }
        }
    }
}
