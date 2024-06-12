package pl.edu.pw.ia.shared.domain.query

import java.util.UUID

data class FindAccountByIdQuery(
	val accountId: UUID,
)

data class FindAccountByUsernameQuery(
	val username: String,
)