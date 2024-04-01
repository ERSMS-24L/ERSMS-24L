package pl.edu.pw.ia.threads.domain.command.aggregate

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.edu.pw.ia.shared.domain.command.CreateThreadCommand
import pl.edu.pw.ia.shared.domain.command.UpdateThreadCommand
import pl.edu.pw.ia.shared.domain.event.ThreadCreatedEvent
import java.time.Instant
import java.util.UUID

@Aggregate
internal class Thread {

    @AggregateIdentifier
    private lateinit var threadId: UUID
    private lateinit var title: String
    private lateinit var accountId: UUID

    private constructor()

    @CommandHandler
    constructor(command: CreateThreadCommand) {
        AggregateLifecycle.apply(
            ThreadCreatedEvent(
                accountId = command.accountId,
                title = command.title,
                threadId = command.threadId,
                createdAt = Instant.now(),
            )
        )
    }

    @CommandHandler
    fun handle(command: UpdateThreadCommand) {}

    @EventSourcingHandler
    fun on(event: ThreadCreatedEvent) {
        threadId = event.threadId
        title = event.title
        accountId = event.accountId
    }

}