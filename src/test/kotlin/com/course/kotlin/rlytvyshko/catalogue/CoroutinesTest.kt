package com.course.kotlin.rlytvyshko.catalogue

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

@ExtendWith(SpringExtension::class)
class CoroutinesTest {

    @Test
    fun `add mark and get rate test`() {
        val movie = Movie("Snatch", "Guy Ritchie", "UK", 2000, BigDecimal("19.98"), rate = 10.0F)
        val rateBefore = movie.mark
        println("Before : $rateBefore")
        println("Voted ${movie.markedBy}")

        assert(movie.mark == rateBefore)

        val oneMoreMark = 4.0F
        movie.mark = oneMoreMark // add new mark
        println(movie.mark )
        val result = (rateBefore + oneMoreMark) / 2

        println("Voted ${movie.markedBy}")
        assert(result == movie.mark)
    }

    suspend fun massiveRun(action: suspend () -> Unit) {
        val n = 10  // number of coroutines to launch
        val k = 1000 // times an action is repeated by each coroutine
        val time = measureTimeMillis {
            coroutineScope { // scope for coroutines
                repeat(n) {
                    launch {
                        repeat(k) { action() }
                    }
                }
            }
        }
        println("Completed ${n * k} actions in $time ms")
    }

    @Test
    fun `Movie add mark and change rating on coroutines test`() {
        var m = AtomicInteger()
        val movie = Movie("Snatch", "Guy Ritchie", "UK", 2000, BigDecimal("19.98"), rate = 10.0F)
        runBlocking {
            withContext(Dispatchers.Default) {
                massiveRun {
//                    m.incrementAndGet()
                    movie.mark = 10.0f
                }
                massiveRun {
                    assert(movie.rate == 10.0f)
                }
            }
            println("Counter = $m And rate is: ${movie.rate} after ${movie.markedBy} review")
        }
    }

}