package pl.edu.pw.ia.posts.infrastructure.query.repository.database

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import pl.edu.pw.ia.posts.infrastructure.query.entity.PostViewEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SpringPostViewEntityRepository : ReactiveMongoRepository<PostViewEntity, String>{
	fun findByThreadId(threadId: String, pageable: Pageable) : Flux<PostViewEntity>

	fun countByThreadId(threadId: String): Mono<Long>

	fun findByContentIsContainingIgnoreCase(content: String, pageable: Pageable): Flux<PostViewEntity>

	fun countByContentIsContainingIgnoreCase(content: String): Mono<Long>

	fun findByContentIsContainingIgnoreCaseAndThreadId(content: String, threadId: String, pageable: Pageable): Flux<PostViewEntity>

	fun countByContentIsContainingIgnoreCaseAndThreadId(content: String, threadId: String): Mono<Long>
}
