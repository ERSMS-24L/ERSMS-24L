package pl.edu.pw.ia.accounts.domain.command.aggregate

import java.time.Instant
import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.command.CreateAccountCommand
import pl.edu.pw.ia.shared.domain.event.AccountCreatedEvent

@Aggregate
internal class Account {

	@AggregateIdentifier
	private lateinit var accountId: UUID
	private lateinit var name: String
	private lateinit var email: String

	private constructor()

	@CommandHandler
	constructor(command: CreateAccountCommand) {
		AggregateLifecycle.apply(
			AccountCreatedEvent(
				accountId = command.accountId,
				name = command.name,
				email = command.email,
				createdAt = Instant.now(),
			)
		)
	}

	@EventSourcingHandler
	fun on(event: AccountCreatedEvent) {
		accountId = event.accountId
		name = event.name
		email = event.email
	}
}
