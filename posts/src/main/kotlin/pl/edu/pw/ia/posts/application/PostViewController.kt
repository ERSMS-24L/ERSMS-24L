package pl.edu.pw.ia.posts.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.config.PageResponseType
import pl.edu.pw.ia.shared.domain.exception.PostNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindPostByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindPostsByContentAndThreadIdQuery
import pl.edu.pw.ia.shared.domain.query.FindPostsByContentQuery
import pl.edu.pw.ia.shared.domain.query.FindPostsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.PostView
import pl.edu.pw.ia.shared.security.Scopes
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Tag(name = "Posts")
@ApiResponse(responseCode = "200", description = "Ok.")
@ApiResponse(
	responseCode = "404",
	description = "Post not found.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface PostViewController {
	@Operation(description = "Find post by id")
	fun findPostById(@PathVariable postId: UUID): Mono<PostView>

	@Operation(description = "Find posts by thread id")
	fun findPostsByThreadId(@RequestParam threadId: UUID, @PageableDefault pageable: Pageable): Mono<Page<PostView>>

	@Operation(description = "Find posts by matching content.")
	fun findPostsByContent(@RequestParam content: String, @PageableDefault pageable: Pageable): Mono<Page<PostView>>

	@Operation(description = "Find posts by matching content and thread id.")
	fun findPostsByContentAndThreadId(
		@RequestParam content: String,
		@RequestParam threadId: UUID,
		@PageableDefault pageable: Pageable
	): Mono<Page<PostView>>

}

@RestController
@RequestMapping(
	value = ["/api/v1/posts"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class PostViewControllerImpl(
	private val reactorQueryGateway: ReactorQueryGateway
) : PostViewController {

	@GetMapping("/{postId}")
	@PreAuthorize("hasAnyAuthority('${Scopes.POST.READ}')")
	override fun findPostById(@PathVariable postId: UUID): Mono<PostView> {
		return reactorQueryGateway.query(
			FindPostByIdQuery(postId),
			ResponseTypes.instanceOf(PostView::class.java)
		).switchIfEmpty {
			Mono.error(PostNotFoundException(postId))
		}
	}

	@GetMapping(params = ["threadId"])
	@PreAuthorize("hasAnyAuthority('${Scopes.POST.READ}')")
	override fun findPostsByThreadId(
		@RequestParam(required = true) threadId: UUID,
		@PageableDefault pageable: Pageable
	): Mono<Page<PostView>> {
		return reactorQueryGateway.query(
			FindPostsByThreadIdQuery(threadId, pageable),
			PageResponseType(PostView::class.java)
		)
	}

	@GetMapping(params = ["content"])
	@PreAuthorize("hasAnyAuthority('${Scopes.POST.READ}')")
	override fun findPostsByContent(
		@RequestParam(required = false) content: String,
		@PageableDefault pageable: Pageable
	): Mono<Page<PostView>> {
		return reactorQueryGateway.query(
			FindPostsByContentQuery(content, pageable),
			PageResponseType(PostView::class.java)
		)
	}

	@GetMapping(params = ["content", "threadId"])
	@PreAuthorize("hasAnyAuthority('${Scopes.POST.READ}')")
	override fun findPostsByContentAndThreadId(
		@RequestParam(required = false) content: String,
		@RequestParam(required = true) threadId: UUID,
		@PageableDefault pageable: Pageable
	): Mono<Page<PostView>> {
		return reactorQueryGateway.query(
			FindPostsByContentAndThreadIdQuery(content, threadId, pageable),
			PageResponseType(PostView::class.java)
		)
	}
}
