package pl.edu.pw.ia.shared.domain.command

import io.swagger.v3.oas.annotations.tags.Tag
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
	@TargetAggregateIdentifier val moderatorId: UUID,
	val threadId: UUID,
	val accountId: UUID,
	val subjectAccountId: UUID,
)

data class RemoveModeratorCommand(
	@TargetAggregateIdentifier val moderatorId: UUID,
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
