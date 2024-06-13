package pl.edu.pw.ia.threads.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.event.ModeratorAddedEvent
import pl.edu.pw.ia.shared.domain.event.ModeratorRemovedEvent
import pl.edu.pw.ia.shared.domain.query.FindAccountByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindModeratorByThreadAndAccountIdQuery
import pl.edu.pw.ia.shared.domain.query.FindModeratorsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.AccountView
import pl.edu.pw.ia.shared.domain.view.ModeratorView
import pl.edu.pw.ia.threads.domain.query.repository.ModeratorViewRepository

@Service
class ModeratorProjector(
	private val repository: ModeratorViewRepository,
) {
	@EventHandler
	fun on(event: ModeratorAddedEvent, reactorQueryGateway: ReactorQueryGateway) {
		val username = reactorQueryGateway.query(FindAccountByIdQuery(accountId = event.accountId), ResponseTypes.instanceOf(AccountView::class.java)).block()?.name
		val view = ModeratorView(
			moderatorId = event.moderatorId,
			threadId = event.threadId,
			accountId = event.accountId,
			username = username,
		)
		repository.save(view)
	}

	@EventHandler
	fun on(event: ModeratorRemovedEvent) {
		repository.delete(event.moderatorId)
	}

	@QueryHandler
	fun handle(query: FindModeratorByThreadAndAccountIdQuery): ModeratorView? =
		repository.findByAccountIdAndThreadId(query.accountId, query.threadId)

	@QueryHandler
	fun handle(query: FindModeratorsByThreadIdQuery): Page<ModeratorView> =
		repository.findByThreadId(query.threadId, query.pageable)
}