package pl.edu.pw.ia.threads.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.domain.command.CreateThreadCommand
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