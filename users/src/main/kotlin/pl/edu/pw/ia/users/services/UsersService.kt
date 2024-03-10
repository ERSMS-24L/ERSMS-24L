package pl.edu.pw.ia.users.services

import org.springframework.stereotype.Service
import pl.edu.pw.ia.users.domain.entity.UserEntity
import pl.edu.pw.ia.users.repositories.UsersRepository
import reactor.core.publisher.Mono

@Service
class UsersService (
        private val usersRepository: UsersRepository
) {
    fun createUser(user: UserEntity) : Mono<UserEntity> {
        return usersRepository.save(user);
    }
}