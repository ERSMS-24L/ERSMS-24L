package pl.edu.pw.ia.shared.domain.query

import java.util.UUID
import org.springframework.data.domain.Pageable

data class FindThreadByIdQuery(
	val threadId: UUID,
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
