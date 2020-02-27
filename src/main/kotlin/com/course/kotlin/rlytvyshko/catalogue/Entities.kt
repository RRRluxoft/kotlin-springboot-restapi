package com.course.kotlin.rlytvyshko.catalogue

import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "USERS")
data class User(
        @Column(name = "nick_name") val nickName: String = "",
        val email: String = "",
        val age: Int = 0,
        @OneToMany(fetch = FetchType.LAZY, targetEntity = Movie::class, cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "user_id")
        val movies: MutableList<Movie?> = mutableListOf(null),
        var balance: BigDecimal = BigDecimal.TEN,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "user_id") val userId: Long? = -1L
) {
    override fun toString(): String {
        return "\t>>> User : $nickName, email = $email and $age old and has $balance USD"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        if (email != other.email) return false
        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

}


@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "MOVIES")
data class Movie(
        val title: String = "",
        val director: String = "",
        val country: String = "",
        val year: Int = 1900,
        val price: BigDecimal = BigDecimal.ZERO,
        val description: String = "",
        @Min(1) @Max(10)
        var rate: Float = Random.nextInt(1, 10).toFloat(),
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "movie_id") val movieId: Long? = -1L
) {
    var sumOfMark: BigDecimal = BigDecimal.ZERO + BigDecimal(rate.toString())
    var markedBy: AtomicLong = AtomicLong(1L)
    var mark: Float
        @ObsoleteCoroutinesApi
//        @JsonIgnore
        get() {
            rate = (sumOfMark / BigDecimal(markedBy.get()) ).toFloat().round()
            return rate
        }
        @ObsoleteCoroutinesApi
        set(value) {
            val ctx = newSingleThreadContext("MarkContext")
            runBlocking {
                addMark(value, ctx)
            }
        }

    private suspend fun addMark(m: Float, ctx: CoroutineContext) = withContext(ctx) {
        markedBy.incrementAndGet()
        sumOfMark += BigDecimal(m.toString())
    }

    fun getDetailedInfo(): String {
        return """ID=$movieId; Movie '${this.title}' with rating ${this.rate} watched by ${this.markedBy} people
            | told as about ${this.description}
        """.trimMargin()
    }

    override fun toString(): String {
        return "\t>>> Movie: \"$title\" by $director. Produced by $country on $year. Costs $price USD only today"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Movie
        if (title != other.title) return false
        if (director != other.director) return false
        if (country != other.country) return false
        if (year != other.year) return false
        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + director.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + year
        return result
    }

}