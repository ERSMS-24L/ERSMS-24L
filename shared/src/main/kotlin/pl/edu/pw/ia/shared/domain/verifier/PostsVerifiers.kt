package pl.edu.pw.ia.shared.domain.verifier

import java.util.UUID

interface PostVerifier {

	fun postExists(postId: UUID): Boolean
}

interface VoteVerifier {

	fun voteExists(
		accountId: UUID,
		postId: UUID,
	): Boolean
}
