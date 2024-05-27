package pl.edu.pw.ia.threads.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.util.UUID
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.domain.command.CreateThreadCommand
import pl.edu.pw.ia.shared.domain.command.DeleteThreadCommand
import pl.edu.pw.ia.shared.domain.command.UpdateThreadCommand

data class ThreadCreateRequest(
	@Schema(maxLength = 256)
	@field:NotBlank(message = "Title cannot be blank")
	@field:Length(max = 256, message = "Maximum allowed title length is 256 characters.")
	val title: String
) {
	fun toCommand(accountId: UUID) =
		CreateThreadCommand(
			threadId = UUID.randomUUID(),
			title = title,
			accountId = accountId,
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
	fun toCommand(accountId: UUID) =
		UpdateThreadCommand(
			threadId = UUID.fromString(threadId),
			title = title,
			accountId = accountId,
		)
}

data class ThreadDeleteRequest(
	@field:org.hibernate.validator.constraints.UUID(message = "Thread id is invalid")
	val threadId: String,
) {
	fun toCommand(accountId: UUID) =
		DeleteThreadCommand(
			threadId = UUID.fromString(threadId),
			accountId = accountId,
		)
}
