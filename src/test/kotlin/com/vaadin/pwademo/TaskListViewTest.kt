package com.vaadin.pwademo

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import kotlin.test.expect

/**
 * Tests the main screen.
 */
class TaskListViewTest : DynaTest({
    usingApp()

    test("add a task") {
        UI.getCurrent().navigate("")
        _get<TextField> { label = "Title:" }.value = "New Task"
        _get<Button> { text = "Add" }._click()
        expectList("New Task") { Task.findAll().map { it.title } }
        expect(1) { _get<Grid<*>>().dataProvider._size() }
    }
})

// since there is no servlet environment, Flow won't auto-detect the @Routes. We need to auto-discover all @Routes
// and populate the RouteRegistry properly.
private val routes: Routes = Routes().autoDiscoverViews("com.vaadin.pwademo")

/**
 * Properly configures the app in the test context, so that the app is properly initialized, and the database is emptied before every test.
 */
@DynaTestDsl
fun DynaNodeGroup.usingApp() {
    beforeGroup { Bootstrap().contextInitialized(null) }
    afterGroup { Bootstrap().contextDestroyed(null) }

    beforeEach { MockVaadin.setup(routes) }
    afterEach { MockVaadin.tearDown() }

    // it's a good practice to clear up the db before every test, to start every test with a predefined state.
    fun cleanupDb() {
        Task.deleteAll()
    }
    beforeEach { cleanupDb() }
    afterEach { cleanupDb() }
}
