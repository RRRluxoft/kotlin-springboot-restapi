package com.course.kotlin.rlytvyshko.catalogue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content


@WebMvcTest(UserController::class)
internal class RestApiTest {
    @Autowired
    lateinit var mapper: ObjectMapper
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userController: UserController
    @MockBean
    lateinit var movieController: MovieController
    @MockBean
    lateinit var userRepository: UserRepository
    @MockBean
    lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun before() {
        mapper = ObjectMapper().registerModule(KotlinModule())
    }

    @Test
    fun addMovieTest() {
        mockMvc.post("/api/movies/add") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(Movie(title = "Movie 44", country = "USA", director = "Kim Hyim", year = 2020))
        }.andExpect {
            status {
                isOk
            }
            content { content().string("") }
        }
    }


    @Test
    fun `get all users test`() {
        mockMvc.get("/api/users/") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(PageRequest.of(0, 5, Sort.by("nickName").descending()))
        }.andDo { print("\n\n\n\n\t\t\t >>>> all users") }
                .andExpect {
                    status { isOk }
                }

    }


}