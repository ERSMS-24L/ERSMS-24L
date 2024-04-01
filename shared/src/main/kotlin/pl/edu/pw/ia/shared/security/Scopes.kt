package pl.edu.pw.ia.shared.security

object Scopes {

	val USER = UserScopes
	val THREAD = ThreadScopes

	private const val SCOPE = "SCOPE"

	object UserScopes {
		private const val USERS = "users"

		const val READ = "${SCOPE}_read:${USERS}"
		const val WRITE = "${SCOPE}_write:${USERS}"
	}

	object ThreadScopes {
		private const val THREADS = "threads"

		const val READ = "${SCOPE}_read:${THREADS}"
		const val WRITE = "${SCOPE}_write:${THREADS}"
	}
}
