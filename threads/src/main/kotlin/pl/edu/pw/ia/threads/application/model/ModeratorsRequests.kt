package pl.edu.pw.ia.threads.application.model

import java.util.UUID
import pl.edu.pw.ia.shared.domain.command.AddModeratorCommand
import pl.edu.pw.ia.shared.domain.command.RemoveModeratorCommand

data class AddModeratorRequest(
	val threadId: UUID,
	val subjectAccountId: UUID,
) {
	fun toCommand(accountId: UUID): AddModeratorCommand =
		AddModeratorCommand(
			threadId = threadId,
			subjectAccountId = subjectAccountId,
			moderatorId = UUID.randomUUID(),
			accountId = accountId,
		)
}

data class RemoveModeratorRequest(
	val moderatorId: UUID,
) {
	fun toCommand(accountId: UUID): RemoveModeratorCommand =
		RemoveModeratorCommand(
			moderatorId = moderatorId,
			accountId = accountId,
		)
}
