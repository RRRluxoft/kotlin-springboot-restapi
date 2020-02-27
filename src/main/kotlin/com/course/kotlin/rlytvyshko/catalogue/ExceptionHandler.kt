package com.course.kotlin.rlytvyshko.catalogue

import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
open class ExceptionHandler {
    companion object : KLogging()

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    open fun handleNorFoundException(ex: NotFoundException) {
        logger.error { "Exception : ${ex.message}" }
    }

    @ExceptionHandler(NotEnoughMoneyException::class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    open fun handleNotEnoughMoneyException(ex: NotEnoughMoneyException) {
        logger.error { "Exception : ${ex.message}" }
    }
}