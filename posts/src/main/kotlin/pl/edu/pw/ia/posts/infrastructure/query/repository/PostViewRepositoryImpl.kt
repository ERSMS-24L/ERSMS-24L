package pl.edu.pw.ia.posts.infrastructure.query.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pl.edu.pw.ia.posts.domain.query.repository.PostViewRepository
import pl.edu.pw.ia.posts.infrastructure.query.entity.PostViewEntity.Companion.toEntity
import pl.edu.pw.ia.posts.infrastructure.query.repository.database.SpringPostViewEntityRepository
import pl.edu.pw.ia.shared.domain.view.PostView
import reactor.core.publisher.Mono

@Service
class PostViewRepositoryImpl(
	private val repository: SpringPostViewEntityRepository
) : PostViewRepository {
	override fun save(post: PostView) {
		Mono.just(post).map { it.toEntity() }.flatMap { repository.save(it) }.block()
	}

	override fun delete(postId: UUID) {
		repository.deleteById(postId.toString()).block()
	}

	override fun findById(id: UUID): PostView? {
		return repository.findById(id.toString())
			.map { it.toDomain() }
			.block()
	}

	override fun findByThreadId(threadId: UUID, pageable: Pageable): Page<PostView> {
		val postViews = repository.findByThreadId(threadId.toString(), pageable)
			.map { it.toDomain() }
			.collectList()
			.block()
		val countViews = repository.countByThreadId(threadId.toString()).block()
		return PageImpl(postViews ?: emptyList(), pageable, countViews ?: 0)
	}

	override fun findByContent(content: String, pageable: Pageable): Page<PostView> {
		val postViews = repository.findByContentIsContainingIgnoreCase(content, pageable)
			.map { it.toDomain() }
			.collectList()
			.block()
		val countViews = repository.countByContentIsContainingIgnoreCase(content).block()
		return PageImpl(postViews ?: emptyList(), pageable, countViews ?: 0)
	}

	override fun findByContentAndThreadId(content: String, threadId: UUID, pageable: Pageable): Page<PostView> {
		val postViews = repository.findByContentIsContainingIgnoreCaseAndThreadId(content, threadId.toString(), pageable)
			.map { it.toDomain() }
			.collectList()
			.block()
		val countViews = repository.countByContentIsContainingIgnoreCaseAndThreadId(content, threadId.toString()).block()
		return PageImpl(postViews ?: emptyList(), pageable, countViews ?: 0)
	}
}