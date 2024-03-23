package pl.edu.pw.ia.accounts.infrastructure.query.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pw.ia.shared.domain.view.AccountView

@Document(collection = "accountViews")
data class AccountViewEntity(
	@Id
	val id: String,
	val name: String,
	val email: String,
) {
	fun toDomain(): AccountView =
		AccountView(
			accountId = UUID.fromString(id),
			name = name,
			email = email,
		)

	companion object {
		fun AccountView.toEntity(): AccountViewEntity =
			AccountViewEntity(
				id = accountId.toString(),
				name = name,
				email = email,
			)
	}
}
