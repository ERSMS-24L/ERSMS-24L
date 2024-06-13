package pl.edu.pw.ia.threads.domain.query.repository

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pl.edu.pw.ia.shared.domain.view.BannedUserView

interface BannedUserViewRepository {

	fun save(bannedUser: BannedUserView)

	fun findByAccountIdAndThreadId(accountId: UUID, threadId: UUID): BannedUserView?

	fun findByThreadId(threadId: UUID, pageable: Pageable): Page<BannedUserView>

	fun findByBannedUserId(bannedUserId: UUID): BannedUserView?

	fun delete(bannedUserId: UUID)
}