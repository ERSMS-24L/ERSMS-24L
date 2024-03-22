package pl.edu.pw.ia.shared.domain.view

import java.util.UUID

data class ThreadView(
	val threadId: UUID,
	val accountId: UUID,
	val username: String,
	val title: String,
	val post: String,
)

data class ModeratorView(
	val threadId: UUID,
	val accountId: UUID,
)
