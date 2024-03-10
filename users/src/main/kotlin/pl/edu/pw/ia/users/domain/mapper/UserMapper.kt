package pl.edu.pw.ia.users.domain.mapper

import pl.edu.pw.ia.users.domain.entity.UserEntity
import pl.edu.pw.ia.users.dto.CreateUserRequest
import pl.edu.pw.ia.users.dto.UserResponse

object UserMapper {
    fun mapToEntity(request: CreateUserRequest): UserEntity {
        return UserEntity(
                name = request.name,
                surname = request.surname
        );
    }

    fun mapToResponse(userEntity: UserEntity) : UserResponse {
        return UserResponse(
                id = userEntity.id!!,
                name = userEntity.name,
                surname = userEntity.surname,
        )
    }
}