package pl.edu.pw.ia.posts

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication(scanBasePackages = ["pl.edu.pw.ia"])
@OpenAPIDefinition(
	info = Info(title = "Posts Service")
)
class PostsApplication

fun main(args: Array<String>) {
	runApplication<PostsApplication>(*args)
}
