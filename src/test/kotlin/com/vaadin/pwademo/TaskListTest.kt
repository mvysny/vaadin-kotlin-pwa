package com.vaadin.pwademo

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.github.vokorm.deleteAll
import com.github.vokorm.findAll
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import kotlin.test.expect

/**
 * Tests the main screen.
 */
class TaskListTest: DynaTest({
    beforeGroup { Bootstrap().contextInitialized(null) }
    afterGroup { Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup(Routes().autoDiscoverViews("com.vaadin.pwademo")) }
    afterEach { MockVaadin.tearDown() }
    beforeEach { Task.deleteAll() }
    afterEach { Task.deleteAll() }

    test("add a task") {
        UI.getCurrent().navigate("")
        _get<TextField> { caption = "Title:" } .value = "New Task"
        _get<Button> { caption = "Add" } ._click()
        expectList("New Task") { Task.findAll().map { it.title } }
        expect(1) { _get<Grid<*>>().dataProvider._size() }
    }
})
