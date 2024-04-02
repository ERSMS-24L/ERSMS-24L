package pl.edu.pw.ia.threads.domain.query.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pl.edu.pw.ia.shared.domain.view.ThreadView
import java.time.Instant
import java.util.UUID

interface ThreadViewRepository {

    fun save(thread: ThreadView)

    fun findById(id: UUID): ThreadView?

    fun findRecent(date: Instant, pageable: Pageable): Page<ThreadView>

    fun findByAuthor(accountId: UUID, pageable: Pageable): Page<ThreadView>

    fun findByTitle(title: String, pageable: Pageable): Page<ThreadView>

    fun delete(threadId: UUID)
}