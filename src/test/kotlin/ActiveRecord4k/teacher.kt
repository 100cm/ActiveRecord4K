package ActiveRecord4k

import anotation.has_many
import entity.Column
import type.ActiveList

/**
 * Created by icepoint1999 on 27/05/2017.
 */
class Teacher : ActiveRecord<Teacher>() {

    var name by Column()

    var id by Column(Int::class)

    @has_many(User::class,base="id",target="teacher_id")
    var users:List<User> = ActiveList()

}