package entity

import ActiveRecord4k.ActiveRecord
import ActiveRecord4k.SqlBuilder
import com.sun.org.apache.bcel.internal.util.ClassLoader
import org.sql2o.Sql2o
import sun.plugin2.liveconnect.JavaClass
import kotlin.reflect.KProperty

/**
 * Created by icepoint1999 on 26/05/2017.
 */
class Column(var type: Class<Any> = String.javaClass) {

    var column_type = this.type

    var column_value :Any? = null

    operator fun getValue(thisRef: Any, property: KProperty<*>): Any? {

        return this.column_value

    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Any?): Any? {
        this.column_value = value
        return thisRef
    }
}