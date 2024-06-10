package pl.edu.pw.ia.accounts.application.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class CreateAccountRequestInner(

	@Schema(maxLength = 50)
	@field:NotBlank(message = "Name cannot be blank")
	@field:Length(max = 50, message = "Maximum allowed name length is 50 characters")
	val username: String,

	@Schema(maxLength = 320)
	@field:Email
	@field:NotBlank(message = "Email cannot be blank")
	@field:Length(max = 50, message = "Maximum allowed email length is 320 characters")
	val email: String,
)