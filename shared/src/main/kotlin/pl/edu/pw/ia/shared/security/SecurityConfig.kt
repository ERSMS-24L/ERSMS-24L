package pl.edu.pw.ia.shared.security

import java.security.interfaces.RSAPublicKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Profile("!disableSecurity")
class SecurityConfig(
		@Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
		private val jwkSetUri: String,
		@Value("\${springdoc.api-docs.path}")
		private val restApiDocPath: String,
		@Value("\${springdoc.swagger-ui.path}")
		private val swaggerPath: String
) {

	@Bean
	fun securityFilterChain(@Autowired http: ServerHttpSecurity): SecurityWebFilterChain = http
			.cors(Customizer.withDefaults())
			.csrf { it.disable() }
			.exceptionHandling {
				it
						.authenticationEntryPoint(BearerTokenServerAuthenticationEntryPoint())
						.accessDeniedHandler(BearerTokenServerAccessDeniedHandler())
			}
			.authorizeExchange {
				it
						.pathMatchers("$restApiDocPath**").permitAll()
						.pathMatchers("$restApiDocPath/**").permitAll()
						.pathMatchers(swaggerPath).permitAll()
						.pathMatchers("/webjars/swagger-ui/**").permitAll()
						.pathMatchers("/actuator/**").permitAll()
						.anyExchange().authenticated()
			}
			.oauth2ResourceServer {
				it.jwt(Customizer.withDefaults())
			}
			.build()

	@Bean
	fun jwtDecoder(): ReactiveJwtDecoder {
		return NimbusReactiveJwtDecoder
			.withJwkSetUri(jwkSetUri)
			.build()
	}

	@Bean
	@Primary
	fun jwtAuthenticationManager(@Autowired jwtDecoder: ReactiveJwtDecoder): JwtReactiveAuthenticationManager {
		return JwtReactiveAuthenticationManager(jwtDecoder)
	}
}

@Configuration
@EnableWebFluxSecurity
@Profile("disableSecurity")
class TestSecurityConfig {

	@Bean
	fun securityFilterChain(@Autowired http: ServerHttpSecurity): SecurityWebFilterChain = http
			.cors { it.disable() }
			.csrf { it.disable() }
			.authorizeExchange {
				it.anyExchange().permitAll()
			}
			.build()
}
