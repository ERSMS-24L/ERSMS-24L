package pl.edu.pw.ia.threads.domain.command.aggregate

import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.command.AddModeratorCommand
import pl.edu.pw.ia.shared.domain.command.RemoveModeratorCommand
import pl.edu.pw.ia.shared.domain.event.ModeratorAddedEvent
import pl.edu.pw.ia.shared.domain.event.ModeratorRemovedEvent
import pl.edu.pw.ia.shared.domain.exception.AccountIsNotOwnerException
import pl.edu.pw.ia.shared.domain.exception.ThreadNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindThreadByIdQuery
import pl.edu.pw.ia.shared.domain.view.ThreadView

@Aggregate
internal class Moderator {

	@AggregateIdentifier
	private lateinit var moderatorId: UUID
	private lateinit var accountId: UUID
	private lateinit var threadId: UUID

	private constructor()

	@CommandHandler
	constructor(command: AddModeratorCommand, queryGateway: QueryGateway) {
		val threadView = queryGateway.query(
			FindThreadByIdQuery(command.threadId), ThreadView::class.java
		).join() ?: throw ThreadNotFoundException(command.threadId)
		if (command.accountId != threadView.accountId) {
			throw AccountIsNotOwnerException(command.accountId, threadView.accountId)
		}
		AggregateLifecycle.apply(
			ModeratorAddedEvent(
				moderatorId = command.moderatorId,
				threadId = command.threadId,
				accountId = command.subjectAccountId
			)
		)
	}

	@CommandHandler
	fun handle(command: RemoveModeratorCommand, queryGateway: QueryGateway) {
		// TODO: we could store the thread owner in the aggregate (but we would have to modify the event)
		val threadView = queryGateway.query(
			FindThreadByIdQuery(threadId), ThreadView::class.java
		).join() ?: throw ThreadNotFoundException(threadId)
		if (command.accountId != threadView.accountId) {
			throw AccountIsNotOwnerException(command.accountId, threadView.accountId)
		}
		AggregateLifecycle.apply(
			ModeratorRemovedEvent(
				moderatorId = command.moderatorId,
				threadId = threadId,
			)
		)
	}

	@EventSourcingHandler
	fun on(event: ModeratorAddedEvent) {
		moderatorId = event.moderatorId
		accountId = event.accountId
		threadId = event.threadId
	}
}
