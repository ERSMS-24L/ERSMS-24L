package pl.edu.pw.ia.posts.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import pl.edu.pw.ia.posts.application.model.CreateVoteRequest
import pl.edu.pw.ia.posts.application.model.UpdateVoteRequest
import pl.edu.pw.ia.shared.application.exception.ApiErrorResponse
import pl.edu.pw.ia.shared.application.model.IdResponse
import pl.edu.pw.ia.shared.domain.query.FindVoteByAccountAndPostIdsQuery
import pl.edu.pw.ia.shared.domain.view.VoteView
import pl.edu.pw.ia.shared.security.getAccountId
import reactor.core.publisher.Mono

@Tag(name = "Votes")
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error.",
	content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface VoteController {

	@Operation(description = "Post a vote")
	@ApiResponse(responseCode = "201", description = "Ok.")
	fun createVote(
		request: CreateVoteRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse>

	@Operation(description = "Update a vote")
	@ApiResponse(responseCode = "201", description = "Ok.")
	fun updateVote(
		request: UpdateVoteRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse>
}

@Validated
@RestController
@RequestMapping(
	value = ["/api/v1/votes"],
	produces = [MediaType.APPLICATION_JSON_VALUE]
)
class VoteControllerImpl(
	private val reactorCommandGateway: ReactorCommandGateway,
	private val reactorQueryGateway: ReactorQueryGateway
) : VoteController {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	override fun createVote(
		@RequestBody request: CreateVoteRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse> {
		// TODO: Not working, throws 404 when Vote should be created
		return reactorQueryGateway.query(
			FindVoteByAccountAndPostIdsQuery(request.postId, webExchange.getAccountId()),
			ResponseTypes.instanceOf(VoteView::class.java)
		).flatMap { voteView ->
			val updateRequest = UpdateVoteRequest(voteView.voteId, request.postId, request.vote)
			updateVote(updateRequest, webExchange)
		}.onErrorResume {
			val command = request.toCommand(webExchange.getAccountId())
			reactorCommandGateway.send<UUID>(command)
				.map { IdResponse(id = it) }
		}
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	override fun updateVote(
		@RequestBody request: UpdateVoteRequest,
		webExchange: ServerWebExchange
	): Mono<IdResponse> {
		val command = request.toCommand(webExchange.getAccountId())
		return reactorCommandGateway.send<UUID>(command)
			.map { IdResponse(id = it) }
	}
}
