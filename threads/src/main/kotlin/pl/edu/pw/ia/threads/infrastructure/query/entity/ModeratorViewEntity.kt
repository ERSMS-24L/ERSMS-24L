package pl.edu.pw.ia.threads.infrastructure.query.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pw.ia.shared.domain.view.ModeratorView

@Document(collection = "moderatorViews")
data class ModeratorViewEntity(
	@Id
	val moderatorId: String,
	val threadId: String,
	val accountId: String,
	val username: String?
) {
	fun toDomain() : ModeratorView =
		ModeratorView(
			moderatorId = UUID.fromString(moderatorId),
			threadId = UUID.fromString(threadId),
			accountId = UUID.fromString(accountId),
			username = username,

		)

	companion object {
		fun ModeratorView.toEntity(): ModeratorViewEntity =
			ModeratorViewEntity(
				moderatorId = moderatorId.toString(),
				threadId = threadId.toString(),
				accountId = accountId.toString(),
				username = username
			)
	}
}