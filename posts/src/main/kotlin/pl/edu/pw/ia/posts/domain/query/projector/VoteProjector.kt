package pl.edu.pw.ia.posts.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service
import pl.edu.pw.ia.posts.domain.aggregate.Vote
import pl.edu.pw.ia.posts.domain.query.repository.VoteViewRepository
import pl.edu.pw.ia.shared.domain.event.VoteCreatedEvent
import pl.edu.pw.ia.shared.domain.event.VoteUpdatedEvent
import pl.edu.pw.ia.shared.domain.exception.VoteNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindVoteByAccountAndPostIdsQuery
import pl.edu.pw.ia.shared.domain.view.VoteView

@Service
class VoteProjector(
	private val repository: VoteViewRepository,
	private val queryGateway: QueryGateway
)
{
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
	fun handle(query: FindVoteByAccountAndPostIdsQuery): VoteView =
		repository.findByAccountIdAndPostId(query.postId, query.accountId) ?: throw VoteNotFoundException(query.postId, query.accountId)
}