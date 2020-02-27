package com.course.kotlin.rlytvyshko.catalogue

import io.swagger.annotations.ApiModelProperty
import mu.KLogger
import mu.KotlinLogging

fun Float.round(decimals: Int = 2): Float = "%.${decimals}f".format(this).toFloat()

inline fun <reified T : KLogger> T.logger() : KLogger = KotlinLogging.logger(this)

data class SwaggerPageable(
        @ApiModelProperty("Number of records per page", example = "20")
        val size: Int?,

        @ApiModelProperty("Results page you want to retrieve (0..N)", example = "0")
        val page: Int?,

        @ApiModelProperty("Sorting criteria in the format: property(,asc|desc)." +
                "Default sort order is ascending. Multiple sort criteria are supported.")
        var sort: String?
)