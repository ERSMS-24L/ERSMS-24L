package pl.edu.pw.ia.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication(scanBasePackages = ["pl.edu.pw.ia"])
class UsersApplication

fun main(args: Array<String>) {
	runApplication<UsersApplication>(*args)
}
