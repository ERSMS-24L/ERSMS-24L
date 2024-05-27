package pl.edu.pw.ia.threads.infrastructure.query.entity

import java.time.Instant
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pw.ia.shared.domain.view.ThreadView

@Document(collection = "threadViews")
data class ThreadViewEntity(
	@Id
	val threadId: String,
	val title: String,
	val accountId: String,
	val postId: String?,
	val post: String?,
	val username: String,
	val lastModified: Instant
) {

	fun toDomain(): ThreadView =
		ThreadView(
			accountId = UUID.fromString(accountId),
			threadId = UUID.fromString(threadId),
			postId = postId?.let { UUID.fromString(it) },
			post = post,
			title = title,
			username = username,
			lastModified = lastModified,
		)

	companion object {
		fun ThreadView.toEntity(): ThreadViewEntity =
			ThreadViewEntity(
				threadId = threadId.toString(),
				postId = postId?.toString(),
				title = title,
				accountId = accountId.toString(),
				post = post,
				username = username,
				lastModified = Instant.now(),
			)
	}
}
