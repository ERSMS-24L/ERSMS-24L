package pl.edu.pw.ia.accounts.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service
import pl.edu.pw.ia.accounts.domain.query.repository.AccountViewRepository
import pl.edu.pw.ia.shared.domain.event.AccountCreatedEvent
import pl.edu.pw.ia.shared.domain.query.FindAccountByIdQuery
import pl.edu.pw.ia.shared.domain.view.AccountView

@Service
class AccountProjector(
	private val repository: AccountViewRepository,
) {

	@EventHandler
	fun on(event: AccountCreatedEvent) {
		val view = AccountView(
			accountId = event.accountId,
			name = event.name,
			email = event.email,
		)
		repository.save(view)
	}

	@QueryHandler
	fun handle(query: FindAccountByIdQuery): AccountView? =
		repository.findById(query.accountId)
}
