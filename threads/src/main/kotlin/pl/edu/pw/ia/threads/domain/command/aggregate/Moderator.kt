package pl.edu.pw.ia.threads.domain.command.aggregate

import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.command.AddModeratorCommand
import pl.edu.pw.ia.shared.domain.command.RemoveModeratorCommand
import pl.edu.pw.ia.shared.domain.event.ModeratorAddedEvent
import pl.edu.pw.ia.shared.domain.event.ModeratorRemovedEvent

@Aggregate
internal class Moderator {

	@AggregateIdentifier
	private lateinit var moderatorId: UUID
	private lateinit var accountId: UUID
	private lateinit var threadId: UUID

	private constructor()

	@CommandHandler
	constructor(command: AddModeratorCommand) {
		// TODO: Check if command.accountId is author of command.threadId
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
		// TODO: Check if command.accountId is author of command.threadId
		AggregateLifecycle.apply(
			ModeratorRemovedEvent(
				moderatorId = command.moderatorId,
			)
		)
	}

}