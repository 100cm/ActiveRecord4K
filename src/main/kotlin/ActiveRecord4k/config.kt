package ActiveRecord4k

/**
 * Created by atyun on 6/1/17.
 */
class Config private constructor() {

    private object Holder { val INSTANCE = Config() }

    companion object {
        val init: Config by lazy { Holder.INSTANCE }
    }

    var jdbcUrl = null
    var username = null
    var password = null
    var database = null



}