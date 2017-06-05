package ActiveRecord4k

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

/**
 * Created by atyun on 6/1/17.
 */
object ActiveRecordConfig  {


    var database:HikariDataSource = HikariDataSource()

    operator fun invoke(jdbcUrl:String,username:String,password:String): ActiveRecordConfig  {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/demo?useSSL=false"
        config.username = "root"
        config.password = "atyun123456"
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "2500")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        val ds = HikariDataSource(config)
        this.database = ds
        return this
    }



}