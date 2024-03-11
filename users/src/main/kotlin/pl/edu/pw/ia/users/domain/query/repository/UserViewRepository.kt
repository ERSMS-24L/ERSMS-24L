package pl.edu.pw.ia.users.domain.query.repository

import java.util.UUID
import pl.edu.pw.ia.users.domain.query.view.UserView

interface UserViewRepository {

	fun save(user: UserView)

	fun findById(id: UUID): UserView?
}
