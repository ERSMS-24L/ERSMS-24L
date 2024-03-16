package pl.edu.pw.ia.users.infrastructure.query.repository

import java.util.UUID
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import pl.edu.pw.ia.users.domain.query.repository.UserViewRepository
import pl.edu.pw.ia.users.domain.query.view.UserView
import pl.edu.pw.ia.users.infrastructure.query.entity.UserViewEntity
import pl.edu.pw.ia.users.infrastructure.query.repository.database.SpringUserViewEntityRepository
import reactor.core.publisher.Mono

@Service
class UserViewRepositoryImpl(
	private val repository: SpringUserViewEntityRepository,
	private val conversionService: ConversionService,
) : UserViewRepository {

	override fun save(user: UserView) {
		Mono.just(user)
			.mapNotNull { conversionService.convert(it, UserViewEntity::class.java) }
			.flatMap { repository.save(it!!) }
			.block()
	}

	override fun findById(id: UUID): UserView? =
		repository.findById(id.toString())
			.mapNotNull { conversionService.convert(it, UserView::class.java) }
			.block()
}
