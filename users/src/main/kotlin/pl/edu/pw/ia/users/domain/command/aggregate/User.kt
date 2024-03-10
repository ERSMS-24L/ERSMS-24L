package pl.edu.pw.ia.users.domain.command.aggregate

import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.command.CreateUserCommand
import pl.edu.pw.ia.shared.domain.event.UserCreatedEvent

@Aggregate
internal class User {

	@AggregateIdentifier
	private lateinit var id: UUID
	private lateinit var name: String
	private lateinit var email: String

	private constructor()

	@CommandHandler
	constructor(command: CreateUserCommand) {
		AggregateLifecycle.apply(
			UserCreatedEvent(
				userId = command.userId,
				name = command.name,
				email = command.email,
			)
		)
	}

	@EventSourcingHandler
	fun on(event: UserCreatedEvent) {
		id = event.userId
		name = event.name
		email = event.email
	}
}
