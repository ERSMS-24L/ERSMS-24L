package pl.edu.pw.ia.users.infrastructure.query.repository.database

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import pl.edu.pw.ia.users.infrastructure.query.entity.UserViewEntity

@Repository
interface SpringUserViewEntityRepository : ReactiveMongoRepository<UserViewEntity, String>
