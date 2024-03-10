package pl.edu.pw.ia.shared.application.exception

import java.util.Date
import org.springframework.validation.ObjectError

data class ApiErrorResponse(
	val timestamp: Date = Date(),
	val status: Int? = null,
	val error: String? = null,
	val exception: String? = null,
	val message: String? = null,
	val errors: List<ObjectError> = listOf(),
	val trace: String? = null,
	val path: String? = null,
	val requestId: String? = null,
)
