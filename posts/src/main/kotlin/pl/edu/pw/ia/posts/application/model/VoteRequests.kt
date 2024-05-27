package pl.edu.pw.ia.posts.application.model

import java.util.UUID
import pl.edu.pw.ia.shared.domain.command.CreateVoteCommand
import pl.edu.pw.ia.shared.domain.command.UpdateVoteCommand
import pl.edu.pw.ia.shared.domain.model.VoteType

data class CreateVoteRequest(
	val postId: UUID,
	val vote: VoteType,
) {
	fun toCommand(accountId: UUID): CreateVoteCommand =
		CreateVoteCommand(
			voteId = UUID.randomUUID(),
			postId = postId,
			accountId = accountId,
			vote = vote,
		)
}

data class UpdateVoteRequest(
	val voteId: UUID,
	val postId: UUID,
	val vote: VoteType,
) {
	fun toCommand(accountId: UUID): UpdateVoteCommand =
		UpdateVoteCommand(
			voteId = voteId,
			postId = postId, // TODO: reconsider passing only voteId
			accountId = accountId,
			vote = vote,
		)
}
