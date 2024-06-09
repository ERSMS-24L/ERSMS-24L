package pl.edu.pw.ia.accounts.application.model

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID
import org.hibernate.validator.constraints.Length
import pl.edu.pw.ia.shared.domain.command.CreateAccountCommand

data class CreateAccountRequest(

	@Schema
	@field:NotNull
	val representation: String,

	@Schema(maxLength = 50)
	@field:NotBlank(message = "operationType cannot be blank")
	@field:Length(max = 50, message = "Maximum allowed operationType length is 50 characters")
	val operationType: String,

	@Schema(maxLength = 50)
	@field:NotBlank(message = "resourceType cannot be blank")
	@field:Length(max = 50, message = "Maximum allowed resourceType length is 50 characters")
	val resourceType: String,

	@Schema(maxLength = 100)
	@field:NotBlank(message = "resourcePath cannot be blank")
	@field:Length(max = 100, message = "Maximum allowed resourcePath length is 100 characters")
	val resourcePath: String,
) {
	fun toCommand(): CreateAccountCommand {
		val objectMapper = ObjectMapper()
		val actualRepresentation: CreateAccountRequestInner = objectMapper.readValue(representation.replace("\\", ""), CreateAccountRequestInner::class.java)
		return CreateAccountCommand(
			accountId = UUID.fromString(resourcePath.replace("users/", "")),
			name = actualRepresentation.username,
			email = actualRepresentation.email,
		)
	}
}
