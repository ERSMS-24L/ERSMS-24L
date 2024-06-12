package pl.edu.pw.ia.posts.domain.command.aggregate

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.exception.AccountMismatchException
import java.time.Instant
import java.util.UUID
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import pl.edu.pw.ia.shared.domain.command.CreatePostCommand
import pl.edu.pw.ia.shared.domain.command.DeletePostByAdminCommand
import pl.edu.pw.ia.shared.domain.command.DeletePostCommand
import pl.edu.pw.ia.shared.domain.command.UpdatePostCommand
import pl.edu.pw.ia.shared.domain.event.PostCreatedEvent
import pl.edu.pw.ia.shared.domain.event.PostDeletedEvent
import pl.edu.pw.ia.shared.domain.event.PostUpdatedEvent
import pl.edu.pw.ia.shared.domain.query.FindModeratorByThreadAndAccountIdQuery
import pl.edu.pw.ia.shared.domain.query.FindPostByIdQuery
import pl.edu.pw.ia.shared.domain.query.FindThreadByIdQuery
import pl.edu.pw.ia.shared.domain.view.ModeratorView
import pl.edu.pw.ia.shared.domain.view.PostView
import pl.edu.pw.ia.shared.domain.view.ThreadView

@Aggregate
internal class Post {

	@AggregateIdentifier
	private lateinit var postId: UUID
	private lateinit var content: String
	private lateinit var accountId: UUID
	private lateinit var threadId: UUID

	private constructor()

	@CommandHandler
	constructor(command: CreatePostCommand) {
		AggregateLifecycle.apply(
			PostCreatedEvent(
				postId = command.postId,
				accountId = command.accountId,
				threadId = command.threadId,
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
				threadId = threadId,
				content = command.content,
				postId = command.postId,
			)
		)
	}

	@CommandHandler
	fun handle(command: DeletePostCommand, reactorQueryGateway: ReactorQueryGateway) {
		var isOwner = false
		var isModerator = false
		val isPostCreator = accountId == command.accountId
		val threadId = reactorQueryGateway.query(FindPostByIdQuery(command.postId), ResponseTypes.instanceOf(PostView::class.java)).block()?.threadId
		if(threadId != null) {
			val threadView = reactorQueryGateway.query(FindThreadByIdQuery(threadId), ResponseTypes.instanceOf(ThreadView::class.java)).block()
			if(threadView != null) {
				isOwner = threadView.accountId == command.accountId
			}
			val moderatorView = reactorQueryGateway.query(FindModeratorByThreadAndAccountIdQuery(threadId=threadId, accountId=command.accountId), ResponseTypes.instanceOf(ModeratorView::class.java)).block()
			if(moderatorView != null) {
				isModerator = true
			}
		}
		if(isOwner || isModerator || isPostCreator){
			AggregateLifecycle.apply(
				PostDeletedEvent(
					accountId = command.accountId,
					postId = command.postId,
				)
			)
		} else {
			throw AccountMismatchException(
				currentAccountId = command.accountId,
				ownerAccountId = accountId,
			)
		}
	}

	@CommandHandler
	fun handle(command: DeletePostByAdminCommand) {
		AggregateLifecycle.apply(
			PostDeletedEvent(
				accountId = command.accountId,
				postId = command.postId,
			)
		)
	}

	@EventSourcingHandler
	fun on(event: PostCreatedEvent) {
		postId = event.postId
		content = event.content
		accountId = event.accountId
		threadId = event.threadId
	}

	@EventSourcingHandler
	fun on(event: PostUpdatedEvent) {
		content = event.content
	}

}