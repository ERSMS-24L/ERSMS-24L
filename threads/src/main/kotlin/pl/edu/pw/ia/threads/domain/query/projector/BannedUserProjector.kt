package pl.edu.pw.ia.threads.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import pl.edu.pw.ia.shared.domain.event.AccountBannedEvent
import pl.edu.pw.ia.shared.domain.event.AccountUnbannedEvent
import pl.edu.pw.ia.shared.domain.exception.BannedUserNotFoundException
import pl.edu.pw.ia.shared.domain.exception.NotFoundException
import pl.edu.pw.ia.shared.domain.query.FindBannedAccountByThreadAndAccountIdsQuery
import pl.edu.pw.ia.shared.domain.query.FindBannedAccountsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.BannedUserView
import pl.edu.pw.ia.threads.domain.query.repository.BannedUserViewRepository

class BannedUserProjector(
	private val repository: BannedUserViewRepository,
	private val queryGateway: QueryGateway
) {

	@EventHandler
	fun on(event: AccountBannedEvent) {
		val view = BannedUserView(
			event.bannedUserId,
			event.threadId,
			event.accountId
		)
		repository.save(view)
	}

	@EventHandler
	fun on(event: AccountUnbannedEvent) {
		repository.delete(event.bannedUserId)
	}

	@QueryHandler
	fun handle(query: FindBannedAccountByThreadAndAccountIdsQuery): BannedUserView =
		repository.findByAccountIdAndThreadId(query.accountId, query.threadId) ?: throw BannedUserNotFoundException(query.accountId, query.threadId)

	@QueryHandler
	fun handle(query: FindBannedAccountsByThreadIdQuery): Page<BannedUserView> =
		repository.findByThreadId(query.threadId, query.pageable)
}