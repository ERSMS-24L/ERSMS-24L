package pl.edu.pw.ia.posts.infrastructure.query.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pw.ia.shared.domain.model.VoteType
import pl.edu.pw.ia.shared.domain.view.VoteView

@Document
data class VoteViewEntity(
	@Id
	val voteId: String,
	val postId: String,
	val accountId: String,
	val vote: VoteType
) {
	fun toDomain(): VoteView =
		VoteView(
			voteId = UUID.fromString(voteId),
			postId = UUID.fromString(postId),
			accountId = UUID.fromString(accountId),
			vote = vote
		)

	companion object {
		fun VoteView.toEntity(): VoteViewEntity =
			VoteViewEntity(
				voteId = voteId.toString(),
				postId = postId.toString(),
				accountId = accountId.toString(),
				vote = vote
			)
	}
}