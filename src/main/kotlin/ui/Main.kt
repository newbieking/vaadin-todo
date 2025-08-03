package org.example.ui

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLayout
import org.example.models.Todo
import org.example.viewmodel.TodoViewModel
import kotlin.reflect.KProperty

class MainLayout : KComposite(), RouterLayout {
    private val root = ui {
        verticalLayout {
            div { h1("Todos") }
        }
    }

    override fun showRouterLayoutContent(content: HasElement?) {
        root.add(content as Component)
    }

}

@Route("", layout = MainLayout::class)
class TodoList : KComposite() {
    private val currentUi = UI.getCurrent()
    private val viewModel = TodoViewModel()
    private lateinit var grid: Grid<Todo>

    private val root = ui {
        verticalLayout {
            button("Add todo", icon = VaadinIcon.PLUS.create()) {
                onClick {
                    TodoForm {
                        todo -> viewModel.insert(todo)
                    }
                }
            }
            textField {
                setWidthFull()
                isClearButtonVisible = true
                placeholder = "Search for todo title..."
                valueChangeMode = ValueChangeMode.LAZY
                addValueChangeListener { event ->
                    viewModel.fetchAll(event.value)
                }
            }
            grid = grid {
                componentColumn({todo -> TodoWithCheckbox(
                    todo,
                    onCheckChange = {isChecked ->
                        viewModel.updateCompletion(todo, isChecked)
                    },
                    onClickLink = {}
                )}).setHeader("Todo")
                column({it.dueAt?.toString()}).setHeader("Due Date")
                column({it.user?.name}).setHeader("Assignee")
            }
        }
    }

    init {
        configObservers()
        viewModel.fetchAll()
    }

    private fun configObservers() {

        viewModel.onTodosChange = { property: KProperty<*>, oldValue: List<Todo>, newValue: List<Todo> ->
            currentUi.accessSynchronously {
                grid.setItems(newValue)
            }
        }
    }
}
