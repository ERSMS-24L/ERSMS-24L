package pl.edu.pw.ia.shared.config

import java.lang.reflect.Type
import java.util.concurrent.Future
import org.axonframework.common.ReflectionUtils
import org.axonframework.messaging.responsetypes.AbstractResponseType
import org.springframework.data.domain.Page

@Suppress("UNCHECKED_CAST")
class PageResponseType<R>(expectedResponseType: Class<R>) : AbstractResponseType<Page<R>>(expectedResponseType) {

	override fun matches(responseType: Type): Boolean {
		val unwrapped: Type = ReflectionUtils.unwrapIfType(responseType, Future::class.java, Page::class.java)
		return isGenericAssignableFrom(unwrapped) || isAssignableFrom(unwrapped)
	}

	override fun convert(response: Any): Page<R> {
		return response as Page<R>
	}

	override fun responseMessagePayloadType(): Class<Page<R>> {
		return Page::class.java as Class<Page<R>>
	}

	override fun toString(): String {
		return "PageResponseType(expectedResponseType=$expectedResponseType)"
	}
}
