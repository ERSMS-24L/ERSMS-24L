package pl.edu.pw.ia.threads.domain.command.aggregate

import java.time.Instant
import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pw.ia.shared.domain.command.BanAccountCommand
import pl.edu.pw.ia.shared.domain.command.UnbanAccountCommand
import pl.edu.pw.ia.shared.domain.event.AccountBannedEvent
import pl.edu.pw.ia.shared.domain.event.AccountUnbannedEvent
import pl.edu.pw.ia.shared.domain.exception.AccountIsNotOwnerException
import pl.edu.pw.ia.shared.domain.exception.ModeratorNotFoundException
import pl.edu.pw.ia.shared.domain.query.FindModeratorByThreadAndAccountIdQuery
import pl.edu.pw.ia.shared.domain.query.FindThreadByIdQuery
import pl.edu.pw.ia.shared.domain.view.ModeratorView
import pl.edu.pw.ia.shared.domain.view.ThreadView

@Aggregate
internal class BannedUser {

	@AggregateIdentifier
	private lateinit var bannedUserId: UUID
	private lateinit var accountId: UUID
	private lateinit var threadId: UUID

	@Autowired
	private lateinit var queryGateway: QueryGateway

	private constructor()

	@CommandHandler
	constructor(command: BanAccountCommand) {
		val threadView = queryGateway.query(FindThreadByIdQuery(command.threadId), ThreadView::class.java).join()
		if(command.accountId != threadView.accountId){
			throw AccountIsNotOwnerException(command.accountId, threadView.accountId)
		}
		val moderatorView = queryGateway.query(
			FindModeratorByThreadAndAccountIdQuery(command.threadId, command.accountId),
			ModeratorView::class.java
		).join() ?: throw ModeratorNotFoundException(command.threadId, command.accountId)
		AggregateLifecycle.apply(
			AccountBannedEvent(
				bannedUserId = command.bannedUserId,
				threadId = command.threadId,
				accountId = command.subjectAccountId,
				createdAt = Instant.now(),
			)
		)
	}

	@CommandHandler
	fun handle(command: UnbanAccountCommand) {
		val threadView = queryGateway.query(FindThreadByIdQuery(threadId), ThreadView::class.java).join()
		if(command.accountId != threadView.accountId){
			throw AccountIsNotOwnerException(command.accountId, threadView.accountId)
		}
		val moderatorView = queryGateway.query(
			FindModeratorByThreadAndAccountIdQuery(threadId, command.accountId),
			ModeratorView::class.java
		).join() ?: throw ModeratorNotFoundException(threadId, command.accountId)
		AggregateLifecycle.apply(
			AccountUnbannedEvent(
				bannedUserId = command.bannedUserId,
				threadId = threadId
			)
		)
	}

}