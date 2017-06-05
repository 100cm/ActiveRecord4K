package ActiveRecord4k

import ActiveRecord4k.ActiveRecord


fun main(args: Array<String>) {

    val a = User().where("name = ? ", "测试").joins("teachers")

    val b = User().where("name like ?", "测试").where("id" to "1")


    val aa = b[0]?.teacher?.where("name" to "teacher", "id" to 1)!![0]?.users!![0]?.name

    print(aa)


}

