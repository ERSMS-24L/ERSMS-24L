package pl.edu.pw.ia.threads.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import pl.edu.pw.ia.shared.domain.event.ModeratorAddedEvent
import pl.edu.pw.ia.shared.domain.event.ModeratorRemovedEvent
import pl.edu.pw.ia.shared.domain.exception.ModeratorNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindModeratorByThreadAndAccountIdQuery
import pl.edu.pw.ia.shared.domain.query.FindModeratorsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.ModeratorView
import pl.edu.pw.ia.threads.domain.query.repository.ModeratorViewRepository

class ModeratorProjector(
	private val repository: ModeratorViewRepository,
	private val queryGateway: QueryGateway
) {
	@EventHandler
	fun on(event: ModeratorAddedEvent) {
		val view = ModeratorView(
			moderatorId = event.moderatorId,
			threadId = event.threadId,
			accountId = event.accountId
		)
		repository.save(view)
	}

	@EventHandler
	fun on(event: ModeratorRemovedEvent) {
		repository.delete(event.moderatorId)
	}

	@QueryHandler
	fun handle(query: FindModeratorByThreadAndAccountIdQuery): ModeratorView =
		repository.findByAccountIdAndThreadId(query.accountId, query.threadId) ?: throw ModeratorNotFoundException(query.accountId, query.threadId)

	@QueryHandler
	fun handle(query: FindModeratorsByThreadIdQuery): Page<ModeratorView> =
		repository.findByThreadId(query.threadId, query.pageable)
}