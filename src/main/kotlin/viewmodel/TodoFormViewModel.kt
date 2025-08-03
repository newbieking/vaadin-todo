package org.example.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.example.models.Todo
import org.example.models.User
import org.example.models.Users
import org.example.models.database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class TodoFormViewModel : CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Default) {

    var users: List<User> by observable(listOf()) { property, oldValue, newValue ->
        onUsersChange(property, oldValue, newValue)
    }

    var onUsersChange: (property: KProperty<*>, oldValue: List<User>, newValue: List<User>) -> Unit =
        { property, oldValue, newValue -> {} }



    fun fetchUsers(): Unit {
        launch {
            newSuspendedTransaction(Dispatchers.Default, database) {
                users = Users.selectAll().map { resultRow ->
                    User(
                        id = resultRow[Users.id].value,
                        name = resultRow[Users.name],
                    )
                }
            }
        }
    }
}