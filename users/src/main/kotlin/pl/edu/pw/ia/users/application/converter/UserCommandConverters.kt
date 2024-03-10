package pl.edu.pw.ia.users.application.converter

import java.util.UUID
import org.springframework.core.convert.converter.Converter
import pl.edu.pw.ia.shared.config.TypeConverter
import pl.edu.pw.ia.shared.domain.command.CreateUserCommand
import pl.edu.pw.ia.users.application.model.CreateUserRequest

@TypeConverter
class CreateUserRequestToCreateUserCommandConverter : Converter<CreateUserRequest, CreateUserCommand> {

	override fun convert(source: CreateUserRequest): CreateUserCommand? =
		CreateUserCommand(
			userId = UUID.randomUUID(),
			name = source.name,
			email = source.email,
		)
}
