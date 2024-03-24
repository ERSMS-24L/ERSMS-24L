package pl.edu.pw.ia.accounts.domain.query.repository

import java.util.UUID
import pl.edu.pw.ia.shared.domain.view.AccountView

interface AccountViewRepository {

	fun save(account: AccountView)

	fun findById(id: UUID): AccountView?
}
