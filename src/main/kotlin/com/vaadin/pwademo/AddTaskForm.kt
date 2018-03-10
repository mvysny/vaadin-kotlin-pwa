package com.vaadin.pwademo

import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

/**
 * A reusable component demo; the component is completely built server-side.
 * @author mavi
 */
class AddTaskForm : HorizontalLayout() {
    /**
     * Invoked when a new task should be added.
     */
    var onAddTask: (Task)->Unit = {}

    private val binder = beanValidationBinder<Task>()
    init {
        content { align(left, baseline) }

        textField("Title:") {
            bind(binder)
                .trimmingConverter()
                .bind(Task::title)
        }
        button("Add") {
            setSizeUndefined()

            onLeftClick {
                val newTask = Task()
                if (binder.writeBeanIfValid(newTask)) {
                    onAddTask(newTask)
                    binder.readBean(Task())
                }
            }
        }

        binder.readBean(Task())
    }
}

/**
 * A DSL function which allows your component to be placed in the DSL component tree.
 */
fun (@VaadinDsl HasComponents).addTaskForm(block: (@VaadinDsl AddTaskForm).() -> Unit = {}) = init(AddTaskForm(), block)
