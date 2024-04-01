package pl.edu.pw.ia.threads.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
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
import pl.edu.pw.ia.shared.application.model.IdResponse
import pl.edu.pw.ia.shared.security.Scopes
import pl.edu.pw.ia.threads.application.model.ThreadCreateRequest
import pl.edu.pw.ia.threads.application.model.ThreadUpdateRequest
import reactor.core.publisher.Mono
import java.util.UUID

@Tag(name = "Threads")
@ApiResponse(
    responseCode = "500",
    description = "Internal Server Error.",
    content = [Content(schema = Schema(implementation = ApiErrorResponse::class))]
)
@SecurityRequirement(name = "Bearer")
interface ThreadController {

    @Operation(description = "Create thread")
    @ApiResponse(responseCode = "201", description = "Created.")
    fun createThread(@Valid request: ThreadCreateRequest): Mono<IdResponse>

    @Operation(description = "Update thread")
    @ApiResponse(responseCode = "200", description = "Updated.")
    fun updateThread(@Valid request: ThreadUpdateRequest): Mono<IdResponse>
}

@Validated
@RestController
@RequestMapping(
    value = ["/api/v1/threads"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ThreadControllerImpl(
    private val reactorCommandGateway: ReactorCommandGateway,
) : ThreadController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('${Scopes.THREAD.WRITE}')")
    override fun createThread(
        @RequestBody request: ThreadCreateRequest
    ): Mono<IdResponse> {
        val command = request.toCommand()
        return reactorCommandGateway.send<UUID>(command).map { IdResponse(id=it) }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    override fun updateThread(request: ThreadUpdateRequest): Mono<IdResponse> {
        val command = request.toCommand()
        return reactorCommandGateway.send<UUID>(command).map { IdResponse(id=it) }
    }
}