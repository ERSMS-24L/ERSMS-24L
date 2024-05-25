package pl.edu.pw.ia.shared.domain.event

import java.time.Instant
import java.util.UUID
import org.axonframework.serialization.Revision

@Revision("1.0")
data class ThreadCreatedEvent(
	val threadId: UUID,
	val title: String,
	val accountId: UUID,
	val createdAt: Instant,
)

@Revision("1.0")
data class ThreadUpdatedEvent(
	val threadId: UUID,
	val title: String,
	val accountId: UUID,
	val modifiedAt: Instant,
)

@Revision("1.0")
data class ThreadDeleteEvent(
	val threadId: UUID,
	val accountId: UUID,
	val deletedAt: Instant,
)

@Revision("1.0")
data class ModeratorAddedEvent(
	val moderatorId: UUID,
	val threadId: UUID,
	val accountId: UUID,
)

@Revision("1.0")
data class ModeratorRemovedEvent(
	val moderatorId: UUID,
	val threadId: UUID
)

@Revision("1.0")
data class AccountBannedEvent(
	val bannedUserId: UUID,
	val threadId: UUID,
	val accountId: UUID,
	val createdAt: Instant,
)

@Revision("1.0")
data class AccountUnbannedEvent(
	val bannedUserId: UUID,
	val threadId: UUID,
)
