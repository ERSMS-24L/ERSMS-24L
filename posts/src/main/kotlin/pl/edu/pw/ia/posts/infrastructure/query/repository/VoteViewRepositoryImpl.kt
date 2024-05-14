package pl.edu.pw.ia.posts.infrastructure.query.repository

import java.util.UUID
import org.springframework.stereotype.Service
import pl.edu.pw.ia.posts.domain.query.repository.VoteViewRepository
import pl.edu.pw.ia.posts.infrastructure.query.entity.VoteViewEntity.Companion.toEntity
import pl.edu.pw.ia.posts.infrastructure.query.repository.database.SpringVoteViewEntityRepository
import pl.edu.pw.ia.shared.domain.view.VoteView
import reactor.core.publisher.Mono

@Service
class VoteViewRepositoryImpl(
	private val repository: SpringVoteViewEntityRepository
) : VoteViewRepository {
	override fun save(vote: VoteView) {
		Mono.just(vote).map { it.toEntity() }.flatMap { repository.save(it) }.block()
	}

	override fun findByAccountIdAndPostId(postId: UUID, accountId: UUID): VoteView? {
		return repository.findByPostIdAndAccountId(postId.toString(), accountId.toString()).map { it.toDomain() }.block()
	}
}