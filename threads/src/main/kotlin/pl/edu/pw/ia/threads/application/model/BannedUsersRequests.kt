package pl.edu.pw.ia.threads.application.model

import java.util.UUID
import pl.edu.pw.ia.shared.domain.command.BanAccountCommand
import pl.edu.pw.ia.shared.domain.command.UnbanAccountCommand
import pl.edu.pw.ia.shared.security.SecurityContext

data class BanUserRequest(

	val threadId: UUID,
	val subjectAccountId: UUID,
) {
	fun toCommand(): BanAccountCommand =
		BanAccountCommand(
			bannedUserId = UUID.randomUUID(),
			threadId = threadId,
			subjectAccountId = subjectAccountId,
			accountId = SecurityContext.getAccountId(),
		)
}

data class UnbanUserRequest(
	val bannedUserId: UUID
) {
	fun toCommand(): UnbanAccountCommand =
		UnbanAccountCommand(
			bannedUserId = bannedUserId,
			accountId = SecurityContext.getAccountId(),
		)
}