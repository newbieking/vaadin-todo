package org.example.ui

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.sun.jna.platform.win32.Netapi32Util.getUsers
import com.vaadin.flow.component.ItemLabelGenerator
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import org.example.models.Todo
import org.example.models.User
import org.example.viewmodel.TodoFormViewModel

class TodoForm(
    private val todo: Todo? = null,
    onClickDelete: ((Todo) -> Unit)? = null,
    onClickSave: (Todo) -> Unit
) : KComposite(){
    private lateinit var dialog: Dialog
    private lateinit var titleField: TextField
    private lateinit var dueDateField: DatePicker
    private lateinit var notesField: TextArea
    private lateinit var userField: Select<User>

    private var viewModel = TodoFormViewModel()

    private val currentUi = UI.getCurrent()

    init {
        configureObservable()
        fetchUsers()
    }

    private fun configureObservable() {
        viewModel.onUsersChange = {property, oldValue, newValue ->
            currentUi.accessSynchronously {
                userField.setItems(newValue)
                todo?.user?.let { user ->
                    userField.value = user
                }
            }
        }
    }

    fun fetchUsers(): Unit {
        viewModel.fetchUsers()
    }

    private fun formIsValid(): Boolean {
        return titleField.value.isNotBlank()
                && dueDateField.value != null
                && notesField.value.isNotBlank()
                && userField.value != null
    }

    private val root = ui {
        verticalLayout {
            dialog = openDialog {
                verticalLayout {
                    formLayout {
                        titleField = textField("Title") {
                            isRequired = true
                            todo?.title?.let { value = it }
                        }
                        dueDateField = datePicker("Due Date") {
                            todo?.dueAt?.let { value = it }
                        }
                        notesField = textArea("Notes") {
                            maxRows=2
                            todo?.notes?.let { value = it }
                        }
                        userField = select("Assignee") {
                            itemLabelGenerator = ItemLabelGenerator {it.name}
                        }
                    }
                    horizontalLayout {
                        if (todo != null) {
                            button("Delete") {
                                onClick {
                                    onClickDelete?.invoke(todo)
                                    dialog.close()
                                }
                            }
                        }
                        button("Cancel") {
                            onClick {
                                dialog.close()
                            }
                        }
                        button("Save") {
                            setPrimary()
                                onClick {
                                    if (!formIsValid()) return@onClick

                                    val newTodo = genTodoModel()
                                    onClickSave(newTodo)
                                    dialog.close()
                                }
                        }
                    }
                }
            }
        }
    }

    private fun genTodoModel(): Todo {
        return Todo(
            title = titleField.value,
            dueAt = dueDateField.value,
            notes = notesField.value,
            user = userField.value
        )
    }
}