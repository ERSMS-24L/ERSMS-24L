package pl.edu.pw.ia.shared.security

import java.util.UUID
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import pl.edu.pw.ia.shared.domain.exception.MissingAccountIdException
import reactor.core.publisher.Mono

@Configuration
@Profile("!disableSecurity")
class UserContextWebFilter : WebFilter {

	override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
		ReactiveSecurityContextHolder.getContext()
			.mapNotNull { it.authentication.principal }
			.cast(Jwt::class.java)
			.map {
				val accountId = UUID.fromString(it.subject)
				exchange.setAccountId(accountId)
				return@map it
			}
			.then(chain.filter(exchange))
}

@Configuration
@Profile("disableSecurity")
class TestUserContextWebFilter : WebFilter {

	override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
		val headerUserId = exchange.request.headers["X-User-Id"]?.firstOrNull()
		val accountId = UUID.fromString(headerUserId ?: TEST_ACCOUNT_ID)
		exchange.setAccountId(accountId)
		return chain.filter(exchange)
			.contextWrite(
				ReactiveSecurityContextHolder.withAuthentication(
					TestingAuthenticationToken(accountId, null)
				)
			)
	}
}

const val TEST_ACCOUNT_ID = "facade00-0000-4000-a000-000000000000"

const val ACCOUNT_ID = "accountId"

fun ServerWebExchange.getAccountId(): UUID {
	return getAttribute<String>(ACCOUNT_ID)
		?.let { UUID.fromString(it) }
		?: throw MissingAccountIdException()
}

fun ServerWebExchange.setAccountId(accountId: UUID) {
	attributes[ACCOUNT_ID] = accountId.toString()
}
