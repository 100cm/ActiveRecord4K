package ActiveRecord4k

import anotation.belongs_to
import anotation.has_many
import association.LazyLoad
import entity.Column
import type.ActiveList
import kotlin.reflect.KProperty

/**
 * Created by icepoint1999 on 27/05/2017.
 */
class User : ActiveRecord<User>() {

    var name by Column(String::class);

    var id by Column(Int::class);

    @has_many(Teacher::class)
    var teachers :List<Teacher> = ActiveList();

    @belongs_to(Teacher::class)
    var teacher :Teacher = Teacher()

}