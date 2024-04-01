package pl.edu.pw.ia.shared.domain.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class CreateThreadCommand(
	@TargetAggregateIdentifier val threadId: UUID,
	val title: String,
	val accountId: UUID,
)

data class UpdateThreadCommand(
	@TargetAggregateIdentifier val threadId: UUID,
	val title: String,
	val accountId: UUID,
)

data class DeleteThreadCommand(
	@TargetAggregateIdentifier val threadId: UUID,
	val accountId: UUID,
)

data class AddModeratorCommand(
	val threadId: UUID,
	val accountId: UUID,
	val subjectAccountId: UUID,
)

data class RemoveModeratorCommand(
	val threadId: UUID,
	val accountId: UUID,
	val subjectAccountId: UUID,
)

data class BanAccountCommand(
	val threadId: UUID,
	val accountId: UUID,
	val subjectAccountId: UUID,
)

data class UnbanAccountCommand(
	val threadId: UUID,
	val accountId: UUID,
	val subjectAccountId: UUID,
)
