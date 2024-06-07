package pl.edu.pw.ia.threads.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.Instant
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
import pl.edu.pw.ia.shared.domain.exception.ThreadNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindRecentThreadsQuery
import pl.edu.pw.ia.shared.domain.query.FindThreadByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindThreadsByAuthor
import pl.edu.pw.ia.shared.domain.query.FindThreadsByTitleQuery
import pl.edu.pw.ia.shared.domain.view.ThreadView
import pl.edu.pw.ia.shared.security.Scopes
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Tag(name = "Threads")
@ApiResponse(responseCode = "200", description = "Ok.")
@ApiResponse(
	responseCode = "404",
	description = "Thread not found.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface ThreadViewController {
	@Operation(description = "Find thread by id")
	fun findThreadById(@PathVariable threadId: UUID): Mono<ThreadView>

	@Operation(description = "Find thread by title")
	fun findThreadByTitle(
		@RequestParam title: String,
		@PageableDefault pageable: Pageable
	): Mono<Page<ThreadView>>

	@Operation(description = "Find thread by author")
	fun findThreadByAuthor(
		@RequestParam accountId: UUID,
		@PageableDefault pageable: Pageable
	): Mono<Page<ThreadView>>

	@Operation(description = "Find recent threads by date")
	fun findThreadsByDate(
		@RequestParam date: Instant,
		@PageableDefault pageable: Pageable
	): Mono<Page<ThreadView>>

	@Operation(description = "Get recent threads")
	fun findRecentThreads(
		@PageableDefault pageable: Pageable
	): Mono<Page<ThreadView>>

}

@RestController
@RequestMapping(
	value = ["/api/v1/threads"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ThreadViewControllerImpl(
	private val reactorQueryGateway: ReactorQueryGateway
) : ThreadViewController {

	@GetMapping("/{threadId}")
	@PreAuthorize("hasAnyAuthority('${Scopes.THREAD.READ}')")
	override fun findThreadById(@PathVariable threadId: UUID): Mono<ThreadView> {
		return reactorQueryGateway.query(
			FindThreadByIdQuery(threadId),
			ResponseTypes.instanceOf(ThreadView::class.java)
		).switchIfEmpty {
			Mono.error(ThreadNotFoundException(threadId))
		}
	}

	@GetMapping(params = ["title"])
	@PreAuthorize("hasAnyAuthority('${Scopes.THREAD.READ}')")
	override fun findThreadByTitle(
		@RequestParam(required = false) title: String,
		@PageableDefault pageable: Pageable
	): Mono<Page<ThreadView>> {
		return reactorQueryGateway.query(
			FindThreadsByTitleQuery(title, pageable),
			PageResponseType(ThreadView::class.java)
		)
	}

	@GetMapping(params = ["accountId"])
	@PreAuthorize("hasAnyAuthority('${Scopes.THREAD.READ}')")
	override fun findThreadByAuthor(
		@RequestParam(required = false) accountId: UUID,
		@PageableDefault pageable: Pageable
	): Mono<Page<ThreadView>> {
		return reactorQueryGateway.query(
			FindThreadsByAuthor(accountId, pageable),
			PageResponseType(ThreadView::class.java)
		)
	}

	@GetMapping(params = ["date"])
	@PreAuthorize("hasAnyAuthority('${Scopes.THREAD.READ}')")
	override fun findThreadsByDate(
		@RequestParam(required = false) date: Instant,
		@PageableDefault pageable: Pageable
	): Mono<Page<ThreadView>> {
		return reactorQueryGateway.query(
			FindRecentThreadsQuery(date, pageable),
			PageResponseType(ThreadView::class.java)
		)
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('${Scopes.THREAD.READ}')")
	override fun findRecentThreads(pageable: Pageable): Mono<Page<ThreadView>> {
		return reactorQueryGateway.query(
			FindRecentThreadsQuery(Instant.now(), pageable),
			PageResponseType(ThreadView::class.java)
		)
	}
}
