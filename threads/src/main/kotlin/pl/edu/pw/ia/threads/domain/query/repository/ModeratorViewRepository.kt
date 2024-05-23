package pl.edu.pw.ia.threads.domain.query.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pl.edu.pw.ia.shared.domain.view.ModeratorView

interface ModeratorViewRepository {

	fun save(moderator: ModeratorView)

	fun findByAccountIdAndThreadId(accountId: UUID, threadId: UUID): ModeratorView?

	fun findByThreadId(threadId: UUID, pageable: Pageable): Page<ModeratorView>

	fun delete(moderatorId: UUID)
}