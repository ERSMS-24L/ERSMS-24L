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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.edu.pw.ia.posts.application.model.PostCreateRequest
import pl.edu.pw.ia.posts.application.model.PostDeleteRequest
import pl.edu.pw.ia.posts.application.model.PostUpdateRequest
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.application.model.IdResponse
import pl.edu.pw.ia.shared.security.Scopes
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
	fun createPost(@Valid request: PostCreateRequest): Mono<IdResponse>

	@Operation(description = "Update post")
	@ApiResponse(responseCode = "200", description = "Updated.")
	fun updatePost(@Valid request: PostUpdateRequest): Mono<IdResponse>

	@Operation(description = "Delete post")
	@ApiResponse(responseCode = "204", description = "Deleted.")
	fun deletePost(@Valid request: PostDeleteRequest): Mono<IdResponse>
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
	override fun createPost(request: PostCreateRequest): Mono<IdResponse> {
		val command = request.toCommand()
		return reactorCommandGateway.send<UUID>(command).map { IdResponse(id=it) }
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	override fun updatePost(request: PostUpdateRequest): Mono<IdResponse> {
		val command = request.toCommand()
		return reactorCommandGateway.send<UUID>(command).map { IdResponse(id=it) }
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	override fun deletePost(request: PostDeleteRequest): Mono<IdResponse> {
		val command = request.toCommand()
		return reactorCommandGateway.send<UUID>(command).map { IdResponse(id=it) }
	}
}