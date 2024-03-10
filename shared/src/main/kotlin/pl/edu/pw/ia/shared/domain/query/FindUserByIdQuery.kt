package pl.edu.pw.ia.shared.domain.query

import java.util.UUID

data class FindUserByIdQuery(
		val userId: UUID,
)
