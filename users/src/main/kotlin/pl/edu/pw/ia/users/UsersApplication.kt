package pl.edu.pw.ia.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["pl.edu.pw.ia"])
class UsersApplication

fun main(args: Array<String>) {
	runApplication<UsersApplication>(*args)
}
