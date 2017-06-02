package ActiveRecord4k


fun main(args: Array<String>) {

    val a = User().where("name = ? ","测试")
           .joins("teachers")

    val b = User().where("name like ?","测试").where("id" to "1")



    println(a[0].teachers)

    println(b[0].name)



}

