package pl.edu.pw.ia.shared.domain.exception

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
open class NotFoundException(message: String? = null, cause: Throwable? = null) :
	RuntimeException(
		message?.let { "[NotFoundException] $message" } ?: cause?.message,
		cause
	)

class AccountNotFoundException(accountId: UUID) : NotFoundException("Could not find account with Id: $accountId")

class AccountWithUsernameNotFoundException(username: String) : NotFoundException("Could not find account with username: $username")

class ThreadNotFoundException(threadId: UUID) : NotFoundException("Could not find thread with Id: $threadId")

class PostNotFoundException(postId: UUID) : NotFoundException("Could not find post with id: $postId")

class VoteNotFoundException(postId: UUID, accountId: UUID) : NotFoundException("Could not find vote with post id: $postId and account id: $accountId")

class ModeratorNotFoundException(accountId: UUID, threadId: UUID) : NotFoundException("Could not find moderator with account id: $accountId and thread id: $threadId")

class BannedUserNotFoundException(accountId: UUID, threadId: UUID) : NotFoundException("Could not find banned account with account id: $accountId in thread with id: $threadId")
