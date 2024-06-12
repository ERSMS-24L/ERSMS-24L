package pl.edu.pw.ia.posts.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.util.UUID
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.domain.command.CreatePostCommand
import pl.edu.pw.ia.shared.domain.command.DeletePostByAdminCommand
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
	fun toCommand(accountId: UUID) =
		CreatePostCommand(
			postId = UUID.randomUUID(),
			accountId = accountId,
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
	fun toCommand(accountId: UUID) =
		UpdatePostCommand(
			postId = UUID.fromString(postId),
			content = content,
			accountId = accountId,
		)
}

data class PostDeleteRequest(
	@field:org.hibernate.validator.constraints.UUID(message = "Post id is invalid")
	val postId: String,
) {
	fun toCommand(accountId: UUID) =
		DeletePostCommand(
			postId = UUID.fromString(postId),
			accountId = accountId,
		)

	fun toAdminCommand(accountId: UUID) =
		DeletePostByAdminCommand(
			postId = UUID.fromString(postId),
			accountId = accountId,
		)
}
