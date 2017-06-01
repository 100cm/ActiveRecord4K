package ActiveRecord4k

import java.sql.Connection
import java.sql.PreparedStatement



/**
 * Created by icepoint1999 on 27/05/2017.
 */

class SqlBuilder {


    fun selectSQl(record: ActiveRecord<Any>):String {

//        找到select
        var select_sql = "select * from ${record::class.java.simpleName.toLowerCase()}s "

        print(select_sql)

        if (record.selects.isEmpty()) {


        }
        return select_sql

    }

}
