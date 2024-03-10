package pl.edu.pw.ia.users.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.domain.query.FindUserByIdQuery
import pl.edu.pw.ia.shared.security.Scopes
import pl.edu.pw.ia.shared.security.getUserId
import pl.edu.pw.ia.users.domain.query.view.UserView
import reactor.core.publisher.Mono

@Tag(name = "Users")
@ApiResponse(responseCode = "200", description = "Ok.")
@ApiResponse(
	responseCode = "404",
	description = "Author not found.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface UserViewController {

	@Operation(description = "Find user by identifier")
	fun findUser(@PathVariable userId: UUID): Mono<UserView>

	@Operation(description = "Find current user info")
	fun findCurrentUser(exchange: ServerWebExchange): Mono<UserView>
}

@RestController
@RequestMapping(
	value = ["/api/v1/users"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class UserViewControllerImpl(
	private val reactorQueryGateway: ReactorQueryGateway
) : UserViewController {

	@GetMapping("/{userId}")
	@PreAuthorize("hasAnyAuthority('${Scopes.USER.READ}')")
	override fun findUser(@PathVariable userId: UUID): Mono<UserView> {
		return reactorQueryGateway.query(
			FindUserByIdQuery(userId),
			ResponseTypes.instanceOf(UserView::class.java)
		)
	}

	@GetMapping("/me")
	@PreAuthorize("hasAnyAuthority('${Scopes.USER.READ}')")
	override fun findCurrentUser(exchange: ServerWebExchange): Mono<UserView> {
		return reactorQueryGateway.query(
			FindUserByIdQuery(exchange.getUserId()),
			ResponseTypes.instanceOf(UserView::class.java)
		)
	}
}
