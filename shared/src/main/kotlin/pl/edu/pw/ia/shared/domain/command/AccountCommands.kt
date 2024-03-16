package pl.edu.pw.ia.shared.domain.command

import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

// TODO: add support for:
//  - oauth2 id from provider

data class CreateAccountCommand(
	@TargetAggregateIdentifier val accountId: UUID,
	val name: String,
	val email: String,
)

data class UpdateAccountCommand(
	@TargetAggregateIdentifier val accountId: UUID,
	val name: String,
	val email: String,
)
