package ActiveRecord4k

fun main(args: Array<String>) {


    var config = ActiveRecordConfig(jdbcUrl = "jdbc:mysql://localhost:3306/demo?useSSL=false", username = "root", password = "atyun123456")


    var teachers = User().where("name" to "2b")[0]!!.teachers


    var aa = teachers.all()

}



