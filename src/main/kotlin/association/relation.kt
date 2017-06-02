package association

import kotlin.reflect.KClass

/**
 * Created by atyun on 6/2/17.
 */
class Relation {
    var relation_type: String = ""
    var base_class: String = ""
    var target_class: String = ""
    var base_column:String ="id"
    var target_column:String ="id"
}