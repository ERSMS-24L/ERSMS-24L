package pl.edu.pw.ia.shared.domain.exception

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
open class NotFoundException(message: String) : RuntimeException(message)

class AccountNotFoundException(accountId: UUID) : NotFoundException("Could not find account with Id: $accountId")

class ThreadNotFoundException(threadId: UUID) : NotFoundException("Could not find thread with Id: $threadId")

class PostNotFoundException(postId: UUID) : NotFoundException("Could not find post with id: $postId")

class VoteNotFoundException(voteId: UUID) : NotFoundException("Could not find vote with id: $voteId")
