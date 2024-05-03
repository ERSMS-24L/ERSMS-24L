package pl.edu.pw.ia.posts.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.security.SecurityContext
import java.util.UUID
import pl.edu.pw.ia.shared.domain.command.CreatePostCommand
import pl.edu.pw.ia.shared.domain.command.DeletePostCommand
import pl.edu.pw.ia.shared.domain.command.UpdatePostCommand

data class PostCreateRequest(
	@Schema(maxLength = 4096)
	@field:NotBlank(message = "Content cannot be blank")
	@field:Length(max = 4096, message = "Maximum allowed post length is 4096 characters.")
	val content: String,

	@field:org.hibernate.validator.constraints.UUID(message = "Thread id is invalid")
	val threadId: String,
) {
	fun toCommand() =
		CreatePostCommand(
			postId = UUID.randomUUID(),
			accountId = SecurityContext.getAccountId(),
			content = content,
			threadId = UUID.fromString(threadId),
		)
}

data class PostUpdateRequest(
	@Schema(maxLength = 4096)
	@field:NotBlank(message = "Content cannot be blank")
	@field:Length(max = 4096, message = "Maximum allowed post length is 4096 characters.")
	val content: String,

	@field:org.hibernate.validator.constraints.UUID(message = "Post id is invalid")
	val postId: String,
) {
	fun toCommand() =
		UpdatePostCommand(
			postId = UUID.fromString(postId),
			content = content,
			accountId = SecurityContext.getAccountId(),
		)
}

data class PostDeleteRequest(
	@field:org.hibernate.validator.constraints.UUID(message = "Post id is invalid")
	val postId: String,
) {
	fun toCommand() =
		DeletePostCommand(
			postId = UUID.fromString(postId),
			accountId = SecurityContext.getAccountId(),
		)
}