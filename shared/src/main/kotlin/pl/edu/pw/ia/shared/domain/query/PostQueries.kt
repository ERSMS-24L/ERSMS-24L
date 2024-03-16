package pl.edu.pw.ia.shared.domain.query

import java.util.UUID
import org.springframework.data.domain.Pageable

data class FindPostByIdQuery(
	val postId: UUID,
)

data class FindVoteByAccountAndPostIdsQuery(
	val postId: UUID,
	val accountId: UUID,
)

data class FindPostsByThreadIdQuery(
	val threadId: UUID,
	val pageable: Pageable,
)
