package pl.edu.pw.ia.posts.infrastructure.query.entity;

import java.time.Instant
import java.util.UUID
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.pw.ia.shared.domain.view.PostView

@Document(collection = "postViews")
data class PostViewEntity(
	@Id
	val postId: String,
	val threadId: String,
	val content: String,
	val accountId: String,
	val username: String,
	val createdAt: Instant,
	val votes: Int = 0,
) {
	fun toDomain(): PostView =
		PostView(
			postId = UUID.fromString(postId),
			threadId = UUID.fromString(threadId),
			content = content,
			username = username,
			accountId = UUID.fromString(accountId),
			createdAt = createdAt,
			votes = votes
		)

	companion object {
		fun PostView.toEntity(): PostViewEntity =
			PostViewEntity(
				postId = postId.toString(),
				content = content,
				accountId = accountId.toString(),
				threadId = threadId.toString(),
				username = username,
				createdAt = createdAt,
				votes = votes
			)
	}
}
