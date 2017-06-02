package anotation

import kotlin.reflect.KClass

/**
 * Created by atyun on 6/2/17.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class belongs_to(val table:KClass<*>,val base:String = "id",val target:String = "id" )