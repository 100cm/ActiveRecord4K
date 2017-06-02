package ActiveRecord4k

import anotation.belongs_to
import anotation.has_many
import entity.Column
import kotlin.reflect.KProperty

/**
 * Created by icepoint1999 on 27/05/2017.
 */
class User : ActiveRecord<User>() {

    var name by Column(String.javaClass);

    var id by Column(Int.javaClass);

    @has_many(Teacher::class)
    var teachers:List<Teacher> = listOf();

    @belongs_to(Teacher::class)
    var teacher:Teacher =Teacher();

}