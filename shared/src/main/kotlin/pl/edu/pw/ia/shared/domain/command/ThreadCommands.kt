package pl.edu.pw.ia.shared.domain.command

import java.util.UUID

data class CreateThreadCommand(
	val threadId: UUID,
	val title: String,
	val accountId: UUID,
)

data class UpdateThreadCommand(
	val threadId: UUID,
	val title: String,
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
