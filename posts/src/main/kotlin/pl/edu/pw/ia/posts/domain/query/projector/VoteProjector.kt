package pl.edu.pw.ia.posts.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service
import pl.edu.pw.ia.posts.domain.query.repository.VoteViewRepository
import pl.edu.pw.ia.shared.domain.event.VoteCreatedEvent
import pl.edu.pw.ia.shared.domain.event.VoteUpdatedEvent
import pl.edu.pw.ia.shared.domain.query.FindVoteByAccountAndPostIdsQuery
import pl.edu.pw.ia.shared.domain.view.VoteView

@Service
class VoteProjector(
	private val repository: VoteViewRepository,
) {
	@EventHandler
	fun on(event: VoteCreatedEvent) {
		val view = VoteView(
			voteId = event.voteId,
			postId = event.postId,
			accountId = event.accountId,
			vote = event.vote
		)
		repository.save(view)
	}

	@EventHandler
	fun on(event: VoteUpdatedEvent) {
		val view = VoteView(
			voteId = event.voteId,
			postId = event.postId,
			accountId = event.accountId,
			vote = event.vote
		)
		repository.save(view)
	}

	@QueryHandler
	fun handle(query: FindVoteByAccountAndPostIdsQuery): VoteView? =
		repository.findByAccountIdAndPostId(query.postId, query.accountId)
}
