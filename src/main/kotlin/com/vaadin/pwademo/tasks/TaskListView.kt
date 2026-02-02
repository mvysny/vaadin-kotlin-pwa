package com.vaadin.pwademo.tasks

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.asc
import com.github.mvysny.kaributools.refresh
import com.github.mvysny.kaributools.sort
import com.github.mvysny.ktormvaadin.and
import com.github.mvysny.ktormvaadin.dataProvider
import com.github.mvysny.ktormvaadin.e
import com.github.mvysny.ktormvaadin.filter.BooleanFilterField
import com.github.mvysny.ktormvaadin.filter.FilterTextField
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.pwademo.MainLayout
import org.ktorm.dsl.eq
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.support.postgresql.ilike

/**
 * The main view of the app. It is a vertical layout which lays out the child components vertically. There are only two components:
 *
 * * a custom component named [AddTaskForm] which is a form used to add new items to the task list;
 * * a Vaadin Grid which lists all current tasks
 *
 * The UI is defined by the means of so-called DSL; see [Karibu-DSL examples](https://github.com/mvysny/karibu-dsl#how-to-write-dsls-for-vaadin-8-and-vaadin8-v7-compat)
 * for more examples.
 *
 * Note that the `@Route` annotation defines the main layout with which this view is wrapped in. See [MainLayout] for details on how to
 * create an app-wide layout which hosts views.
 */
@Route("", layout = MainLayout::class)
@PageTitle("Task List")
class TaskListView : KComposite() {
    private val dataProvider = Tasks.dataProvider
    private val completedFilter = BooleanFilterField()
    private val taskTitleFilter = FilterTextField("task_title_filter")

    private lateinit var form: AddTaskForm
    private lateinit var grid: Grid<Task>
    private val root = ui {
        verticalLayout {
            setSizeFull(); isPadding = false

            verticalLayout { // add a bit of padding
                form = addTaskForm {
                    onAddTask = { task ->
                        task.save()
                        grid.refresh()
                    }
                }
            }

            grid = grid<Task>(dataProvider) {
                width = "100%"; isExpand = true

                appendHeaderRow() // workaround for https://github.com/vaadin/vaadin-grid-flow/issues/912
                val filterBar = appendHeaderRow()

                val completedColumn = columnFor(Task::completed, createTaskCompletedCheckboxRenderer()) {
                    isExpand = false; setHeader("Done"); width = "130px"
                    setSortProperty(Tasks.completed.e.key)
                    completedFilter.width = "100%"
                    completedFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = completedFilter
                }
                columnFor(Task::title, createTaskTitleDivRenderer()) {
                    setSortProperty(Tasks.title.e.key)
                    taskTitleFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = taskTitleFilter
                }
                addColumn(newDeleteButtonRenderer()).apply {
                    isExpand = false; width = "90px"
                }

                sort(completedColumn.asc)
            }
            dataProvider.setSortOrders(listOf(Tasks.completed.e.asc, Tasks.created.e.asc))
        }
    }

    private fun updateFilter() {
        val c = mutableListOf<ColumnDeclaring<Boolean>?>()
        if (completedFilter.value != null) {
            c += Tasks.completed eq completedFilter.value!!
        }
        if (taskTitleFilter.value.isNotBlank()) {
            c += Tasks.title ilike taskTitleFilter.value + "%"
        }
        dataProvider.setFilter(c.and())
    }

    private fun createTaskCompletedCheckboxRenderer(): ComponentRenderer<Checkbox, Task> =
        ComponentRenderer { task ->
            Checkbox(task.completed).apply {
                // when the check box is changed, update the task and reload the grid
                addValueChangeListener {
                    task.completed = it.value
                    task.save()
                    grid.dataProvider.refreshAll()
                }
            }
        }

    private fun createTaskTitleDivRenderer(): ComponentRenderer<Div, Task> =
        ComponentRenderer { task ->
            Div().apply {
                text = task.title
                if (task.completed) {
                    classNames.add("crossedout")
                }
            }
        }

    private fun newDeleteButtonRenderer(): ComponentRenderer<Button, Task> =
        ComponentRenderer { task ->
            Button(Icon(VaadinIcon.TRASH)).apply {
                addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE)  // this will remove the button border
                onClick {
                    task.delete()
                    grid.dataProvider.refreshAll()
                }
            }
        }
}