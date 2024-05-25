package pl.edu.pw.ia.threads.domain.command.aggregate

import java.time.Instant
import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.command.BanAccountCommand
import pl.edu.pw.ia.shared.domain.command.UnbanAccountCommand
import pl.edu.pw.ia.shared.domain.event.AccountBannedEvent
import pl.edu.pw.ia.shared.domain.event.AccountUnbannedEvent

@Aggregate
internal class BannedUser {

	@AggregateIdentifier
	private lateinit var bannedUserId: UUID
	private lateinit var accountId: UUID
	private lateinit var threadId: UUID

	private constructor()

	@CommandHandler
	constructor(command: BanAccountCommand) {
		// TODO: Check if command.accountId is author or moderator(?) of command.threadId
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
		// TODO: Check if command.accountId is author or moderator (?) of command.threadId
		AggregateLifecycle.apply(
			AccountUnbannedEvent(
				bannedUserId = command.bannedUserId,
				threadId = threadId
			)
		)
	}

}