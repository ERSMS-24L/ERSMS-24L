package pl.edu.pw.ia.shared.domain.exception

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
open class ForbiddenException(message: String) : RuntimeException(message)

class AccountMismatchException(currentAccountId: UUID, ownerAccountId: UUID) :
	ForbiddenException(
		"Account with id $currentAccountId cannot modify resources of account with id: $ownerAccountId"
	)
