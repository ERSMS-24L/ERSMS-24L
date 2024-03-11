package pl.edu.pw.ia.users.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import java.util.UUID
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.springframework.core.convert.ConversionService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.domain.command.CreateUserCommand
import pl.edu.pw.ia.shared.security.Scopes
import pl.edu.pw.ia.users.application.model.CreateUserRequest
import reactor.core.publisher.Mono

@Tag(name = "Users")
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface UserController {

	@Operation(description = "Create user")
	@ApiResponse(responseCode = "201", description = "Ok.")
	fun createUser(request: CreateUserRequest): Mono<UUID>
}

@Validated
@RestController
@RequestMapping(
	value = ["/api/v1/users"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class UserControllerImpl(
	private val reactorCommandGateway: ReactorCommandGateway,
	private val conversionService: ConversionService,
) : UserController {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyAuthority('${Scopes.USER.WRITE}')")
	override fun createUser(
		@Valid @RequestBody request: CreateUserRequest
	): Mono<UUID> {
		val command = conversionService.convert(request, CreateUserCommand::class.java)
		return reactorCommandGateway.send(command)
	}
}
