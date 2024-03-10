package pl.edu.pw.ia.users.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import pl.edu.pw.ia.users.domain.entity.UserEntity
import reactor.core.publisher.Mono


@Repository
class UsersRepository(
        val repositoryImpl: UsersRepositoryImpl
) {
    fun save(user: UserEntity): Mono<UserEntity> {
        return repositoryImpl.save(user);
    }
}

@Repository
interface UsersRepositoryImpl : ReactiveMongoRepository<UserEntity, String> {}