package pl.edu.pw.ia.accounts.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.domain.command.CreateAccountCommand

data class CreateAccountRequest(

	@Schema
	@field:NotNull
	val details: CreateAccountRequestInner,

	@Schema(maxLength = 50)
	@field:NotBlank(message = "type cannot be blank")
	@field:Length(max = 50, message = "Maximum allowed type length is 50 characters")
	val type: String,

	@Schema
	@field:NotNull
	val userId: UUID,
) {
	fun toCommand(): CreateAccountCommand {
		return CreateAccountCommand(
			accountId = userId,
			name = details.username,
			email = details.email,
		)
	}
}
