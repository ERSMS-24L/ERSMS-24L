package pl.edu.pw.ia.shared.config

import com.thoughtworks.xstream.XStream
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfiguration {

	@Bean
	fun xStream(): XStream =
		XStream().apply {
			allowTypesByWildcard(
				arrayOf(
					"pl.edu.pw.ia.**",
					"org.springframework.data.domain.**"
				)
			)
		}
}
