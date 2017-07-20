package ActiveRecord4k

import java.sql.Connection
import java.sql.PreparedStatement


/**
 * Created by icepoint1999 on 27/05/2017.
 */

class SqlBuilder {


    fun selectSQl(record: ActiveRecord<Any>): String {


        var table_name = "`${record::class.java.simpleName.toLowerCase()}s`"

//        找到select
        var select_sql = "select #{select} from ${table_name} #{join}  #{where} #{limit} "


        if (record.selects.isEmpty()) {
            select_sql = select_sql.replace("#{select}", "${table_name}.*")
        }

        /**
         * 处理where
         */
        if (record.wheres.isEmpty()) {
            select_sql = select_sql.replace("#{where}", "")
        } else {
            var where_state = "where "
            record.wheres.forEachIndexed { index, where ->

                //判断是否原生sql
                if (where.sql.isBlank()) {
                    where_state += "${table_name}.${where.column_name} ${where.operator} ?"
                    record.bind_args = record.bind_args.plusElement(where.column_value)
                } else {
                    //直接转成sql
                    where_state += "(${where.sql})"
                    record.bind_args = record.bind_args.plus(where.bindArgs as List<Any>)
                }

                if (index != record.wheres.size - 1) {
                    where_state += "AND "
                }

            }
            select_sql = select_sql.replace("#{where}", where_state)

        }

        /**
         * 处理join
         */
        if (record.joins.isEmpty()){
            select_sql = select_sql.replace("#{join}", "")
        }else{
            var join_sql = ""
            record.joins.forEachIndexed {index,join ->
                join_sql += join.join_sql


            }

            select_sql = select_sql.replace("#{join}",join_sql)
        }

        /**
         * 处理limit
         */

        if(record.limit.isNullOrEmpty()){
            select_sql = select_sql.replace("#{limit}","")
        }else{
            select_sql = select_sql.replace("#{limit}","${record.limit}")
        }

        return select_sql

    }

}
