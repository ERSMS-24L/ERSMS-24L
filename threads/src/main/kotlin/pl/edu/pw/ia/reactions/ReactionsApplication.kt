package pl.edu.pw.ia.reactions

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication(scanBasePackages = ["pl.edu.pw.ia"])
@OpenAPIDefinition(
	info = Info(title = "Reactions Service")
)
class ReactionsApplication

fun main(args: Array<String>) {
	runApplication<ReactionsApplication>(*args)
}
