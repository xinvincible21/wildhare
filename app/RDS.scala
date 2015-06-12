package utils

import com.zaxxer.hikari.{HikariDataSource, HikariConfig}

object RDS{

    val config = new HikariConfig("conf/hikaricp.properties")
    val ds = new HikariDataSource(config)
    implicit val conn = ds.getConnection

    val stmt = conn.createStatement()
    val rs = stmt.executeQuery("select advertisement_name from advertisement_metadata where advertisement_id = 1")
    rs.next()
    println(rs.getString("advertisement_name"))
}
