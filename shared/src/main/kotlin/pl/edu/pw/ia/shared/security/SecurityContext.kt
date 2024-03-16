package pl.edu.pw.ia.shared.security

import java.util.UUID
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import pl.edu.pw.ia.shared.domain.exception.MissingAccountIdException

object SecurityContext {

	@JvmStatic
	fun getAccountId(): UUID = ReactiveSecurityContextHolder.getContext()
		.map { it.authentication }
		.mapNotNull { it.principal }
		.mapNotNull<UUID> {
			when (it) {
				is Jwt -> UUID.fromString(it.subject)
				is UUID -> it
				else -> null
			}
		}
		.blockOptional()
		.orElseThrow { MissingAccountIdException() }
}
