package pl.edu.pw.ia.posts.domain.query.projector

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service
import pl.edu.pw.ia.posts.domain.query.repository.PostViewRepository
import pl.edu.pw.ia.shared.domain.event.PostCreatedEvent

@Service
class PostProjector(
	private val repository: PostViewRepository,
	private val queryGateway: QueryGateway
) {
	@EventHandler
	fun on(event: PostCreatedEvent) {

	}
}