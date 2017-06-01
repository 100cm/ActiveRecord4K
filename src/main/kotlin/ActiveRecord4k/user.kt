package ActiveRecord4k

import entity.Column
import kotlin.reflect.KProperty

/**
 * Created by icepoint1999 on 27/05/2017.
 */
class User : ActiveRecord<User>() {

    var name by Column(String.javaClass);

    var id by Column(Int.javaClass);


}