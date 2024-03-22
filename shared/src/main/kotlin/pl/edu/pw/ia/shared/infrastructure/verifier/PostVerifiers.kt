package pl.edu.pw.ia.shared.infrastructure.verifier

import java.util.UUID
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.springframework.stereotype.Service
import pl.edu.pw.ia.shared.domain.query.FindPostByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindVoteByAccountAndPostIdsQuery
import pl.edu.pw.ia.shared.domain.verifier.PostVerifier
import pl.edu.pw.ia.shared.domain.verifier.VoteVerifier
import pl.edu.pw.ia.shared.domain.view.PostView
import pl.edu.pw.ia.shared.domain.view.VoteView

@Service
class PostVerifierImpl(
	val reactorQueryGateway: ReactorQueryGateway,
) : PostVerifier {

	override fun postExists(postId: UUID): Boolean =
		reactorQueryGateway.query(FindPostByIdQuery(postId), PostView::class.java)
			.blockOptional()
			.isPresent
}

@Service
class VoteVerifierImpl(
	val reactorQueryGateway: ReactorQueryGateway,
) : VoteVerifier {

	override fun voteExists(accountId: UUID, postId: UUID): Boolean =
		reactorQueryGateway.query(
			FindVoteByAccountAndPostIdsQuery(
				accountId = accountId,
				postId =  postId,
			),
			VoteView::class.java
		)
			.blockOptional()
			.isPresent
}
