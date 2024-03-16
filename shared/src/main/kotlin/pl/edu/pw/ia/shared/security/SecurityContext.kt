package pl.edu.pw.ia.shared.security

import java.util.UUID
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt

object SecurityContext {

	@JvmStatic
	fun getAccountId(): UUID = ReactiveSecurityContextHolder.getContext()
		.map { it.authentication }
		.mapNotNull { it.principal }
		.mapNotNull<Jwt> { if (it is Jwt) it else null }
		.map { UUID.fromString(it.subject) }
		.blockOptional()
		.orElseThrow()
}
