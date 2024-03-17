package pl.edu.pw.ia.users

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication(scanBasePackages = ["pl.edu.pw.ia"])
@OpenAPIDefinition(
	info = Info(title = "Users Service")
)
@EnableDiscoveryClient
class UsersApplication

fun main(args: Array<String>) {
	runApplication<UsersApplication>(*args)
}
