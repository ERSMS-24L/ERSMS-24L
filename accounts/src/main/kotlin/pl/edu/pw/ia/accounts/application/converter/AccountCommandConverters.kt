package pl.edu.pw.ia.accounts.application.converter

import java.util.UUID
import org.springframework.core.convert.converter.Converter
import pl.edu.pw.ia.shared.config.TypeConverter
import pl.edu.pw.ia.shared.domain.command.CreateAccountCommand
import pl.edu.pw.ia.accounts.application.model.CreateAccountRequest

@TypeConverter
class CreateAccountRequestToCreateAccountCommandConverter : Converter<CreateAccountRequest, CreateAccountCommand> {

	override fun convert(source: CreateAccountRequest): CreateAccountCommand? =
		CreateAccountCommand(
			accountId = UUID.randomUUID(),
			name = source.name,
			email = source.email,
		)
}
