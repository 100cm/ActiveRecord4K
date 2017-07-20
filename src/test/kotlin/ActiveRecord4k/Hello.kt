package ActiveRecord4k

import org.junit.Test

class Hello {

    @Test
    fun main() {

        var config = ActiveRecordConfig(jdbcUrl = "jdbc:mysql://localhost:3306/demo?useSSL=false", username = "root", password = "atyun123456")

        var user = User().not("id" to 1)

        print(user[0]?.name)

        user = User()

        user.name = 123

        user.save()

    }
}



