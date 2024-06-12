package pl.edu.pw.ia.shared.domain.command

import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier
import pl.edu.pw.ia.shared.domain.model.VoteType

data class CreatePostCommand(
	@TargetAggregateIdentifier val postId: UUID,
	val accountId: UUID,
	val content: String,
	val threadId: UUID,
)

data class UpdatePostCommand(
	@TargetAggregateIdentifier val postId: UUID,
	val accountId: UUID,
	val content: String,
)

data class DeletePostCommand(
	@TargetAggregateIdentifier val postId: UUID,
	val accountId: UUID,
)

data class DeletePostByAdminCommand(
	@TargetAggregateIdentifier val postId: UUID,
	val accountId: UUID,
)

data class CreateVoteCommand(
	@TargetAggregateIdentifier val voteId: UUID,
	val postId: UUID,
	val accountId: UUID,
	val vote: VoteType,
)

data class UpdateVoteCommand(
	@TargetAggregateIdentifier val voteId: UUID,
	val postId: UUID,
	val accountId: UUID,
	val vote: VoteType,
)
