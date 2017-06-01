package ActiveRecord4k

/**
 * Created by icepoint1999 on 27/05/2017.
 */
data class Join(
        var join_table: Any? = "",
        var join_target: Any? = "",
        var join_table_column: Any? = "",
        var join_target_column: Any? = "",
        var join_type: Any? = "INNER JOIN") {


}