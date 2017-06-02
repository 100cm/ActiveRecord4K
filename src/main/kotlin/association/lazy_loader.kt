package association

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Created by atyun on 6/2/17.
 */
class LazyLoad(val t:KClass<*>) {

    var column_value: Any? = null

    operator fun getValue(thisRef: Any, property: KProperty<*>): Any? {

        print(thisRef)

        return this.column_value

    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Any?): Any? {
        this.column_value = value
        return thisRef
    }

}