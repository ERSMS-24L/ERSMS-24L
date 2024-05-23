package pl.edu.pw.ia.threads.infrastructure.query.repository.database

import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import pl.edu.pw.ia.threads.infrastructure.query.entity.ModeratorViewEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SpringModeratorViewEntityRepository : ReactiveMongoRepository<ModeratorViewEntity, String> {
	fun findByAccountIdAndThreadId(accountId: String, threadId: String): Flux<ModeratorViewEntity>
	fun findByThreadId(threadId: String, pageable: Pageable): Flux<ModeratorViewEntity>
	fun countByThreadId(threadId: String): Mono<Long>
}