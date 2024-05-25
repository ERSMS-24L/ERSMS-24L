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
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.config.PageResponseType
import pl.edu.pw.ia.shared.domain.query.FindBannedAccountByThreadAndAccountIdsQuery
import pl.edu.pw.ia.shared.domain.query.FindBannedAccountsByThreadIdQuery
import pl.edu.pw.ia.shared.domain.view.BannedUserView
import pl.edu.pw.ia.shared.security.Scopes
import reactor.core.publisher.Mono

@Tag(name = "BannedUsers")
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
interface BannedUserViewController {

	@Operation(description = "Find banned user by account id and thread id")
	fun findBannedUserByAccountIdAndThreadId(@RequestParam accountId: UUID, @RequestParam threadId: UUID): Mono<BannedUserView>

	@Operation(description = "Find banned users by thread id")
	fun findBannedUserByThreadId(@RequestParam threadId: UUID, @PageableDefault(page = 0) pageable: Pageable): Mono<Page<BannedUserView>>
}

@RestController
@RequestMapping(
	value = ["/api/v1/bannedUsers"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class BannedUserViewControllerImpl(
	private val reactorQueryGateway: ReactorQueryGateway
) : BannedUserViewController {

	@GetMapping
	@PreAuthorize("hasAnyAuthority(${Scopes.BANNEDUSER.READ})")
	override fun findBannedUserByAccountIdAndThreadId(accountId: UUID, threadId: UUID): Mono<BannedUserView> {
		return reactorQueryGateway.query(
			FindBannedAccountByThreadAndAccountIdsQuery(threadId = threadId, accountId = accountId),
			ResponseTypes.instanceOf(BannedUserView::class.java)
		)
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority(${Scopes.BANNEDUSER.READ})")
	override fun findBannedUserByThreadId(threadId: UUID, pageable: Pageable): Mono<Page<BannedUserView>> {
		return reactorQueryGateway.query(
			FindBannedAccountsByThreadIdQuery(threadId, pageable),
			PageResponseType(BannedUserView::class.java)
		)
	}
}