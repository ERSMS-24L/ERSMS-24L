package pl.edu.pw.ia.shared.domain.view

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

data class ThreadView(
	val threadId: UUID,
	val accountId: UUID,
	val postId: UUID?,
	val username: String,
	val title: String,
	val post: String?,
	@Parameter(schema = Schema(type = "number", format = "double"))
	val lastModified: Instant
)

data class ModeratorView(
	val moderatorId: UUID,
	val threadId: UUID,
	val accountId: UUID,
	val username: String?
)

data class BannedUserView(
	val bannedUserId: UUID,
	val threadId: UUID,
	val accountId: UUID,
	val username: String?,
)
