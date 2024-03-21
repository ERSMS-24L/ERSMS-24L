package pl.edu.pw.ia.accounts.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import java.util.UUID
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.edu.pw.ia.accounts.application.model.CreateAccountRequest
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.security.Scopes
import reactor.core.publisher.Mono

@Tag(name = "Accounts")
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface AccountController {

	@Operation(description = "Create account")
	@ApiResponse(responseCode = "201", description = "Ok.")
	fun createAccount(@Valid request: CreateAccountRequest): Mono<UUID>
}

@Validated
@RestController
@RequestMapping(
	value = ["/api/v1/accounts"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class AccountControllerImpl(
	private val reactorCommandGateway: ReactorCommandGateway,
) : AccountController {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyAuthority('${Scopes.USER.WRITE}')")
	override fun createAccount(
		@RequestBody request: CreateAccountRequest
	): Mono<UUID> {
		val command = request.toCommand()
		return reactorCommandGateway.send(command)
	}
}
