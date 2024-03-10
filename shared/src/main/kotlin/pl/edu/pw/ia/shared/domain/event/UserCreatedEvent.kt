package pl.edu.pw.ia.shared.domain.event

import java.util.UUID
import org.axonframework.serialization.Revision

@Revision("1.0")
data class UserCreatedEvent(
		val userId: UUID,
		val name: String,
		val email: String,
)
