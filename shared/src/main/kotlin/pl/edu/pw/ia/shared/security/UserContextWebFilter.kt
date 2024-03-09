package pl.edu.pw.ia.shared.security

import java.util.UUID
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
@Profile("!disableSecurity")
class UserContextWebFilter : WebFilter {

	override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
			ReactiveSecurityContextHolder.getContext()
					.mapNotNull { it.authentication.principal }
					.cast(Jwt::class.java)
					.map {
						val userId = UUID.fromString(it.subject)
						exchange.setUserId(userId)
						return@map it
					}
					.then(chain.filter(exchange))
}

@Configuration
@Profile("disableSecurity")
class TestUserContextWebFilter : WebFilter {

	override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
		val headerUserId = exchange.request.headers["X-User-Id"]?.firstOrNull()
		val userId = UUID.fromString(headerUserId ?: TEST_USER_ID)
		exchange.setUserId(userId)
		return chain.filter(exchange)
	}
}

const val TEST_USER_ID = "facade00-0000-4000-a000-000000000000"

const val USER_ID = "userId"

fun ServerWebExchange.getUserId(): UUID {
	return getAttribute<String>(USER_ID)
			?.let { UUID.fromString(it) }
			?: error("User id not found")
}

fun ServerWebExchange.setUserId(userId: UUID) {
	attributes[USER_ID] = userId.toString()
}
