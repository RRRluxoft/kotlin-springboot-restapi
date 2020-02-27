package com.course.kotlin.rlytvyshko.catalogue

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findByNickNameLike(nickName: String): Optional<User>
}

interface MovieRepository : PagingAndSortingRepository<Movie, Long> {
    fun findByTitleContainingIgnoreCase(title: String, pageable: Pageable): Page<Movie>

}