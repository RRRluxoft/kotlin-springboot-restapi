package com.course.kotlin.rlytvyshko.catalogue

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KLogging
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.math.RoundingMode
import javax.validation.Valid

@Api(value = "Users information", tags = ["Users"])
@RestController
@RequestMapping("/api/users")
open class UserController {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var movieRepository: MovieRepository

    private val logger = KotlinLogging.logger {}

    @ApiOperation("Obtain user's list page by page")
    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(pageable: Pageable): ResponseEntity<Page<User>> {
        logger.info { " >>>>> Getting list of users page by page <<<<<" }
        return userRepository.findAll(pageable).let {
            logger.info { "\nWe have ${it.totalElements} user(s)" }
            ResponseEntity.ok(it)
        }
    }

    @ApiOperation("View user’s profile by id: Long")
    @GetMapping("/{id}/profile", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        return userRepository.findById(id)
                .map { ResponseEntity.ok(it) }
                .orElseGet { ResponseEntity.notFound().build() }
    }

    @ApiOperation("Add some value to the balance for current User")
    @PutMapping("/{id}/balance/{amount}")
    fun addMoney(@PathVariable id: String, @PathVariable amount: String): ResponseEntity<User> {
        val user = userRepository.findById(id.toLong())
                .map {
                    it.balance += BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
                    userRepository.save(it)
                }
        val balance = userRepository.findById(id.toLong())
                .map { it.balance }
                .orElseThrow {
                    logger.warn { "User with ${id} doesnt exist" };
                    NotFoundException("User with ${id} doesnt exist")
                }
        logger.info { "\n >>> Balance for userID = ${id} has been updated : added ${amount} $\nCurrent balance is ${balance}" }
        return user.map { ResponseEntity.accepted().body(it) }
                .orElseGet { logger.warn { "Balance wasnt changed" }; ResponseEntity.notFound().build<User>() }
    }

    @ApiOperation("Order the movie (make sure that user’s balance is decreased by the defined amount)")
    @PutMapping("{userId}/orders/{movieId}")
    fun addOrder(@PathVariable userId: String, @PathVariable movieId: String): ResponseEntity<User> {
        val movie = movieRepository.findById(movieId.toLong())
                .orElseThrow {
                    logger.warn { "Movie with ID=${movieId} doesnt exist"; }
                    NotFoundException("Movie with ID=${movieId} doesnt exist")
                }
        logger.info { "\nGot movie for buying ${movie}" }


        val user = userRepository.findById(userId.toLong())
        if (user.filter { it.balance < movie.price }.isPresent) {
            logger.warn { "\n >>>> User ${userId} have to fill in account" }
            throw NotEnoughMoneyException("User ${userId} have to fill in account")
        }
        if (user.filter { it.movies.contains(movie) }.isPresent) {
            logger.warn { "\n >>>> User ${userId} dont need this movie ${movie}" }
            throw ElementAlreadyExistsException("User ${userId} has movie ${movie.title}")
        } else {
            user.map {
                it.balance -= movie.price; it.movies.add(movie)
                userRepository.save(it)
                it
            }
            logger.info { "\n Movie ${movie} has beeen bought by User with ID=${userId}" }
        }

        return when (user.isPresent) {
            true -> ResponseEntity.of(user)
            else -> ResponseEntity.notFound().build()
        }
    }

}

@Api(value = "Movies information", tags = ["Movies"])
@RestController
@RequestMapping("/api/movies")
open class MovieController {

    @Autowired
    lateinit var movieRepository: MovieRepository

    @ApiOperation("Obtain movie's list in catalogue page by page")
    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(pageable: Pageable): ResponseEntity<Page<Movie>> {
        logger.info { "\n >>>>> Retrieve movie's list in catalogue page by page <<<<< " }
        return movieRepository.findAll(pageable).let { ResponseEntity.ok(it) }
    }

    @ApiOperation("Search movies by name. Works with part of name. Case sensitive ignored")
    @GetMapping("/{title}", produces = [MediaType.ALL_VALUE])
    fun getMovieByTitle(@PathVariable title: String, pageable: Pageable): ResponseEntity<List<String>> {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable)
                .content.map { it.getDetailedInfo() }.let { ResponseEntity.ok(it) }
    }

    @ApiOperation("Add new movie")
    @PostMapping("/add")
    fun addNewMovie(@Valid @RequestBody movie: Movie): ResponseEntity<Movie> {
        if (movieRepository.findAll().contains(movie)) throw ElementAlreadyExistsException("Movie ${movie} already exists")
        val entity = movieRepository.save(movie)
        logger.info { "Movie $entity posted" }
        return ResponseEntity<Movie>(entity, HttpStatus.CREATED)
    }

    @ApiOperation("Add new mark to Movie and calculate rate")
    @PutMapping("/{movieId}/mark/{mark}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun addMark(@PathVariable movieId: Long, @Validated @PathVariable mark: Int): ResponseEntity<Movie> {
        val movie = movieRepository.findById(movieId)
                .map { it.mark = mark.toFloat(); movieRepository.save(it); it }
        return when (movie.isPresent) {
            true -> ResponseEntity.of(movie)
            else -> ResponseEntity.notFound().build()
        }
    }

    companion object : KLogging()
}