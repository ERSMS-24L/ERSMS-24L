package pl.edu.pw.ia.users.infrastructure.query.converter

import java.util.UUID
import org.springframework.core.convert.converter.Converter
import pl.edu.pw.ia.shared.config.TypeConverter
import pl.edu.pw.ia.users.domain.query.view.AccountView
import pl.edu.pw.ia.users.infrastructure.query.entity.AccountViewEntity

@TypeConverter
class AccountViewToAccountViewEntityConverter : Converter<AccountView, AccountViewEntity> {

	override fun convert(source: AccountView): AccountViewEntity? =
		AccountViewEntity(
			id = source.accountId.toString(),
			name = source.name,
			email = source.email,
		)
}

@TypeConverter
class AccountViewEntityToAccountViewConverter : Converter<AccountViewEntity, AccountView> {

	override fun convert(source: AccountViewEntity): AccountView? =
		AccountView(
			accountId = UUID.fromString(source.id),
			name = source.name,
			email = source.email,
		)
}
