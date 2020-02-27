package com.course.kotlin.rlytvyshko.catalogue

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.*

@ExtendWith(SpringExtension::class)
@DataJpaTest
internal class RepositoryTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository,
        val movieRepository: MovieRepository
) {

    @Test
    fun `add just a new user test`() {
        val userKt = User("kotlin02", "kotlin2@kt.kt", 33, mutableListOf<Movie?>(null))
        userRepository.deleteAll()
        entityManager.persistAndFlush(userKt)
        assertEquals(Optional.of(userKt), userRepository.findByNickNameLike(userKt.nickName))
    }

    @Test
    fun `add new movie test`() {
        val snatchMovie = Movie("Snatch", "Guy Ritchie", "UK", 2000, BigDecimal("19.98"), rate = 10.0F)
        movieRepository.deleteAll()
        entityManager.persistAndFlush(snatchMovie)
        assertEquals(snatchMovie, movieRepository.findByTitleContainingIgnoreCase(snatchMovie.title, PageRequest.of(0, 2)).first() )
    }

    @Test
    fun `Search movies by name test`() {
        val snatchMovie = Movie("Snatch", "Guy Ritchie", "UK", 2000, BigDecimal("19.98"))
        val userKt = User("kotlin01", "kotlin@kt.kt", 33, mutableListOf<Movie?>(snatchMovie))
        entityManager.persistAndFlush(userKt)
        assertEquals(Optional.of(userKt), userRepository.findByNickNameLike(userKt.nickName))
        val movie = movieRepository.findByTitleContainingIgnoreCase(snatchMovie.title, PageRequest.of(0, 2)).first()
        assertEquals(snatchMovie, movie)
        assertEquals(snatchMovie, movieRepository.findByIdOrNull(movie.movieId))
    }

    @Test
    fun `get user's orders movies test`() {
        val snatchMovie = Movie("Snatch", "Guy Ritchie", "UK", 2000, BigDecimal("19.98"))
        val userKt = User("kotlin01", "kotlin@kt.kt", 33, mutableListOf<Movie?>(snatchMovie))
        userKt.movies.add(snatchMovie.copy(title = "Snatch2", year = 2022, price = BigDecimal("89.89")))
        entityManager.persistAndFlush(userKt)

        val movieList = userRepository.findByNickNameLike(userKt.nickName).get().movies
        assertEquals(2, movieList.size)
    }

    @Test
    fun `add some money for user test`() {
        val userKt = User("kotlin01", "kotlin@kt.kt", 33, mutableListOf<Movie?>(null))
        val balanceBefore = userKt.balance
        userRepository.deleteAll()
        val id = entityManager.persistAndGetId(userKt) as Long
        userRepository.findById(id).map { it.balance += BigDecimal("100") }
        val balanceAfter = userRepository.findById(id).get().balance
        assertTrue(balanceBefore < balanceAfter)
        println("Before : ${balanceBefore}\nAfter : ${balanceAfter}")
        assertEquals( BigDecimal("100"), balanceAfter.minus(balanceBefore))
    }

}