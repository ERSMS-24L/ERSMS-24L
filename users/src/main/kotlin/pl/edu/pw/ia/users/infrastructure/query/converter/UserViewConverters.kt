package pl.edu.pw.ia.users.infrastructure.query.converter

import java.util.UUID
import org.springframework.core.convert.converter.Converter
import pl.edu.pw.ia.shared.config.TypeConverter
import pl.edu.pw.ia.users.domain.query.view.UserView
import pl.edu.pw.ia.users.infrastructure.query.entity.UserViewEntity

@TypeConverter
class UserViewToUserViewEntityConverter : Converter<UserView, UserViewEntity> {

	override fun convert(source: UserView): UserViewEntity? =
		UserViewEntity(
			id = source.id.toString(),
			name = source.name,
			email = source.email,
		)
}

@TypeConverter
class UserViewEntityToUserViewConverter : Converter<UserViewEntity, UserView> {

	override fun convert(source: UserViewEntity): UserView? =
		UserView(
			id = UUID.fromString(source.id),
			name = source.name,
			email = source.email,
		)
}
