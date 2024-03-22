package pl.edu.pw.ia.shared.domain.view

import java.util.UUID

data class AccountView(
	val accountId: UUID,
	val name: String,
	val email: String,
)
