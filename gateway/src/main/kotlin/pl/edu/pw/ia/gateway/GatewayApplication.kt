package pl.edu.pw.ia.gateway

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.config.EnableWebFlux


@EnableWebFlux
@SpringBootApplication
@EnableDiscoveryClient
class GatewayApplication {

	@Bean
	fun routeLocator(builder: RouteLocatorBuilder): RouteLocator {
		return builder.routes()
			.route("accounts") { predicate ->
				predicate.path("/accounts/**")
					.filters { it.stripPrefix(1) }
					.uri("lb://ACCOUNTS")
			}
			.route("posts") { predicate ->
				predicate.path("/posts/**")
					.filters { it.stripPrefix(1) }
					.uri("lb://POSTS")
			}
			.route("threads") { predicate ->
				predicate.path("/threads/**")
					.filters { it.stripPrefix(1) }
					.uri("lb://THREADS")
			}
			.route("discovery") { predicate ->
				predicate.path("/discovery/**")
					.filters { it.stripPrefix(1) }
					.uri("lb://DISCOVERY")
			}
			.build()
	}

	@Bean
	fun defaultCustomizer(): Customizer<ReactiveResilience4JCircuitBreakerFactory> {
		return Customizer<ReactiveResilience4JCircuitBreakerFactory> { factory ->
			factory.configureDefault { id ->
				Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
					.timeLimiterConfig(
						TimeLimiterConfig.custom()
							.timeoutDuration(4.seconds.toJavaDuration())
							.build()
					)
					.build()
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<GatewayApplication>(*args)
}
