package pl.edu.pw.ia.users.controllers


import org.springframework.web.bind.annotation.*
import pl.edu.pw.ia.users.domain.mapper.UserMapper.mapToEntity
import pl.edu.pw.ia.users.domain.mapper.UserMapper.mapToResponse
import pl.edu.pw.ia.users.dto.CreateUserRequest
import pl.edu.pw.ia.users.dto.UserResponse
import pl.edu.pw.ia.users.services.UsersService
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/users")
class UsersController(
        private val usersService: UsersService
) {
    @PostMapping
    fun createUser(@RequestBody request: Mono<CreateUserRequest>): Mono<UserResponse> {
        return request.flatMap { req ->
            usersService.createUser(mapToEntity(req)).map(::mapToResponse)
        }
    }

}

