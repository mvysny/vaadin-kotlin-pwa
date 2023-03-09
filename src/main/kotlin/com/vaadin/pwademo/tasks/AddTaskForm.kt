package com.vaadin.pwademo.tasks

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents

/**
 * A reusable component demo; the component is completely built using server-side code; no Polymer templates nor JavaScript
 * are used to build the component.
 * @author mavi
 */
class AddTaskForm : KComposite() {
    /**
     * Invoked when a new task should be added.
     */
    var onAddTask: (Task)->Unit = {}

    private val binder = beanValidationBinder<Task>()
    private val root = ui {
        horizontalLayout {
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

        }
    }

    init {
        binder.readBean(Task())
    }
}

/**
 * A DSL function which allows your component to be placed in the DSL component tree.
 */
fun (@VaadinDsl HasComponents).addTaskForm(block: (@VaadinDsl AddTaskForm).() -> Unit = {}) = init(
    AddTaskForm(), block)
