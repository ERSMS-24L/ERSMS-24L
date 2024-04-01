package pl.edu.pw.ia.threads.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.domain.command.CreateThreadCommand
import pl.edu.pw.ia.shared.domain.command.DeleteThreadCommand
import pl.edu.pw.ia.shared.domain.command.UpdateThreadCommand
import pl.edu.pw.ia.shared.security.SecurityContext
import java.util.UUID

data class ThreadCreateRequest(
    @Schema(maxLength = 256)
    @field:NotBlank(message = "Title cannot be blank")
    @field:Length(max = 256, message = "Maximum allowed title length is 256 characters.")
    val title: String
) {
    fun toCommand() =
        CreateThreadCommand(
            threadId = UUID.randomUUID(),
            title = title,
            accountId = SecurityContext.getAccountId(),
        )
}

data class ThreadUpdateRequest(
    @Schema(maxLength = 256)
    @field:NotBlank(message = "Title cannot be blank")
    @field:Length(max = 256, message = "Maximum allowed title length is 256 characters.")
    val title: String,

    @field:org.hibernate.validator.constraints.UUID(message = "Thread id is invalid")
    val threadId: String,
) {
    fun toCommand() =
        UpdateThreadCommand(
            threadId = UUID.fromString(threadId),
            title = title,
            accountId = SecurityContext.getAccountId(),
        )
}

data class ThreadDeleteRequest(
    @field:org.hibernate.validator.constraints.UUID(message = "Thread id is invalid")
    val threadId: String,
) {
    fun toCommand() =
        DeleteThreadCommand(
            threadId = UUID.fromString(threadId),
            accountId = SecurityContext.getAccountId(),
        )
}