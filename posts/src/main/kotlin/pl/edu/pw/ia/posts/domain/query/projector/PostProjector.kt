package pl.edu.pw.ia.posts.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import pl.edu.pw.ia.posts.domain.query.repository.PostViewRepository
import pl.edu.pw.ia.shared.domain.event.PostCreatedEvent
import pl.edu.pw.ia.shared.domain.event.PostDeletedEvent
import pl.edu.pw.ia.shared.domain.event.PostUpdatedEvent
import pl.edu.pw.ia.shared.domain.exception.PostNotFoundException
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