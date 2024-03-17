package pl.edu.pw.ia.shared.config

import org.axonframework.common.AxonException
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.reactive.function.server.ServerRequest
import pl.edu.pw.ia.shared.domain.exception.AlreadyExistsException
import pl.edu.pw.ia.shared.domain.exception.ClientRequestException
import pl.edu.pw.ia.shared.domain.exception.ForbiddenException
import pl.edu.pw.ia.shared.domain.exception.NotFoundException

@Configuration
class ErrorHandlerConfiguration {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	fun errorAttributes(): ErrorAttributes =
		GlobalErrorAttributes()
}

class GlobalErrorAttributes : DefaultErrorAttributes() {

	override fun getError(request: ServerRequest?): Throwable {
		val throwable = super.getError(request)
		if (throwable is AxonException) {
			val message = throwable.message
			return when {
				message == null -> throwable
				message.contains(NotFoundException::class.simpleName!!) -> NotFoundException(cause = throwable)
				message.contains(ClientRequestException::class.simpleName!!) -> ClientRequestException(cause = throwable)
				message.contains(ForbiddenException::class.simpleName!!) -> ForbiddenException(cause = throwable)
				message.contains(AlreadyExistsException::class.simpleName!!) -> AlreadyExistsException(cause = throwable)
				else -> throwable
			}
		}
		return throwable
	}
}
