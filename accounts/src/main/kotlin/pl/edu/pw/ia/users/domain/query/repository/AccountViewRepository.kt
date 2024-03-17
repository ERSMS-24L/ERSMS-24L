package pl.edu.pw.ia.users.domain.query.repository

import java.util.UUID
import pl.edu.pw.ia.users.domain.query.view.AccountView

interface AccountViewRepository {

	fun save(user: AccountView)

	fun findById(id: UUID): AccountView?
}
