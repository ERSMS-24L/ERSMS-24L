package pl.edu.pw.ia.accounts.infrastructure.query.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "accountViews")
data class AccountViewEntity(
	@Id
	val id: String,
	val name: String,
	val email: String,
)
