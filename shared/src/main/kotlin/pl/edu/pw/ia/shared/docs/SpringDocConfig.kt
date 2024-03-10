package pl.edu.pw.ia.shared.docs

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
		type = SecuritySchemeType.HTTP,
		name = "Bearer",
		scheme = "bearer"
)
class SpringDocConfig
