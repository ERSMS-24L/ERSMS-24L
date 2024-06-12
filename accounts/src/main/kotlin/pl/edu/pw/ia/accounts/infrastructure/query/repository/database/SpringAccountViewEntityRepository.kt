package pl.edu.pw.ia.accounts.infrastructure.query.repository.database

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import pl.edu.pw.ia.accounts.infrastructure.query.entity.AccountViewEntity
import pl.edu.pw.ia.shared.domain.view.AccountView
import reactor.core.publisher.Mono

@Repository
interface SpringAccountViewEntityRepository : ReactiveMongoRepository<AccountViewEntity, String> {
	fun findByName(name: String): Mono<AccountViewEntity>
}
