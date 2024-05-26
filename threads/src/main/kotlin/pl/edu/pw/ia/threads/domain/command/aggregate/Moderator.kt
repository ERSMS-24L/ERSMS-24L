package pl.edu.pw.ia.threads.domain.command.aggregate

import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pw.ia.shared.domain.command.AddModeratorCommand
import pl.edu.pw.ia.shared.domain.command.RemoveModeratorCommand
import pl.edu.pw.ia.shared.domain.event.ModeratorAddedEvent
import pl.edu.pw.ia.shared.domain.event.ModeratorRemovedEvent
import pl.edu.pw.ia.shared.domain.exception.AccountIsNotOwnerException
import pl.edu.pw.ia.shared.domain.query.FindThreadByIdQuery
import pl.edu.pw.ia.shared.domain.view.ThreadView

@Aggregate
internal class Moderator {

	@AggregateIdentifier
	private lateinit var moderatorId: UUID
	private lateinit var accountId: UUID
	private lateinit var threadId: UUID

	@Autowired
	private lateinit var queryGateway: QueryGateway

	private constructor()

	@CommandHandler
	constructor(command: AddModeratorCommand) {
		val threadView = queryGateway.query(FindThreadByIdQuery(command.threadId), ThreadView::class.java).join()
		if(command.accountId != threadView.accountId){
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
	fun handle(command: RemoveModeratorCommand) {
		val threadView = queryGateway.query(FindThreadByIdQuery(threadId), ThreadView::class.java).join()
		if(command.accountId != threadView.accountId){
			throw AccountIsNotOwnerException(command.accountId, threadView.accountId)
		}
		AggregateLifecycle.apply(
			ModeratorRemovedEvent(
				moderatorId = command.moderatorId,
				threadId = threadId,
			)
		)
	}

}