package pl.edu.pw.ia.threads.application.model

import java.util.UUID
import pl.edu.pw.ia.shared.domain.command.BanAccountCommand
import pl.edu.pw.ia.shared.domain.command.UnbanAccountCommand

data class BanUserRequest(

	val threadId: UUID,
	val subjectAccountId: UUID,
) {
	fun toCommand(accountId: UUID): BanAccountCommand =
		BanAccountCommand(
			bannedUserId = UUID.randomUUID(),
			threadId = threadId,
			subjectAccountId = subjectAccountId,
			accountId = accountId,
		)
}

data class UnbanUserRequest(
	val bannedUserId: UUID
) {
	fun toCommand(accountId: UUID): UnbanAccountCommand =
		UnbanAccountCommand(
			bannedUserId = bannedUserId,
			accountId = accountId,
		)
}
