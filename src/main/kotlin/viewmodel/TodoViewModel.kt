package org.example.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.example.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class TodoViewModel : CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Default) {
    private lateinit var searchTerm: String
    var onTodosChange: (property: KProperty<*>, oldValue: List<Todo>, newValue: List<Todo>) -> Unit =
        { property, oldValue, newValue -> {} }

    var todos by observable(listOf<Todo>()) {
        property, oldValue, newValue ->
        onTodosChange(property, oldValue, newValue)
    }

    fun fetchAll() {
        launch {
            todos = newSuspendedTransaction(Dispatchers.IO, database) {
                val query = Todos.join(Users, JoinType.LEFT, Todos.userId, Users.id)
                    .selectAll()
                    .orderBy(Todos.completedAt, SortOrder.DESC)
                    .orderBy(Todos.dueAt, SortOrder.ASC)
                    .orderBy(Todos.title, SortOrder.ASC)
                query.map { resultRow ->
                    Todo(
                        id = resultRow[Todos.id].value,
                        title = resultRow[Todos.title],
                        dueAt = resultRow[Todos.dueAt],
                        completedAt = resultRow[Todos.completedAt],
                        notes = resultRow[Todos.description],
                        user = User(
                            id = resultRow[Users.id].value,
                            name = resultRow[Users.name],
                        )
                    )
                }

            }
        }
    }

    fun insert() {
        launch {
            newSuspendedTransaction(Dispatchers.IO, database) {
                Users.insert {
                    User(
                        it[id].value,
                        it[name],
                    )
                }
            }
        }
    }

    fun insert(todo: Todo) {
        launch {
            newSuspendedTransaction(Dispatchers.IO, database) {
                Todos.insert {
                    it[title] = todo.title
                    it[dueAt] = todo.dueAt
                    it[completedAt] = todo.completedAt
                    it[description] = todo.notes
                    it[userId] = todo.user?.id
                }
            }
            fetchAll()
        }
    }

    fun fetchAll(searchTerm: String? = null) {
        searchTerm?.let {
            this@TodoViewModel.searchTerm = it
        }
        launch {
            todos = newSuspendedTransaction(Dispatchers.IO, database) {
                val query = Todos.join(Users, JoinType.LEFT, Todos.userId, Users.id)
                    .selectAll()
                    .orderBy(Todos.completedAt, SortOrder.DESC)
                    .orderBy(Todos.dueAt, SortOrder.ASC)
                    .orderBy(Todos.title, SortOrder.ASC)
                this@TodoViewModel.searchTerm?.let {
                    query.where {
                        Todos.title.upperCase() like "%${it.uppercase()}%"
                    }
                }
                query.map { resultRow ->
                    Todo(
                        id = resultRow[Todos.id].value,
                        title = resultRow[Todos.title],
                        dueAt = resultRow[Todos.dueAt],
                        completedAt = resultRow[Todos.completedAt],
                        notes = resultRow[Todos.description],
                        user = User(
                            id = resultRow[Users.id].value,
                            name = resultRow[Users.name],
                        )
                    )
                }
            }
        }
    }

    fun updateCompletion(todo: Todo, isCompleted: Boolean) {
        launch {
            newSuspendedTransaction(Dispatchers.IO, database) {
                Todos.update({ Todos.id eq todo.id }) {
                    it[Todos.completedAt] = if (isCompleted) LocalDate.now() else null
                }
            }
            fetchAll()
        }
    }
}
