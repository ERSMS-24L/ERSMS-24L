package pl.edu.pw.ia.shared.domain.event

import java.time.Instant
import java.util.UUID
import org.axonframework.serialization.Revision

// TODO: add support for:
//  - oauth2 id from provider
//  - roles / permissions

@Revision("1.0")
data class AccountCreatedEvent(
	val accountId: UUID,
	val name: String,
	val email: String,
	val createdAt: Instant,
)

@Revision("1.0")
data class AccountUpdatedEvent(
	val accountId: UUID,
	val name: String,
	val email: String,
)
