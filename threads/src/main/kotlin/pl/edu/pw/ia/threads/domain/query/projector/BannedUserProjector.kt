package pl.edu.pw.ia.threads.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.event.AccountBannedEvent
import pl.edu.pw.ia.shared.domain.event.AccountUnbannedEvent
import pl.edu.pw.ia.shared.domain.query.FindAccountByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindBannedAccountByThreadAndAccountIdsQuery
import pl.edu.pw.ia.shared.domain.query.FindBannedAccountsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.AccountView
import pl.edu.pw.ia.shared.domain.view.BannedUserView
import pl.edu.pw.ia.threads.domain.query.repository.BannedUserViewRepository

@Service
class BannedUserProjector(
	private val repository: BannedUserViewRepository
) {

	@EventHandler
	fun on(event: AccountBannedEvent, reactorQueryGateway: ReactorQueryGateway) {
		val username = reactorQueryGateway.query(FindAccountByIdQuery(accountId = event.accountId), ResponseTypes.instanceOf(AccountView::class.java)).block()?.name
		val view = BannedUserView(
			event.bannedUserId,
			event.threadId,
			event.accountId,
			username = username,
		)
		repository.save(view)
	}

	@EventHandler
	fun on(event: AccountUnbannedEvent) {
		repository.delete(event.bannedUserId)
	}

	@QueryHandler
	fun handle(query: FindBannedAccountByThreadAndAccountIdsQuery): BannedUserView? =
		repository.findByAccountIdAndThreadId(query.accountId, query.threadId)

	@QueryHandler
	fun handle(query: FindBannedAccountsByThreadIdQuery): Page<BannedUserView> =
		repository.findByThreadId(query.threadId, query.pageable)
}