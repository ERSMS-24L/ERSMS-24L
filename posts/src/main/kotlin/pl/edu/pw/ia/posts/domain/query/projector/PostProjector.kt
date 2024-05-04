package pl.edu.pw.ia.posts.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import pl.edu.pw.ia.posts.domain.query.repository.PostViewRepository
import pl.edu.pw.ia.shared.domain.event.AccountUpdatedEvent
import pl.edu.pw.ia.shared.domain.event.PostCreatedEvent
import pl.edu.pw.ia.shared.domain.event.PostDeletedEvent
import pl.edu.pw.ia.shared.domain.event.PostUpdatedEvent
import pl.edu.pw.ia.shared.domain.event.VoteCreatedEvent
import pl.edu.pw.ia.shared.domain.event.VoteUpdatedEvent
import pl.edu.pw.ia.shared.domain.exception.PostNotFoundException
import pl.edu.pw.ia.shared.domain.model.VoteType
import pl.edu.pw.ia.shared.domain.query.FindAccountByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindPostByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindPostsByContentAndThreadIdQuery
import pl.edu.pw.ia.shared.domain.query.FindPostsByContentQuery
import pl.edu.pw.ia.shared.domain.query.FindPostsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.AccountView
import pl.edu.pw.ia.shared.domain.view.PostView

@Service
class PostProjector(
	private val repository: PostViewRepository,
	private val queryGateway: QueryGateway
) {
	@EventHandler
	fun on(event: PostCreatedEvent) {
		val username = queryGateway.query(
			FindAccountByIdQuery(accountId = event.accountId),
			AccountView::class.java
		).get().name
		val view = PostView(
			postId = event.postId,
			threadId = event.threadId,
			accountId = event.accountId,
			username = username,
			content = event.content,
			createdAt = event.createdAt,
			votes = 0
		)
		repository.save(view)
	}

	@EventHandler
	fun on(event: PostUpdatedEvent) {
		val view = repository.findById(event.postId) ?: throw PostNotFoundException(event.postId)
		repository.save(view.copy(content = event.content))
	}

	@EventHandler
	fun on(event: PostDeletedEvent) {
		repository.delete(event.postId)
	}

	@EventHandler
	fun on(event: AccountUpdatedEvent) {
		repository.findAllByAccountId(event.accountId)
			.map { it.copy(username = event.name) }
			.forEach { repository.save(it) }
	}

	@EventHandler
	fun on(event: VoteCreatedEvent) {
		val view = repository.findById(event.postId) ?: throw PostNotFoundException(event.postId)
		val newVotes = when (event.vote) {
			VoteType.UP_VOTE -> view.votes + 1
			VoteType.DOWN_VOTE -> view.votes - 1
			else -> view.votes
		}
		repository.save(view.copy(votes = newVotes))
	}

	@EventHandler
	fun on(event: VoteUpdatedEvent) {
		val view = repository.findById(event.postId) ?: throw PostNotFoundException(event.postId)
		val newVotes = when (event.previousVote) {
			VoteType.DOWN_VOTE ->
				when (event.vote) {
					VoteType.UP_VOTE -> view.votes + 2
					VoteType.NO_VOTE -> view.votes + 1
					else -> view.votes
				}

			VoteType.UP_VOTE ->
				when (event.vote) {
					VoteType.DOWN_VOTE -> view.votes - 2
					VoteType.NO_VOTE -> view.votes - 1
					else -> view.votes
				}

			VoteType.NO_VOTE ->
				when (event.vote) {
					VoteType.UP_VOTE -> view.votes + 1
					VoteType.DOWN_VOTE -> view.votes - 1
					else -> view.votes
				}
		}
		repository.save(view.copy(votes = newVotes))
	}

	@QueryHandler
	fun handle(query: FindPostByIdQuery): PostView =
		repository.findById(query.postId) ?: throw PostNotFoundException(query.postId)

	@QueryHandler
	fun handle(query: FindPostsByContentQuery): Page<PostView> =
		repository.findByContent(query.content, query.pageable)

	@QueryHandler
	fun handle(query: FindPostsByThreadIdQuery): Page<PostView> =
		repository.findByThreadId(query.threadId, query.pageable)

	@QueryHandler
	fun handle(query: FindPostsByContentAndThreadIdQuery): Page<PostView> =
		repository.findByContentAndThreadId(query.content, query.threadId, query.pageable)
}