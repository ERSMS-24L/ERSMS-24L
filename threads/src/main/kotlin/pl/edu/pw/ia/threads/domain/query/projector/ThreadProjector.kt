package pl.edu.pw.ia.threads.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import pl.edu.pw.ia.shared.domain.event.ThreadCreatedEvent
import pl.edu.pw.ia.shared.domain.event.ThreadDeleteEvent
import pl.edu.pw.ia.shared.domain.event.ThreadUpdatedEvent
import pl.edu.pw.ia.shared.domain.exception.ThreadNotFoundException
import pl.edu.pw.ia.shared.domain.query.*
import pl.edu.pw.ia.shared.domain.view.AccountView
import pl.edu.pw.ia.shared.domain.view.ThreadView
import pl.edu.pw.ia.threads.domain.query.repository.ThreadViewRepository

class ThreadProjector(
    private val repository: ThreadViewRepository,
    private val queryGateway: QueryGateway
) {

    @EventHandler
    fun on(event: ThreadCreatedEvent) {
        val view = ThreadView(
            threadId = event.threadId,
            title = event.title,
            accountId = event.accountId,
            post = "",
            username = queryGateway.query(FindAccountByIdQuery(accountId = event.accountId), AccountView::class.java).get().name
        )
        repository.save(view)
    }

    @EventHandler
    fun on(event: ThreadUpdatedEvent) {
        val view = repository.findById(event.threadId) ?: throw ThreadNotFoundException(event.threadId)
        repository.save(view.copy(title = event.title))
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