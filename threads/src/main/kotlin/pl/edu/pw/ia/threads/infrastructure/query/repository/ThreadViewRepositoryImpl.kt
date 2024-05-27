package pl.edu.pw.ia.threads.infrastructure.query.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.view.ThreadView
import pl.edu.pw.ia.threads.domain.query.repository.ThreadViewRepository
import pl.edu.pw.ia.threads.infrastructure.query.entity.ThreadViewEntity.Companion.toEntity
import pl.edu.pw.ia.threads.infrastructure.query.repository.database.SpringThreadViewEntityRepository
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*
import reactor.core.publisher.Flux

@Service
class ThreadViewRepositoryImpl(
    private val repository: SpringThreadViewEntityRepository
) : ThreadViewRepository {
    override fun save(thread: ThreadView) {
        Mono.just(thread)
            .map { it.toEntity() }
            .flatMap { repository.save(it) }
            .block()
    }

    override fun delete(threadId: UUID) {
        repository.deleteById(threadId.toString()).block()
    }

    override fun findById(id: UUID): ThreadView? {
        return repository.findById(id.toString())
            .map { it.toDomain() }
            .block()
    }

    override fun findByAuthor(accountId: UUID, pageable: Pageable): Page<ThreadView> {
        val threadViews = repository.findByAccountId(accountId.toString(), pageable)
            .map { it.toDomain() }
            .collectList()
            .block()
        val countViews = repository.countByAccountId(accountId.toString()).block()
        return PageImpl(threadViews ?: emptyList(), pageable, countViews ?: 0)
    }

    override fun findByTitle(title: String, pageable: Pageable): Page<ThreadView> {
        val threadViews = repository.findByTitleIsContainingIgnoreCase(title, pageable)
            .map { it.toDomain() }
            .collectList()
            .block()

        val countViews = repository.countByTitleIsContainingIgnoreCase(title).block()

        return PageImpl(threadViews ?: emptyList(), pageable, countViews ?: 0)
    }

    override fun findRecent(date: Instant, pageable: Pageable): Page<ThreadView> {
        val threadViews = repository.findByLastModifiedGreaterThanEqual(date, pageable)
            .map { it.toDomain() }
            .collectList()
            .block()

        val countViews = repository.countByLastModifiedGreaterThanEqual(date).block()
        return PageImpl(threadViews ?: emptyList(), pageable, countViews ?: 0)
    }

    override fun findByAccountId(accountId: UUID): Flux<ThreadView> {
        return repository.findByAccountId(accountId.toString()).map { it.toDomain() }
    }

    override fun findByIdAndPostIdIsNull(threadId: UUID): ThreadView? {
        return repository.findByThreadIdAndPostIdIsNull(threadId = threadId.toString()).map { it.toDomain() }.block()
    }

    override fun findByIdAndPostId(threadId: UUID, postId: UUID): ThreadView? {
        return repository.findByThreadIdAndPostId(threadId.toString(), postId.toString()).map { it.toDomain() }.block()
    }

}