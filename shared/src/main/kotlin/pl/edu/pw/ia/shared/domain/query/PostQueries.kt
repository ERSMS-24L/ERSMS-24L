package pl.edu.pw.ia.shared.domain.query

import java.util.UUID
import org.springframework.data.domain.Pageable

data class FindPostByIdQuery(
	val postId: UUID,
)

data class FindPostsByThreadIdQuery(
	val threadId: UUID,
	val pageable: Pageable,
)

data class FindPostsByContentQuery(
	val content: String,
	val pageable: Pageable,
)

data class FindPostsByContentAndThreadIdQuery(
	val content: String,
	val threadId: UUID,
	val pageable: Pageable,
)

data class FindVoteByAccountAndPostIdsQuery(
	val postId: UUID,
	val accountId: UUID,
)
