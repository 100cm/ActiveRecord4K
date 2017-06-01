package ActiveRecord4k

import java.util.jar.Pack200.Packer.PASS
import com.sun.deploy.security.CertStore.USER
import org.sql2o.Sql2o



/**
 * Created by icepoint1999 on 27/05/2017.
 */
open class ActiveRecord<T> {

    var joins: List<Join> = listOf()
    var wheres: List<Where> = listOf();

    var selects: List<String> = listOf();

    var conn:Sql2o? = null;


    init {
        this.conn = Sql2o("jdbc:mysql://localhost:3306/score_treasure_dev", "root", "root")
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

}