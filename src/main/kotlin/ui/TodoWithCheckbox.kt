package org.example.ui

import com.github.mvysny.karibudsl.v10.*
import org.example.models.Todo

class TodoWithCheckbox(
    todo: Todo,
    onCheckChange: (Boolean) -> Unit,
    onClickLink: () -> Unit,
) : KComposite() {
    private val root = ui {
        verticalLayout {
            horizontalLayout {
                checkBox {
                    value = todo.completedAt != null
                    addValueChangeListener { onCheckChange(it.value) }
                }
                span(todo.title) {
                    addClassName("link-text")
                    if (todo.completedAt != null) {
                        addClassName("strikethrough")
                    }
                    addClickListener { onClickLink() }
                }
            }
            todo.notes?.let {
                span(it)
            }
        }
    }

}