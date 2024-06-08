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
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.domain.exception.VoteNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindVoteByAccountAndPostIdsQuery
import pl.edu.pw.ia.shared.domain.view.VoteView
import pl.edu.pw.ia.shared.security.getAccountId
import reactor.core.publisher.Mono

@Tag(name = "Votes")
@ApiResponse(responseCode = "200", description = "Ok.")
@ApiResponse(
	responseCode = "404",
	description = "Vote not found.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface VoteViewController {

	@Operation(description = "Find vote by post id and account id")
	fun findVoteByAccountAndPostIdQuery(
		@RequestParam postId: UUID,
		webExchange: ServerWebExchange
	): Mono<VoteView>

}

@RestController
@RequestMapping(
	value = ["/api/v1/votes"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class VoteViewControllerImpl(
	private val reactorQueryGateway: ReactorQueryGateway
) : VoteViewController {

	@GetMapping("/")
	override fun findVoteByAccountAndPostIdQuery(
		@RequestParam postId: UUID,
		webExchange: ServerWebExchange
	): Mono<VoteView> {
		val accountId = webExchange.getAccountId()
		return reactorQueryGateway.query(
			FindVoteByAccountAndPostIdsQuery(postId, accountId),
			ResponseTypes.instanceOf(VoteView::class.java)
		).switchIfEmpty(
			Mono.error { VoteNotFoundException(postId, accountId) }
		)
	}
}
