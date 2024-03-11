package pl.edu.pw.ia.shared.domain.command

import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateUserCommand(
		@TargetAggregateIdentifier val userId: UUID,
		val name: String,
		val email: String,
) {
	override fun toString(): String {
		return "CreateUserCommand(userId=$userId, name='$name', email=**********)"
	}
}
