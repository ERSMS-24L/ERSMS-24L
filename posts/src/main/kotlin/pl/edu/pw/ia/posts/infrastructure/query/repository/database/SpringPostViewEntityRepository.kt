package pl.edu.pw.ia.posts.infrastructure.query.repository.database;

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.pw.ia.posts.infrastructure.query.entity.PostViewEntity
import reactor.core.publisher.Flux

@Repository
interface SpringPostViewEntityRepository : ReactiveMongoRepository<PostViewEntity, String>{
	fun findByThreadId(accountId: String, pageable: Pageable) : Flux<PostViewEntity>

	fun findByContentIsContainingIgnoreCase(content: String, pageable: Pageable): Flux<PostViewEntity>

	fun findByContentIsContainingIgnoreCaseAndThreadId(content: String, threadId: String, pageable: Pageable): Flux<PostViewEntity>
}
