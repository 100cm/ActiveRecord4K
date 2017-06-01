package ActiveRecord4k


fun main(args: Array<String>) {

    val a = User().where("name = ? ","hello")
           .join("teachers")


    print(a[1].id)


}

