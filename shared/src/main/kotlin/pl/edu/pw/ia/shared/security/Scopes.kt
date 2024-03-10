package pl.edu.pw.ia.shared.security

object Scopes {

	val USER = UserScopes

	private const val SCOPE = "SCOPE"

	object UserScopes {
		private const val USERS = "users"

		const val READ = "${SCOPE}_read:${USERS}"
		const val WRITE = "${SCOPE}_write:${USERS}"
	}
}
