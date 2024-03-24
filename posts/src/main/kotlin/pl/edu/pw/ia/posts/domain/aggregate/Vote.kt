package pl.edu.pw.ia.posts.domain.aggregate

import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.posts.domain.exception.VoteAlreadyExistsException
import pl.edu.pw.ia.shared.domain.command.CreateVoteCommand
import pl.edu.pw.ia.shared.domain.command.UpdateVoteCommand
import pl.edu.pw.ia.shared.domain.event.VoteCreatedEvent
import pl.edu.pw.ia.shared.domain.event.VoteUpdatedEvent
import pl.edu.pw.ia.shared.domain.exception.AccountMismatchException
import pl.edu.pw.ia.shared.domain.exception.AccountNotFoundException
import pl.edu.pw.ia.shared.domain.exception.PostNotFoundException
import pl.edu.pw.ia.shared.domain.model.VoteType
import pl.edu.pw.ia.shared.domain.verifier.AccountVerifier
import pl.edu.pw.ia.shared.domain.verifier.PostVerifier
import pl.edu.pw.ia.shared.domain.verifier.VoteVerifier

@Aggregate
class Vote {

	@AggregateIdentifier
	private lateinit var voteId: UUID
	private lateinit var accountId: UUID
	private lateinit var postId: UUID
	private lateinit var vote: VoteType

	private constructor()

	@CommandHandler
	constructor(
		command: CreateVoteCommand,
		accountVerifier: AccountVerifier,
		postVerifier: PostVerifier,
		voteVerifier: VoteVerifier,
	) {
		if (!accountVerifier.accountExists(command.accountId)) {
			throw AccountNotFoundException(command.accountId)
		}
		if (!postVerifier.postExists(command.postId)) {
			throw PostNotFoundException(command.postId)
		}
		if (voteVerifier.voteExists(command.accountId, command.postId)) {

			throw VoteAlreadyExistsException(command.accountId, command.postId)
		}

		AggregateLifecycle.apply(
			VoteCreatedEvent(
				voteId = command.voteId,
				accountId = command.accountId,
				postId = command.postId,
				vote = command.vote,
			)
		)
	}

	@CommandHandler
	fun handle(command: UpdateVoteCommand) {
		if (accountId != command.accountId) {
			throw AccountMismatchException(
				currentAccountId = command.accountId,
				ownerAccountId = accountId,
			)
		}
		AggregateLifecycle.apply(
			VoteUpdatedEvent(
				voteId = voteId,
				accountId = accountId,
				postId = postId,
				previousVote = vote,
				vote = command.vote,
			)
		)
	}

	@EventSourcingHandler
	fun on(event: VoteCreatedEvent) {
		voteId = event.voteId
		accountId = event.accountId
		postId = event.postId
		vote = event.vote
	}

	@EventSourcingHandler
	fun on(event: VoteUpdatedEvent) {
		vote = event.vote
	}
}
