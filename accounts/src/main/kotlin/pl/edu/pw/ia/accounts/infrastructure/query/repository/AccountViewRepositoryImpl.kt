package pl.edu.pw.ia.accounts.infrastructure.query.repository

import java.util.UUID
import org.springframework.stereotype.Service
import pl.edu.pw.ia.accounts.domain.query.repository.AccountViewRepository
import pl.edu.pw.ia.accounts.infrastructure.query.entity.AccountViewEntity.Companion.toEntity
import pl.edu.pw.ia.accounts.infrastructure.query.repository.database.SpringAccountViewEntityRepository
import pl.edu.pw.ia.shared.domain.view.AccountView
import reactor.core.publisher.Mono

@Service
class AccountViewRepositoryImpl(
	private val repository: SpringAccountViewEntityRepository,
) : AccountViewRepository {

	override fun save(account: AccountView) {
		Mono.just(account)
			.map { it.toEntity() }
			.flatMap { repository.save(it) }
			.block()
	}

	override fun findById(id: UUID): AccountView? =
		repository.findById(id.toString())
			.map { it.toDomain() }
			.block()

	override fun findByUsername(username: String): AccountView? =
		repository.findByName(username)
			.map { it.toDomain() }
			.block()
}
