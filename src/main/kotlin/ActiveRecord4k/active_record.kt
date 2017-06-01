package ActiveRecord4k

import java.util.jar.Pack200.Packer.PASS
import com.sun.deploy.security.CertStore.USER
import org.sql2o.Sql2o
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import entity.Column
import java.sql.ResultSet
import javax.lang.model.type.DeclaredType
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.memberProperties


/**
 * Created by icepoint1999 on 27/05/2017.
 */
open class ActiveRecord<T> {

    var joins: List<Join> = listOf()
    var wheres: List<Where> = listOf();

    var selects: List<String> = listOf();

    var conn: HikariDataSource = HikariDataSource();

    var results: List<T>? = listOf();

    var bind_args:List<Any> = listOf();

    init {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/demo"
        config.username = "root"
        config.password = "atyun123456"
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        val ds = HikariDataSource(config)

        this.conn = ds

    }


    fun where(vararg args: Any?): T {


        if (args[0] is Map<*, *>) {
            (args[0] as Map<*, *>).forEach {
                this.wheres = this.wheres.plusElement(Where(it.key.toString(), it.value))

            }

        } else {
            var w = Where()
            w.sql = args[0].toString()

            var nest_args = args.drop(1)

            w.bindArgs = w.bindArgs.plus(nest_args)

            this.wheres = this.wheres.plusElement(w)
        }

//
        return this as T;
    }


    fun select(columns: List<String>): T {
        this.selects = this.selects.plus(columns)
        return this as T
    }

    fun join(table: Any?, sql: Any? = null): T {

        var table = table?.javaClass.toString()

        var current_table = this.javaClass.toString().toLowerCase()

        when (table) {
            is String -> table = table
        }

        val table_name = "${current_table}s"
        var target_table = "${table}s";
        var table_column = "${current_table}_id";
        var target_table_column = "${table}_id"
        this.joins = this.joins.plusElement(Join(table_name, target_table, table_column, target_table_column))

        return this as T;
    }

    fun find(id: Int?): T {

        return this as T;
    }

    operator fun get(index: Int): T {
        var dataResult: List<T> = listOf()

        //先拿到result
        var sql = SqlBuilder().selectSQl(this as ActiveRecord<Any>)

        var prepareStatement  = this.conn.connection.prepareStatement(sql)

        println(this.bind_args)

        print(sql)

        this.bind_args.forEachIndexed { index, any ->

            when(any){
                Int -> {
                    prepareStatement.setInt(index+1,any as Int)
                }

                String ->{
                    prepareStatement.setString(index+1,any as String)
                }
                else->{
                    prepareStatement.setString(index+1,any as String)
                }
            }

        }


        var result: ResultSet = prepareStatement.executeQuery()

        var names = this.javaClass.declaredFields.map {
            it.name
        }.filter {
            it != "$$" + "delegatedProperties" && it.contains("$" + "delegate")
        }
//        把delegate属性拿出来
        var columns = names.map {
            it.split("$")[0]
        }

        while (result.next()) {

            var clazz = this.javaClass

            var record = clazz.newInstance()

            columns.forEach {

                var current_column = it

                var field = clazz.getDeclaredField(it + "$" + "delegate")

                field.isAccessible = true


                var this_filed = field.get(this)


//            代理类
                var column_delegate_filed = field.type.getDeclaredField("column_type")

                column_delegate_filed.isAccessible = true

//            字段类型
                var column_type = column_delegate_filed.get(this_filed)

                var column_value = ""

                when (column_type) {
                    String.javaClass -> {
                        column_value = result.getString(it)
                    }

                    Int.javaClass -> {
                        column_value = result.getString(it)
                    }

                }


                var kproperties = record.javaClass.kotlin.declaredMemberProperties

                val property = kproperties.find { it.name == current_column }
                if (property is KMutableProperty<*>) {
                    println("it worked")
                    property.setter.call(record, column_value)
                }

//                setter.invoke(record, column_value)
            }


            dataResult = dataResult.plusElement(record as T)


        }

        return dataResult[index];


    }


}