package ActiveRecord4k

/**
 * Created by atyun on 6/8/17.
 */
class ActiveFind(var find_active:Map<String,(value:String)->Any>){

    operator fun get(name:String){
        var act = ActiveFindInvoker()
        act.function = this.find_active[name]
    }

}