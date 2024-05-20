package pl.edu.pw.ia.posts.infrastructure.query.repository.database

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import pl.edu.pw.ia.posts.infrastructure.query.entity.VoteViewEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SpringVoteViewEntityRepository : ReactiveMongoRepository<VoteViewEntity, String> {
	fun findByPostIdAndAccountId(postId: String, accountId: String): Mono<VoteViewEntity>
}