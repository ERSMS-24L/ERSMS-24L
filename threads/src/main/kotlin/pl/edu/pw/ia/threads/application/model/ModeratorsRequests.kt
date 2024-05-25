package pl.edu.pw.ia.threads.application.model

import java.util.UUID
import pl.edu.pw.ia.shared.domain.command.AddModeratorCommand
import pl.edu.pw.ia.shared.domain.command.RemoveModeratorCommand
import pl.edu.pw.ia.shared.security.SecurityContext

data class AddModeratorRequest(

	val threadId: UUID,
	val subjectAccountId: UUID,
) {
	fun toCommand(): AddModeratorCommand =
		AddModeratorCommand(
			threadId = threadId,
			subjectAccountId = subjectAccountId,
			moderatorId = UUID.randomUUID(),
			accountId = SecurityContext.getAccountId(),
		)
}

data class RemoveModeratorRequest(

	val moderatorId: UUID,
) {
	fun toCommand(): RemoveModeratorCommand =
		RemoveModeratorCommand(
			moderatorId = moderatorId,
			accountId = SecurityContext.getAccountId(),
		)
}