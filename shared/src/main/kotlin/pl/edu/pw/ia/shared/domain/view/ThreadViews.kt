package pl.edu.pw.ia.shared.domain.view

import java.time.Instant
import java.util.UUID

data class ThreadView(
	val threadId: UUID,
	val accountId: UUID,
	val username: String,
	val title: String,
	val post: String,
	val lastModified: Instant
)

data class ModeratorView(
	val moderatorId: UUID,
	val threadId: UUID,
	val accountId: UUID,
)
