package pl.edu.pw.ia.posts.domain.command.aggregate

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.exception.AccountMismatchException
import java.time.Instant
import java.util.UUID
import pl.edu.pw.ia.shared.domain.command.CreatePostCommand
import pl.edu.pw.ia.shared.domain.command.DeletePostCommand
import pl.edu.pw.ia.shared.domain.command.UpdatePostCommand
import pl.edu.pw.ia.shared.domain.event.PostCreatedEvent
import pl.edu.pw.ia.shared.domain.event.PostDeletedEvent
import pl.edu.pw.ia.shared.domain.event.PostUpdatedEvent

@Aggregate
internal class Post {

	@AggregateIdentifier
	private lateinit var postId: UUID
	private lateinit var content: String
	private lateinit var accountId: UUID

	private constructor()

	@CommandHandler
	constructor(command: CreatePostCommand) {
		AggregateLifecycle.apply(
			PostCreatedEvent(
				postId = command.postId,
				accountId = command.accountId,
				content = command.content,
				createdAt = Instant.now(),
			)
		)
	}

	@CommandHandler
	fun handle(command: UpdatePostCommand) {
		if (accountId != command.accountId) {
			throw AccountMismatchException(
				currentAccountId = command.accountId,
				ownerAccountId = accountId,
			)
		}
		AggregateLifecycle.apply(
			PostUpdatedEvent(
				accountId = command.accountId,
				content = command.content,
				postId = command.postId,
			)
		)
	}

	@CommandHandler
	fun handle(command: DeletePostCommand) {
		if (accountId != command.accountId) {
			throw AccountMismatchException(
				currentAccountId = command.accountId,
				ownerAccountId = accountId,
			)
		}
		// TODO: Check if user is administrator
		AggregateLifecycle.apply(
			PostDeletedEvent(
				accountId = command.accountId,
				postId = command.postId
			)
		)
	}

	@EventSourcingHandler
	fun on(event: PostCreatedEvent) {
		postId = event.postId
		content = event.content
		accountId = event.accountId
	}

	@EventSourcingHandler
	fun on(event: PostUpdatedEvent) {
		content = event.content
	}

}