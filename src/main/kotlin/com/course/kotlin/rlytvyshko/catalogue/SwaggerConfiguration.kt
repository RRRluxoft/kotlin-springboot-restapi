package com.course.kotlin.rlytvyshko.catalogue

//import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@Configuration
@EnableSwagger2
open class SwaggerConfiguration  /* : WebFluxConfigurer */ {

    @Bean
    open fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .genericModelSubstitutes(Optional::class.java)
            .directModelSubstitute(Pageable::class.java, SwaggerPageable::class.java)
            .apiInfo(apiInfo())

    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder()
                .title("Spring Boot2.X Kotlin Use in Swagger2 structure RESTFul APIs")
                .description("More SpringBoot2.X Kotlin Pay attention to the ...")
                .termsOfServiceUrl("https://www.baeldung.com/spring-rest-openapi-documentation")
                .contact(Contact("Name here", "https://www.google.com/maps", "name@dataart.com"))
                .version("1.0.0")
                .build()
    }

}