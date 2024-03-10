package pl.edu.pw.ia.users.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class UserEntity(
        @Id
        val id: String? = null,
        val name: String,
        val surname: String
)