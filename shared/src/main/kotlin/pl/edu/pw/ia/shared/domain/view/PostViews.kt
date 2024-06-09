package pl.edu.pw.ia.shared.domain.view

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID
import pl.edu.pw.ia.shared.domain.model.VoteType

data class PostView(
	val postId: UUID,
	val threadId: UUID,
	val accountId: UUID,
	val username: String,
	val content: String,
	@Parameter(schema = Schema(type = "number", format = "double"))
	val createdAt: Instant,
	val votes: Int = 0,
)

data class VoteView(
	val voteId: UUID,
	val postId: UUID,
	val accountId: UUID,
	val vote: VoteType,
)
