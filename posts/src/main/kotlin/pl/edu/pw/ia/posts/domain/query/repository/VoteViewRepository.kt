package pl.edu.pw.ia.posts.domain.query.repository

import java.util.UUID
import pl.edu.pw.ia.shared.domain.view.VoteView

interface VoteViewRepository {
	fun save(vote: VoteView)

	fun findByAccountIdAndPostId(postId: UUID, accountId: UUID): VoteView?

}