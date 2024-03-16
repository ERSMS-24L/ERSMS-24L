package pl.edu.pw.ia.shared.domain.event

import java.time.Instant
import java.util.UUID
import org.axonframework.serialization.Revision
import pl.edu.pw.ia.shared.domain.model.VoteType

@Revision("1.0")
data class PostCreatedEvent(
	val postId: UUID,
	val accountId: UUID,
	val content: String,
	val createdAt: Instant,
)

@Revision("1.0")
data class PostUpdatedEvent(
	val postId: UUID,
	val accountId: UUID,
	val content: String,
)

@Revision("1.0")
data class PostDeletedEvent(
	val postId: UUID,
	val accountId: UUID,
)

@Revision("1.0")
data class VoteCreatedEvent(
	val voteId: UUID,
	val postId: UUID,
	val accountId: UUID,
	val vote: VoteType,
)

@Revision("1.0")
data class VoteUpdatedEvent(
	val voteId: UUID,
	val postId: UUID,
	val accountId: UUID,
	val previousVote: VoteType,
	val vote: VoteType,
)
