package pl.edu.pw.ia.gateway

import java.util.function.Predicate
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class NotPathRoutePredicateFactory : PathRoutePredicateFactory() {

	@Override
	override fun apply(config: Config): Predicate<ServerWebExchange> = super.apply(config).negate()
}
