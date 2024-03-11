package pl.edu.pw.ia.shared.config

import kotlin.reflect.full.companionObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
	return javaClass.enclosingClass?.takeIf {
		it.kotlin.companionObject?.java == javaClass
	} ?: javaClass
}

fun lazyLogger(forClass: Class<*>): Lazy<Logger> =
	lazy { getLogger(getClassForLogging(forClass)) }

fun <T : Any> T.logger(): Lazy<Logger> = lazyLogger(javaClass)
