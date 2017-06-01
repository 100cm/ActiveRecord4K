package ActiveRecord4k

import java.util.jar.Pack200.Packer.PASS
import com.sun.deploy.security.CertStore.USER
import org.sql2o.Sql2o
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import entity.Column
import java.sql.ResultSet
import kotlin.reflect.KProperty


/**
 * Created by icepoint1999 on 27/05/2017.
 */
open class ActiveRecord<T> {

    var joins: List<Join> = listOf()
    var wheres: List<Where> = listOf();

    var selects: List<String> = listOf();

    var conn: HikariDataSource = HikariDataSource();

    var results: List<T>? = listOf();

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


    fun where(key: String, value: Any?): T {

        this.wheres = this.wheres.plusElement(Where(key, value))

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

    operator fun get(index: Int) {
        var dataResult: List<T> = listOf()

        //先拿到result
        var sql = SqlBuilder().selectSQl(this as ActiveRecord<Any>)
        var result: ResultSet = this.conn.connection.prepareStatement(sql).executeQuery()

        var names = this.javaClass.declaredFields.map {
            it.name
        }.filter {
            it != "$$" + "delegatedProperties" && it.contains("$" + "delegate")
        }
//        把delegate属性拿出来
        var columns = names.map {
            it.split("$")[0]
        }

        columns.forEach {

            var column_index = result.findColumn(it)

            var clazz = this.javaClass

            var record = clazz.newInstance()

            var field = clazz.getDeclaredField(it + "$" + "delegate")

            field.isAccessible = true

            if (result.next()) {

                var this_filed = field.get(this)


//            代理类
                var column_delegate_filed = field.type.getDeclaredField("column_type")

                column_delegate_filed.isAccessible = true

//            字段类型
                var column_type = column_delegate_filed.get(this_filed)

                var column_value = ""

                when (column_type) {
                    String.javaClass -> {
                        column_value = result.getString(column_index)
                    }

                    Int.javaClass -> {
                        column_value = result.getString(column_index)
                    }

                }

                var column = Column();

                
                column.setValue(record,field as KProperty<*>,column_value)

                field.set(record, column)
                print(record)


            }


        }


    }


}