package type

import ActiveRecord4k.ActiveRecord
import ActiveRecord4k.Teacher
import ActiveRecord4k.User

/**
 * Created by icepoint1999 on 04/06/2017.
 */
class ActiveList<out T:ActiveRecord<*>>(private val list: List<T> = emptyList(),private val lazy_sql:String ="") : List<T> by list {


    override operator fun get(int: Int): T {

        println(this.lazy_sql)


        /**
         * build lazy sql and exexcute
         */
        var t = User()


        t.name = "123"

        return t as T ;
    }


}