package entity

import ActiveRecord4k.ActiveRecord
import ActiveRecord4k.SqlBuilder
import org.sql2o.Sql2o
import kotlin.reflect.KProperty
/**
 * Created by icepoint1999 on 26/05/2017.
 */
class Column {

    operator fun getValue(thisRef: Any, property: KProperty<*>): Any? {

        val con = thisRef.javaClass.superclass.getDeclaredField("conn")

        con.apply {
            isAccessible = true
        }
//
        var conn:Sql2o = con.get(thisRef) as Sql2o

        val sql = SqlBuilder().selectSQl(thisRef as ActiveRecord<Any>)

        val data = conn.createQuery(sql).executeAndFetch(thisRef::class.java);

        data.javaClass.getDeclaredMethod("name")

        println("执行了sql---> "+ thisRef)

        return data
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?): Any? {

        return value;
    }
}