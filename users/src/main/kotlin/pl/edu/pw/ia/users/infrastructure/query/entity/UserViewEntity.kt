package pl.edu.pw.ia.users.infrastructure.query.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class UserViewEntity(
	@Id
	val id: String,
	val name: String,
	val email: String,
)
