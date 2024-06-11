package pl.edu.pw.ia.shared.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

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
						.pathMatchers(HttpMethod.GET, "/api/v1/threads/**").permitAll()
						.pathMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
						.pathMatchers(HttpMethod.GET, "/api/v1/votes**").permitAll()
						.pathMatchers("/api/v1/accounts").permitAll() // TODO: consider allowing only keycloak here
						.pathMatchers("accounts/api/v1/accounts").permitAll() // TODO: consider allowing only keycloak here
						.anyExchange().authenticated()
			}
			.oauth2ResourceServer {
				it.jwt {
					it.jwtAuthenticationConverter(jwtAuthenticationConverter())
				}
			}
			.build()
	@Bean
	fun jwtAuthenticationConverter(): Converter<Jwt, Mono<AbstractAuthenticationToken>> {
		val grantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
		grantedAuthoritiesConverter.setAuthoritiesClaimName("roles")
		grantedAuthoritiesConverter.setAuthorityPrefix("")
		val jwtAuthenticationConverter = JwtAuthenticationConverter()
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)
		return ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter)
	}

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
