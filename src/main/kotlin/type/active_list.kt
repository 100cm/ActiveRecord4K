package type

import ActiveRecord4k.ActiveRecord
import anotation.lazy_list
import java.util.function.Consumer


/**
 * Created by icepoint1999 on 04/06/2017.
 */

class ActiveList<out T : ActiveRecord<*>>(private val list: List<T> = emptyList(), private val lazy_obj: ActiveRecord<*>? = null) : List<T> by list {


    override operator fun get(int: Int): T {
        /**
         * build lazy sql and excute
         */
        return lazy_obj!![int] as T
    }

    @lazy_list
    fun all():List<T>{
        return lazy_obj?.all() as List<T>
    }




}