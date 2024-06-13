package pl.edu.pw.ia.shared.domain.query

import java.util.UUID
import org.springframework.data.domain.Pageable
import java.time.Instant

data class FindThreadByIdQuery(
	val threadId: UUID,
)

data class FindThreadsByTitleQuery(
	val title: String,
	val pageable: Pageable
)

data class FindRecentThreadsQuery(
	val date: Instant,
	val pageable: Pageable
)

data class FindThreadsByAuthor(
	val accountId: UUID,
	val pageable: Pageable
)

data class FindModeratorByThreadAndAccountIdQuery(
	val threadId: UUID,
	val accountId: UUID,
)

data class FindBannedAccountByThreadAndAccountIdsQuery(
	val threadId: UUID,
	val accountId: UUID,
)

data class FindModeratorsByThreadIdQuery(
	val threadId: UUID,
	val pageable: Pageable,
)

data class FindBannedAccountsByThreadIdQuery(
	val threadId: UUID,
	val pageable: Pageable,
)

data class FindBannedUserByBannedUserIdQuery(
	val bannedUserId: UUID
)
