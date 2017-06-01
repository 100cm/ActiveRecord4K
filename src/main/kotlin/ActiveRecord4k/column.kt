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
class Column(var type:Class<Any> = String.javaClass) {

    var column_type = this.type

    operator fun getValue(thisRef: Any, property: KProperty<*>): Any? {

//        val con = thisRef.javaClass.superclass.getDeclaredField("conn")
//
//        con.apply {
//            isAccessible = true
//        }
//
//        var valueType =  type.javaClass
////
//        var conn:Sql2o = con.get(thisRef) as Sql2o
//
//        val sql = SqlBuilder().selectSQl(thisRef as ActiveRecord<Any>)
//
//        val data = conn.createQuery(sql).executeAndFetch(thisRef::class.java);
//
//        data.javaClass.getDeclaredMethod("name")
//
//        println("执行了sql---> "+ thisRef)
//
//        return data

        return thisRef
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?): Any? {

        return value;
    }
}