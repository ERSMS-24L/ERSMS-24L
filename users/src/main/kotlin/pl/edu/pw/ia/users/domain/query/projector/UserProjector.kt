package pl.edu.pw.ia.users.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.event.UserCreatedEvent
import pl.edu.pw.ia.shared.domain.query.FindUserByIdQuery
import pl.edu.pw.ia.users.domain.query.exception.UserNotFoundException
import pl.edu.pw.ia.users.domain.query.repository.UserViewRepository
import pl.edu.pw.ia.users.domain.query.view.UserView

@Service
class UserProjector(
	private val repository: UserViewRepository,
) {

	@EventHandler
	fun on(event: UserCreatedEvent) {
		val view = UserView(
			id = event.userId,
			name = event.name,
			email = event.email,
		)
		repository.save(view)
	}

	@QueryHandler
	fun handle(query: FindUserByIdQuery): UserView =
		repository.findById(query.userId) ?: throw UserNotFoundException(query.userId)
}
