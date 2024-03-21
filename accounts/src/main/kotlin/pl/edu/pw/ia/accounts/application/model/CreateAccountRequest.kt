package pl.edu.pw.ia.accounts.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.UUID
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.domain.command.CreateAccountCommand

data class CreateAccountRequest(

	@Schema(maxLength = 50)
	@field:NotBlank(message = "Name cannot be blank")
	@field:Length(max = 50, message = "Maximum allowed name length is 50 characters")
	val name: String,

	@Schema(maxLength = 320)
	@field:Email
	@field:NotBlank(message = "Email cannot be blank")
	@field:Length(max = 50, message = "Maximum allowed email length is 320 characters")
	val email: String,
) {
	fun toCommand() =
		CreateAccountCommand(
			accountId = UUID.randomUUID(),
			name = name,
			email = email,
		)
}
