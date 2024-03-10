package pl.edu.pw.ia.shared.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.springframework.core.convert.converter.ConverterRegistry
import org.springframework.stereotype.Component

@Configuration
class ConverterConfiguration(
	@TypeConverter private val converters: Set<Converter<*, *>>,
	@TypeConverter private val converterFactories: Set<ConverterFactory<*, *>>,
	private val converterRegistry: ConverterRegistry,
) {

	@PostConstruct
	fun conversionService() {
		for (converter in converters) {
			converterRegistry.addConverter(converter)
		}
		for (factory in converterFactories) {
			converterRegistry.addConverterFactory(factory)
		}
	}
}

@Target(
	allowedTargets = [
		AnnotationTarget.TYPE,
		AnnotationTarget.CLASS,
		AnnotationTarget.FIELD,
		AnnotationTarget.VALUE_PARAMETER
	]
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class TypeConverter
