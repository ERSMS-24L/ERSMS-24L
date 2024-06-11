package pl.edu.pw.ia.shared.security

object Scopes {

	val USER = UserScopes
	val THREAD = ThreadScopes
	val POST = PostScopes
	val VOTE = VoteScopes
	val MODERATOR = ModeratorScopes
	val BANNEDUSER = BannedUserScopes

	object UserScopes {
		private const val USERS = "users"

		const val READ = "read:${USERS}"
		const val WRITE = "write:${USERS}"
	}

	object ThreadScopes {
		private const val THREADS = "threads"

		const val READ = "read:${THREADS}"
		const val WRITE = "write:${THREADS}"
	}

	object PostScopes {
		private const val POSTS = "posts"

		const val READ = "read:${POSTS}"
		const val WRITE = "write:${POSTS}"
	}

	object VoteScopes {
		private const val VOTES = "votes"

		const val READ = "read:${VOTES}"
		const val WRITE = "write:${VOTES}"
	}

	object ModeratorScopes {
		private const val MODERATORS = "moderators"

		const val READ = "read:${MODERATORS}"
		const val WRITE = "write:${MODERATORS}"
	}

	object BannedUserScopes {
		private const val BANNED_USERS = "bannedUsers"

		const val READ = "read:${BANNED_USERS}"
		const val WRITE = "write:${BANNED_USERS}"
	}
}
