package com.course.kotlin.rlytvyshko.catalogue

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException : RuntimeException {
    constructor(message: String, ex: Exception?): super(message, ex) {}
    constructor(message: String): super(message) {}
    constructor(ex: Exception): super(ex) {}
}

@ResponseStatus(HttpStatus.CONFLICT)
class AlreadyExistsException : ElementAlreadyExistsException()

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
class NotEnoughMoneyException(message: String) : RuntimeException(message)

typealias ElementAlreadyExistsException = RuntimeException