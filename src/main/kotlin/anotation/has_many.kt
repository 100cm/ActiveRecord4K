package anotation

import java.lang.annotation.ElementType
import kotlin.reflect.KClass

/**
 * Created by atyun on 6/2/17.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class has_many(val table: KClass<*>,val base: String = "id", val target: String = "id")

