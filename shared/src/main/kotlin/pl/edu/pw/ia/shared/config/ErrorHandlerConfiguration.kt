package pl.edu.pw.ia.shared.config

import org.axonframework.common.AxonException
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.reactive.function.server.ServerRequest

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
			return throwable.cause ?: throwable
		}
		return throwable
	}
}
