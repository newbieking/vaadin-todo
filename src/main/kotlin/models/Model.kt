package org.example.models

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate


val dataSource = HikariDataSource(HikariConfig().apply {
    driverClassName = "org.postgresql.Driver"
    jdbcUrl = "jdbc:postgresql://localhost:5432/"
    username = "postgres"
    password = "postgres"
})

val database = Database.connect(dataSource)

object DatabaseFactory {
    fun init() {

        // 在事务中同步表结构
        transaction(database) {
            // 创建所有表，如果表已存在则忽略
            println("Creating database...")
            SchemaUtils.createMissingTablesAndColumns(Todos, Users)
            // 如果你想删除所有表并重新创建（仅用于开发环境）
            // SchemaUtils.drop(Users, Articles)
            // SchemaUtils.create(Users, Articles)
        }
    }
}

object Todos : IntIdTable("todos") {
    val title = varchar("title", 255).nullable()
    val description = varchar("description", 255).nullable()
    val dueAt = date("due_at").nullable()
    val completedAt = date("completed_at").nullable()
    val userId = reference("user_id", Users).nullable()
}

object Users : IntIdTable("users") {
    val name = varchar("name", 255).nullable()
}


data class Todo (
    val id: Int? = null,
    val title: String? = null,
    val notes: String? = null,
    val dueAt: LocalDate? = null,
    val completedAt: LocalDate? = null,
    val user: User? = null
)

data class User (
    val id: Int? = null,
    val name: String? = null,
)