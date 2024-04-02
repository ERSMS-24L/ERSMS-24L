package pl.edu.pw.ia.threads.infrastructure.query.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pw.ia.shared.domain.view.ThreadView
import java.time.Instant
import java.util.UUID

@Document(collection = "threadViews")
data class ThreadViewEntity(
    @Id
    val threadId: String,
    val title: String,
    val accountId: String,
    val post: String,
    val username: String,
    val lastModified: Instant
) {

    fun toDomain(): ThreadView =
        ThreadView(
            accountId = UUID.fromString(accountId),
            threadId = UUID.fromString(threadId),
            post = post,
            title = title,
            username = username,
            lastModified = lastModified,
        )

    companion object {
        fun ThreadView.toEntity(): ThreadViewEntity =
            ThreadViewEntity(
                threadId = threadId.toString(),
                title = title,
                accountId = accountId.toString(),
                post = post,
                username = username,
                lastModified = lastModified,
            )
    }
}