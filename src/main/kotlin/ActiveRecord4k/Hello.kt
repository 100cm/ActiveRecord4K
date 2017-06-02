package ActiveRecord4k

import ActiveRecord4k.ActiveRecord

fun main(args: Array<String>) {

    val a = User().where("name = ? ","测试")
           .joins("teachers")

    val b = User().where("name like ?","测试").where("id" to "1")



    b[0].teachers[0] getElement 12

    println(b[0].teachers)



}

