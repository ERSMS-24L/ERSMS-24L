package pl.edu.pw.ia.posts.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import java.util.UUID
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import pl.edu.pw.ia.posts.application.model.PostCreateRequest
import pl.edu.pw.ia.posts.application.model.PostDeleteRequest
import pl.edu.pw.ia.posts.application.model.PostUpdateRequest
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.application.model.IdResponse
import pl.edu.pw.ia.shared.security.Scopes
import pl.edu.pw.ia.shared.security.getAccountId
import reactor.core.publisher.Mono

@Tag(name = "Posts")
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface PostController {
	@Operation(description = "Create post")
	@ApiResponse(responseCode = "201", description = "Created.")
	fun createPost(
		@Valid request: PostCreateRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse>

	@Operation(description = "Update post")
	@ApiResponse(responseCode = "200", description = "Updated.")
	fun updatePost(
		@Valid request: PostUpdateRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse>

	@Operation(description = "Delete post")
	@ApiResponse(responseCode = "200", description = "Deleted.")
	fun deletePost(
		@Valid request: PostDeleteRequest,
		webExchange: ServerWebExchange,
		authentication: Authentication,
	): Mono<IdResponse>
}

@Validated
@RestController
@RequestMapping(
	value = ["/api/v1/posts"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class PostControllerImpl(
	private val reactorCommandGateway: ReactorCommandGateway,
) : PostController {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyAuthority('${Scopes.POST.WRITE}')")
	override fun createPost(
		@RequestBody request: PostCreateRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse> {
		val command = request.toCommand(webExchange.getAccountId())
		return reactorCommandGateway.send<UUID>(command).map { IdResponse(id = it) }
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	override fun updatePost(
		@RequestBody request: PostUpdateRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse> {
		val command = request.toCommand(webExchange.getAccountId())
		return reactorCommandGateway.send<UUID>(command)
			.defaultIfEmpty(command.postId)
			.map { IdResponse(id = it) }
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	override fun deletePost(
		@RequestBody request: PostDeleteRequest,
		webExchange: ServerWebExchange,
		authentication: Authentication,
	): Mono<IdResponse> {
		val response: Mono<IdResponse>
		if(authentication.authorities.map { it.toString() }.contains("forumAdmin")){
			val command = request.toAdminCommand(webExchange.getAccountId())
			response = reactorCommandGateway.send<UUID>(command).defaultIfEmpty(command.postId).map { IdResponse(id=it) }
		} else {
			val command = request.toCommand(webExchange.getAccountId())
			response = reactorCommandGateway.send<UUID>(command)
				.defaultIfEmpty(command.postId)
				.map { IdResponse(id = it) }
		}
		return response
	}
}
