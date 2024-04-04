package pl.edu.pw.ia.threads.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.event.ThreadCreatedEvent
import pl.edu.pw.ia.shared.domain.event.ThreadDeleteEvent
import pl.edu.pw.ia.shared.domain.event.ThreadUpdatedEvent
import pl.edu.pw.ia.shared.domain.exception.ThreadNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindAccountByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindRecentThreadsQuery
import pl.edu.pw.ia.shared.domain.query.FindThreadByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindThreadsByAuthor
import pl.edu.pw.ia.shared.domain.query.FindThreadsByTitleQuery
import pl.edu.pw.ia.shared.domain.view.AccountView
import pl.edu.pw.ia.shared.domain.view.ThreadView
import pl.edu.pw.ia.threads.domain.query.repository.ThreadViewRepository

@Service
class ThreadProjector(
	private val repository: ThreadViewRepository,
	private val queryGateway: QueryGateway
) {
	// TODO: Handle for posts, username change, etc
	@EventHandler
	fun on(event: ThreadCreatedEvent) {
		val username = queryGateway.query(
			FindAccountByIdQuery(accountId = event.accountId),
			AccountView::class.java
		).get().name
		val view = ThreadView(
			threadId = event.threadId,
			title = event.title,
			accountId = event.accountId,
			post = "",
			username = username,
			lastModified = event.createdAt
		)
		repository.save(view)
	}

	@EventHandler
	fun on(event: ThreadUpdatedEvent) {
		val view = repository.findById(event.threadId) ?: throw ThreadNotFoundException(event.threadId)
		repository.save(view.copy(title = event.title, lastModified = event.modifiedAt))
	}

	@EventHandler
	fun on(event: ThreadDeleteEvent) {
		repository.delete(event.threadId)
	}

	@QueryHandler
	fun handle(query: FindThreadByIdQuery): ThreadView =
		repository.findById(query.threadId) ?: throw ThreadNotFoundException(query.threadId)

	@QueryHandler
	fun handle(query: FindThreadsByTitleQuery): Page<ThreadView> =
		repository.findByTitle(query.title, query.pageable)

	@QueryHandler
	fun handle(query: FindThreadsByAuthor): Page<ThreadView> =
		repository.findByAuthor(query.accountId, query.pageable)

	@QueryHandler
	fun handle(query: FindRecentThreadsQuery): Page<ThreadView> =
		repository.findRecent(query.date, query.pageable)
}