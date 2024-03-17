package pl.edu.pw.ia.accounts.infrastructure.query.repository

import java.util.UUID
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import pl.edu.pw.ia.accounts.domain.query.repository.AccountViewRepository
import pl.edu.pw.ia.accounts.domain.query.view.AccountView
import pl.edu.pw.ia.accounts.infrastructure.query.entity.AccountViewEntity
import pl.edu.pw.ia.accounts.infrastructure.query.repository.database.SpringAccountViewEntityRepository
import reactor.core.publisher.Mono

@Service
class AccountViewRepositoryImpl(
	private val repository: SpringAccountViewEntityRepository,
	private val conversionService: ConversionService,
) : AccountViewRepository {

	override fun save(account: AccountView) {
		Mono.just(account)
			.mapNotNull { conversionService.convert(it, AccountViewEntity::class.java) }
			.flatMap { repository.save(it!!) }
			.block()
	}

	override fun findById(id: UUID): AccountView? =
		repository.findById(id.toString())
			.mapNotNull { conversionService.convert(it, AccountView::class.java) }
			.block()
}
