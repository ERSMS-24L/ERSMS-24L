package pl.edu.pw.ia.posts.domain.query.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pl.edu.pw.ia.shared.domain.view.PostView

interface PostViewRepository {
	fun save(post: PostView)

	fun findById(id: UUID): PostView?

	fun findByThreadId(threadId: UUID, pageable: Pageable): Page<PostView>

	fun findByContent(content: String, pageable: Pageable): Page<PostView>

	fun findByContentAndThreadId(content: String, threadId: UUID, pageable: Pageable): Page<PostView>

	fun delete(postId: UUID)

	fun findAllByAccountId(accountId: UUID): Sequence<PostView>
}