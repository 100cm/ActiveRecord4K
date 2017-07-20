package ActiveRecord4k

import java.lang.reflect.Parameter

/**
 * Created by atyun on 6/8/17.
 */
class ActiveFindInvoker {

    var function: ((value: String) -> Any)? = {}

    constructor(){

    }

    constructor(params: String) {
        function?.invoke(params)
    }

}