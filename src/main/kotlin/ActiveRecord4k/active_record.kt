package ActiveRecord4k

import anotation.belongs_to
import anotation.has_many
import association.Relation
import association.RelationType
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import org.apache.log4j.Logger
import type.ActiveList
import java.sql.ResultSet
import kotlin.reflect.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinProperty


/**
 * Created by icepoint1999 on 27/05/2017.
 */
open class ActiveRecord<T> {

    var joins: List<Join> = listOf()

    var wheres: List<Where> = listOf();

    var selects: List<String> = listOf();

    var conn: HikariDataSource = HikariDataSource();

    var results: List<T>? = listOf();

    var bind_args: List<Any?> = listOf();

    val logger = Logger.getLogger(ActiveRecord::class.java)

    var associations: List<Relation> = listOf();

    var limit: String? = null

    var is_list: Boolean = true

    init {

        /**
         * 扫描所有注解 并且关联关系加入
         */
        var self = this;

        var self_class = this::class

        var init_properties = self_class.declaredMemberProperties

        init_properties.forEach {

            var prop_annotations = it.annotations

            prop_annotations.forEach {

                var current_annotation = it

                when (it.annotationClass) {
                    has_many::class -> {
                        var rel = Relation()
                        current_annotation = (current_annotation as has_many)
                        rel.relation_type = RelationType().HAS_MANY
                        rel.base_class = "${self_class.simpleName?.toLowerCase()}"
                        rel.target_class = "${current_annotation.table.simpleName?.toLowerCase()}s"
                        rel.target_column = "${self_class.simpleName?.toLowerCase()}_${current_annotation.base}"
                        self.associations = self.associations.plusElement(rel)

                    }
                    belongs_to::class -> {
                        var current_annotation = (current_annotation as belongs_to)
                        var rel = Relation()
                        rel.relation_type = RelationType().BELONGS_TO
                        rel.base_class = "${self_class.simpleName?.toLowerCase()}"
                        rel.target_class = "${current_annotation.table.simpleName?.toLowerCase()}"
                        rel.base_column = "${current_annotation.table.simpleName?.toLowerCase()}_${current_annotation.target}"
                        self.associations = self.associations.plusElement(rel)
                    }

                }


            }

        }


        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/demo?useSSL=false"
        config.username = "root"
        config.password = "root"
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        val ds = HikariDataSource(config)


        this.conn = ds

    }


    fun where(vararg args: Any): T {

        this.bind_args = listOf();

        if (args[0] !is String) {

            args.forEach {
                var pair = (it as Pair<String, *>)
                this.wheres = this.wheres.plusElement(Where(pair.first, pair.second))
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

    fun joins(vararg args: String): T {
        args.forEach {

            var current_join = it;
            var relation_type = RelationType().BELONGS_TO
            var target_table = it
//            复数 has_many
            if (it.endsWith("s")) {
                relation_type = RelationType().HAS_MANY;
            } else {
                target_table += "s"
            }

            //找到关联
            var relation: Relation? = this.associations.find {
                (it.base_class == this::class.simpleName?.toLowerCase() && it.target_class == current_join && it.relation_type == relation_type)
            }

            if (relation == null) {
                throw Exception("Association named ${it} was not found on ${this::class.simpleName} perhaps you misspelled it?")
            }
            /**
             * build join sql
             */
            var join_sql = "INNER JOIN `${target_table}` ON " +
                    " `${relation.base_class}s`.${relation.base_column} = `${target_table}`.${relation.target_column} "

            this.joins = this.joins.plusElement(Join(join_sql))

        }
        return this as T;
    }

    fun find(id: Int?): T {

        return this as T;
    }

    operator fun get(index: Int): T? {
        var dataResult: List<T> = listOf()

        var base_table = "${this.javaClass.simpleName.toLowerCase()}s"

        //先拿到result
        var sql = SqlBuilder().selectSQl(this as ActiveRecord<Any>)



        logger.info("execute SQL ->  ${sql} ,${this.bind_args}")

        var prepareStatement = this.conn.connection.prepareStatement(sql)


        this.bind_args.forEachIndexed { index, any ->

            when (any!!::class) {
                Int::class -> {
                    prepareStatement.setInt(index + 1, any as Int)
                }

                String::class -> {
                    prepareStatement.setString(index + 1, any as String)
                }
                else -> {
                    prepareStatement.setString(index + 1, any as String)
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
                    String::class-> {
                        column_value = result.getString(it)
                    }

                    Int::class -> {
                        column_value = result.getString(it)
                    }

                }
                var kproperties = record.javaClass.kotlin.declaredMemberProperties

                val property = kproperties.find { it.name == current_column }
                if (property is KMutableProperty<*>) {
                    property.setter.call(record, column_value)
                }

                var _lazy_list = kproperties.filter {
                    it.annotations.filter {
                        (it.annotationClass == has_many::class || it.annotationClass == belongs_to::class)
                    }.size > 0
                }

                _lazy_list.forEach {
                    it.isAccessible = true
                    var annotation = it.annotations.first()
                    when (it.annotations.map { it.annotationClass }.first()) {

                        has_many::class -> {
                            var join_table = (annotation as has_many).table
                            var join_column = (annotation as has_many).table
                            var target = join_table.simpleName?.toLowerCase()
                            var target_column = annotation.target
                            var base_column = annotation.base

                            if (it is KMutableProperty<*>) {
                                it.setter.call(record, ActiveList(emptyList(), "build lazy sql"))
                            }


                        }
                        belongs_to::class -> {
                            var join_table = (annotation as belongs_to).table
                            var join_column = (annotation as belongs_to).table
                            var target = join_table.simpleName?.toLowerCase()
                            var target_column = annotation.target
                            var base_column = annotation.base
                            if (it is KMutableProperty<*>) {
                                var set_obj = join_table.createInstance() as ActiveRecord<Any>

                                var joins = listOf<Join>(Join(join_sql = "INNER JOIN `${base_table}` ON  `${base_table}`.${target_column} = " +
                                        " `${target}s`.${base_column}"))

                                var wheres = listOf<Where>(Where())

                                set_obj.joins = joins

                                set_obj.is_list = false

                                it.setter.call(record, set_obj)
                            }
                        }
                    }

                }


            }
            /**
             * 赋予lazy join 语句
             */


            dataResult = dataResult.plusElement(record as T)
        }

        if(dataResult.size <= 0){
            return null
        } else{
            return dataResult[index];
        }




    }


    fun all() {

    }

}