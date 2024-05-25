package pl.edu.pw.ia.threads.infrastructure.query.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.view.BannedUserView
import pl.edu.pw.ia.threads.domain.query.repository.BannedUserViewRepository
import pl.edu.pw.ia.threads.infrastructure.query.entity.BannedUserViewEntity.Companion.toEntity
import pl.edu.pw.ia.threads.infrastructure.query.repository.database.SpringBannedUserViewEntityRepository
import reactor.core.publisher.Mono

@Service
class BannedUserViewRepositoryImpl(
	private val repository: SpringBannedUserViewEntityRepository
) : BannedUserViewRepository {
	override fun save(bannedUser: BannedUserView) {
		Mono.just(bannedUser)
			.map { it.toEntity() }
			.flatMap { repository.save(it) }
			.block()
	}

	override fun delete(bannedUserId: UUID) {
		repository.deleteById(bannedUserId.toString()).block()
	}

	override fun findByAccountIdAndThreadId(accountId: UUID, threadId: UUID): BannedUserView? {
		return repository.findByAccountIdAndThreadId(accountId.toString(), threadId.toString())
			.map { it.toDomain() }
			.block()
	}

	override fun findByThreadId(threadId: UUID, pageable: Pageable): Page<BannedUserView> {
		val views = repository.findByThreadId(threadId.toString(), pageable)
			.map { it.toDomain() }
			.collectList()
			.block()
		val countViews = repository.countByThreadId(threadId.toString()).block()
		return PageImpl(views ?: emptyList(), pageable, countViews ?: 0)
	}
}