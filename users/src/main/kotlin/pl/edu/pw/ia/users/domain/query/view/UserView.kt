package pl.edu.pw.ia.users.domain.query.view

import java.util.UUID

data class UserView(
	val id: UUID,
	val name: String,
	val email: String,
) {
	override fun toString(): String {
		return "UserView(id=$id, name='$name', email=**********)"
	}
}
