package pl.edu.pw.ia.shared.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
open class AlreadyExistsException(message: String? = null, cause: Throwable? = null) :
	RuntimeException(
		message?.let { "[AlreadyExistsException] $message" } ?: cause?.message,
		cause
	)
