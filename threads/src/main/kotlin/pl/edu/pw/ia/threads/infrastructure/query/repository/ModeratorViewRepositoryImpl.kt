package pl.edu.pw.ia.threads.infrastructure.query.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.view.ModeratorView
import pl.edu.pw.ia.threads.domain.query.repository.ModeratorViewRepository
import pl.edu.pw.ia.threads.infrastructure.query.entity.ModeratorViewEntity.Companion.toEntity
import pl.edu.pw.ia.threads.infrastructure.query.repository.database.SpringModeratorViewEntityRepository
import reactor.core.publisher.Mono

@Service
class ModeratorViewRepositoryImpl(
	private val repository: SpringModeratorViewEntityRepository
) : ModeratorViewRepository {
	override fun save(moderator: ModeratorView) {
		Mono.just(moderator)
			.map { it.toEntity() }
			.flatMap { repository.save(it) }
			.block()
	}

	override fun delete(moderatorId: UUID) {
		repository.deleteById(moderatorId.toString()).block()
	}


	override fun findByAccountIdAndThreadId(accountId: UUID, threadId: UUID): ModeratorView? {
		return repository.findByAccountIdAndThreadId(accountId.toString(), threadId.toString())
			.map { it.toDomain() }
			.blockFirst()
	}

	override fun findByThreadId(threadId: UUID, pageable: Pageable): Page<ModeratorView> {
		val views = repository.findByThreadId(threadId.toString(), pageable)
			.map { it.toDomain() }
			.collectList()
			.block()
		val countViews = repository.countByThreadId(threadId.toString()).block()
		return PageImpl(views ?: emptyList(), pageable, countViews?: 0)
	}
}