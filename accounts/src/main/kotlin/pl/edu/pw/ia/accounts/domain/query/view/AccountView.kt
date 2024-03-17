package pl.edu.pw.ia.accounts.domain.query.view

import java.util.UUID

data class AccountView(
	val accountId: UUID,
	val name: String,
	val email: String,
)