package pl.edu.pw.ia.shared.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
open class ClientRequestException(message: String? = null, cause: Throwable? = null) :
	RuntimeException(
		message?.let { "[ClientRequestException] $message" } ?: cause?.message,
		cause
	)

class MissingAccountIdException : ClientRequestException("Request is missing account id")
