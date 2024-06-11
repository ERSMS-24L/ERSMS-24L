package pl.edu.pw.ia.threads.application

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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.config.PageResponseType
import pl.edu.pw.ia.shared.domain.exception.ModeratorNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindModeratorByThreadAndAccountIdQuery
import pl.edu.pw.ia.shared.domain.query.FindModeratorsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.ModeratorView
import pl.edu.pw.ia.shared.security.Scopes
import pl.edu.pw.ia.shared.security.getAccountId
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Tag(name = "Moderators")
@ApiResponse(responseCode = "200", description = "Ok.")
@ApiResponse(
	responseCode = "404",
	description = "Moderator not found.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface ModeratorViewController {

	@Operation(description = "Find moderator by account id and thread id")
	fun amIModeratorByThreadId(
		@RequestParam threadId: UUID,
		webExchange: ServerWebExchange
	): Mono<ModeratorView>

	@Operation(description = "Find moderator by account id and thread id")
	fun findModeratorByAccountIdAndThreadId(
		@RequestParam accountId: UUID,
		@RequestParam threadId: UUID
	): Mono<ModeratorView>

	@Operation(description = "Find moderators by thread id")
	fun findModeratorByThreadId(
		@RequestParam threadId: UUID,
		@PageableDefault(page = 0) pageable: Pageable
	): Mono<Page<ModeratorView>>
}

@RestController
@RequestMapping(
	value = ["/api/v1/moderators"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ModeratorViewControllerImpl(
	private val reactorQueryGateway: ReactorQueryGateway
) : ModeratorViewController {

	@GetMapping("/me")
	@PreAuthorize("hasAnyAuthority('${Scopes.MODERATOR.READ}')")
	override fun amIModeratorByThreadId(threadId: UUID, webExchange: ServerWebExchange): Mono<ModeratorView> =
		findModeratorByAccountIdAndThreadId(webExchange.getAccountId(), threadId)

	@GetMapping(params = ["accountId", "threadId"])
	@PreAuthorize("hasAnyAuthority('${Scopes.MODERATOR.READ}')")
	override fun findModeratorByAccountIdAndThreadId(accountId: UUID, threadId: UUID): Mono<ModeratorView> {
		return reactorQueryGateway.query(
			FindModeratorByThreadAndAccountIdQuery(threadId = threadId, accountId = accountId),
			ResponseTypes.instanceOf(ModeratorView::class.java)
		).switchIfEmpty {
			Mono.error(ModeratorNotFoundException(accountId, threadId))
		}
	}

	@GetMapping(params = ["threadId"])
	@PreAuthorize("hasAnyAuthority('${Scopes.MODERATOR.READ}')")
	override fun findModeratorByThreadId(threadId: UUID, pageable: Pageable): Mono<Page<ModeratorView>> {
		return reactorQueryGateway.query(
			FindModeratorsByThreadIdQuery(threadId, pageable),
			PageResponseType(ModeratorView::class.java)
		)
	}
}
