package ActiveRecord4k

/**
 * Created by icepoint1999 on 27/05/2017.
 */
data class  Where(var column_name:String = "",var column_value:Any? = null) {

    var sql:String = "";

    var bindArgs:List<Any?> = listOf();





}