package ActiveRecord4k

import java.sql.Connection
import java.sql.PreparedStatement



/**
 * Created by icepoint1999 on 27/05/2017.
 */

class SqlBuilder {


    fun selectSQl(record: ActiveRecord<Any>):String {


        var table_name = "${record::class.java.simpleName.toLowerCase()}s"

//        找到select
        var select_sql = "select #{select} from ${table_name} #{where} "


        if (record.selects.isEmpty()) {
            select_sql  = select_sql.replace("#{select}","*")
        }

        /**
         * 处理where
         */
        if(record.wheres.isEmpty()){
            select_sql  = select_sql.replace("#{where}","")
        }else{
            var where_state = "where "
            record.wheres.forEachIndexed { index, where ->
                if(where.sql.isBlank()){
                    where_state += "${table_name}.${where.column_name} = ${where.column_value} "
                }else{
                    where_state += where.sql
                    record.bind_args = record.bind_args.plus(where.bindArgs as List<Any>)
                }

                if(index != record.wheres.size-1){
                    where_state+= "AND"
                }
            }
            select_sql  = select_sql.replace("#{where}",where_state)

        }

        print(select_sql)

        return select_sql

    }

}
