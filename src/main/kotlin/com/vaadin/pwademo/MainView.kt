package com.vaadin.pwademo

import com.github.vok.framework.sql2o.vaadin.dataProvider
import com.github.vok.framework.sql2o.vaadin.generateFilterComponents
import com.github.vok.framework.sql2o.vaadin.sortedBy
import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcons
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

/**
 * The main view of the app. It is itself a vertical layout which lays out the child components vertically. There are only two components:
 *
 * * a custom component named [AddTaskForm] which is a form used to add new items to the task list;
 * * a Vaadin Grid which lists all current tasks
 *
 * The UI is defined by the means of so-called DSL; see [Karibu-DSL examples](https://github.com/mvysny/karibu-dsl#how-to-write-dsls-for-vaadin-8-and-vaadin8-v7-compat)
 * for more examples.
 */
@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Route("")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
class MainView : VerticalLayout() {
    private val form: AddTaskForm
    private lateinit var grid: Grid<Task>
    init {
        setSizeFull()

        form = addTaskForm {
            onAddTask = { task ->
                task.save()
                grid.refresh()
            }
        }

        grid = grid {
            width = "100%"; isExpand = true
            // don't set the height to 100% - this will behave differently than you would expect. flexGrow sets the height automatically so that
            // the Grid fills its parent height-wise.
            // See https://github.com/vaadin/flow/issues/3582 for more details.

            dataProvider = Task.dataProvider.sortedBy(Task::completed.asc, Task::created.desc)

            addColumn(createTaskCompletedCheckboxRenderer()).apply {
                flexGrow = 0
                setHeader("Done")
                sortProperty = Task::completed
            }
            addColumn(createTaskNameDivRenderer()).apply {
                key = "title"
                sortProperty = Task::title
                setHeader("Title")
            }
            addColumn(newDeleteButtonRenderer()).apply {
                flexGrow = 0
            }

            generateFilterComponents(Task::class)
        }
    }

    private fun createTaskCompletedCheckboxRenderer(): ComponentRenderer<Checkbox, Task> = ComponentRenderer { task ->
        Checkbox(task.completed).apply {
            // when the check box is changed, update the task and reload the grid
            addValueChangeListener {
                task.completed = it.value
                task.save()
                grid.dataProvider.refreshAll()
            }
        }
    }

    private fun createTaskNameDivRenderer(): ComponentRenderer<Div, Task> = ComponentRenderer { task ->
        Div().apply {
            text = task.title
            if (task.completed) {
                classNames.add("crossedout")
            }
        }
    }

    private fun newDeleteButtonRenderer(): ComponentRenderer<Button, Task> = ComponentRenderer { task ->
        Button(Icon(VaadinIcons.TRASH)).apply {
            themes.add("tertiary-inline")  // this will remove the button border
            onLeftClick {
                task.delete()
                grid.dataProvider.refreshAll()
            }
        }
    }
}
