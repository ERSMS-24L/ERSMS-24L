package pl.edu.pw.ia.shared.domain.view

import java.time.Instant
import java.util.UUID

data class PostView(
	val postId: UUID,
	val threadId: UUID,
	val accountId: UUID,
	val username: String,
	val content: String,
	val createdAt: Instant,
	val votes: Int = 0,
)

data class VoteView(
	val voteId: UUID,
	val postId: UUID,
	val accountId: UUID,
)
