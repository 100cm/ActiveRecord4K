package entity

import ActiveRecord4k.ActiveRecord
import ActiveRecord4k.SqlBuilder
import com.sun.org.apache.bcel.internal.util.ClassLoader
import org.sql2o.Sql2o
import sun.plugin2.liveconnect.JavaClass
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Created by icepoint1999 on 26/05/2017.
 */
class Column(var type: KClass<*> = String::class) {

    var column_type = this.type

    var column_value: Any? = null

    operator fun getValue(thisRef: Any, property: KProperty<*>): Any? {
         thisRef as ActiveRecord<Any>

        if (thisRef.is_list) {
            return this.column_value

        } else {
            var obj =  thisRef[0]
            /**
             * 获取代理
             */
            var property = obj?.javaClass?.getDeclaredField(property.name+"$"+"delegate")
            property?.isAccessible = true
            var column_obj = property?.get(obj)
            var column = property?.get(obj)?.javaClass?.getDeclaredField("column_value")
            column?.isAccessible = true
            return column?.get(column_obj)
        }


    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Any?): Any? {
        this.column_value = value
        return thisRef
    }
}