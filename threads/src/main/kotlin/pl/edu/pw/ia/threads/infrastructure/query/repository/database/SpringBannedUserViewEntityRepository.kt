package pl.edu.pw.ia.threads.infrastructure.query.repository.database

import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import pl.edu.pw.ia.threads.infrastructure.query.entity.BannedUserViewEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SpringBannedUserViewEntityRepository : ReactiveMongoRepository<BannedUserViewEntity, String> {
	fun findByAccountIdAndThreadId(accountId: String, threadId: String): Mono<BannedUserViewEntity>
	fun findByThreadId(threadId: String, pageable: Pageable): Flux<BannedUserViewEntity>
	fun countByThreadId(threadId: String): Mono<Long>

	fun findByBannedUserId(bannedUserId: String): Mono<BannedUserViewEntity>
}