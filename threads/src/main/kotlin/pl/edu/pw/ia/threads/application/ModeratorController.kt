package pl.edu.pw.ia.threads.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.application.model.IdResponse
import pl.edu.pw.ia.shared.security.Scopes
import pl.edu.pw.ia.shared.security.getAccountId
import pl.edu.pw.ia.threads.application.model.AddModeratorRequest
import pl.edu.pw.ia.threads.application.model.RemoveModeratorRequest
import reactor.core.publisher.Mono

@Tag(name = "Moderators")
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface ModeratorController {

	@Operation(description = "Add moderator to thread")
	@ApiResponse(responseCode = "201", description = "Ok.")
	fun addModerator(
		request: AddModeratorRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse>

	@Operation(description = "Remove moderator from thread")
	@ApiResponse(responseCode = "201", description = "Ok.")
	fun removeModerator(
		request: RemoveModeratorRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse>
}

@Validated
@RestController
@RequestMapping(
	value = ["/api/v1/moderators"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ModeratorControllerImpl(
	private val reactorCommandGateway: ReactorCommandGateway,
) : ModeratorController {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyAuthority('${Scopes.MODERATOR.WRITE}')")
	override fun addModerator(
		@RequestBody request: AddModeratorRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse> {
		val command = request.toCommand(webExchange.getAccountId())
		return reactorCommandGateway.send<UUID>(command).map { IdResponse(id = it) }
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	override fun removeModerator(
		@RequestBody request: RemoveModeratorRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse> {
		val command = request.toCommand(webExchange.getAccountId())
		return reactorCommandGateway.send<UUID>(command)
			.defaultIfEmpty(command.moderatorId)
			.map { IdResponse(id = it) }
	}
}
