package com.vaadin.pwademo

import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

/**
 * A reusable component demo; the component is completely built server-side.
 * @author mavi
 */
class AddTaskForm : Div() {
    /**
     * Invoked when the task should be added.
     */
    var onAddTask: (Task)->Unit = {}
    private val binder = beanValidationBinder<Task>()
    init {
        textField("Title:") {
            bind(binder)
                .trimmingConverter()
                .bindN(Task::title)
        }
        val btn = button("Add") {
            setSizeUndefined()

            onLeftClick {
                val newTask = Task()
                if (binder.writeBeanIfValid(newTask)) {
                    onAddTask(newTask)
                    binder.readBean(Task())
                } else {
                    binder.validate()
                }
            }
        }

        binder.readBean(Task())
    }
}

fun (@VaadinDsl HasComponents).addTaskForm(block: (@VaadinDsl AddTaskForm).() -> Unit = {}) = init(AddTaskForm(), block)
