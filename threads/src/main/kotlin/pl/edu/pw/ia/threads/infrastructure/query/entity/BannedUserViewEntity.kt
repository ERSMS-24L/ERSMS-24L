package pl.edu.pw.ia.threads.infrastructure.query.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pw.ia.shared.domain.view.BannedUserView

@Document(collection = "bannedUserViews")
data class BannedUserViewEntity(
	@Id
	val bannedUserId: String,
	val threadId: String,
	val accountId: String,
	val username: String?,
) {
	fun toDomain(): BannedUserView =
		BannedUserView(
			bannedUserId = UUID.fromString(bannedUserId),
			threadId = UUID.fromString(threadId),
			accountId = UUID.fromString(accountId),
			username = username,
		)

	companion object {
		fun BannedUserView.toEntity(): BannedUserViewEntity =
			BannedUserViewEntity(
				bannedUserId = bannedUserId.toString(),
				threadId = threadId.toString(),
				accountId = accountId.toString(),
				username = username,
			)
	}
}