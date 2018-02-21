package com.vaadin.pwademo

import com.github.vok.framework.sql2o.vaadin.dataProvider
import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Route("")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
class MainView : VerticalLayout() {
    private val form: AddTaskForm
    private lateinit var label: Label
    private lateinit var grid: Grid<Task>
    init {
        setSizeFull()

        form = addTaskForm {
            onAddTask = {
                it.save()
                grid.dataProvider.refreshAll()
            }
        }

        grid = grid {
            width = "100%"; isExpand = true
            // don't set the height to 100% - this will behave differently than you would expect. flexGrow sets the height automatically so that
            // the Grid fills its parent height-wise.
            // See https://github.com/vaadin/flow/issues/3582 for more details.

            dataProvider = Task.dataProvider.sortedBy(Task::completed.asc)
            addColumn(ComponentRenderer<Checkbox, Task> { task -> Checkbox(task.completed).apply {
                // when the check box is changed, update the task and reload the grid
                addValueChangeListener {
                    task.completed = it.value
                    task.save()
                    grid.dataProvider.refreshAll()
                }
            } }).apply {
                flexGrow = 0
                setHeader("Done")
                sortProperty = Task::completed
            }
            addColumnFor(Task::title) {
                setHeader("Title")
            }
        }
    }
}
