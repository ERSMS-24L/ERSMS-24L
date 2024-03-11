package pl.edu.pw.ia.users.domain.query.exception

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(
	code = HttpStatus.NOT_FOUND,
	reason = "User with such id was not found"
)
class UserNotFoundException(userId: UUID) : RuntimeException("Could not find user with id: $userId")
