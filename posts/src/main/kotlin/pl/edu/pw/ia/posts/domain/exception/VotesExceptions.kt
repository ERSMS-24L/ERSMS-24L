package pl.edu.pw.ia.posts.domain.exception

import java.util.UUID
import pl.edu.pw.ia.shared.domain.exception.AlreadyExistsException

class VoteAlreadyExistsException(
	postId: UUID,
	accountId: UUID
) : AlreadyExistsException("Vote on post $postId by account $accountId already exists!")
