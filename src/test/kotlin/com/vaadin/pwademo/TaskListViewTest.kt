package com.vaadin.pwademo

import com.github.mvysny.kaributesting.v10._click
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v10._size
import com.github.mvysny.kaributesting.v10.expectList
import com.github.mvysny.ktormvaadin.findAll
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.pwademo.tasks.Tasks
import org.junit.jupiter.api.Test
import kotlin.test.expect

/**
 * Tests the main screen.
 */
class TaskListViewTest : AbstractAppTest() {
    @Test fun addTask() {
        UI.getCurrent().navigate("")
        _get<TextField> { label = "Title:" }.value = "New Task"
        _get<Button> { text = "Add" }._click()
        expectList("New Task") { Tasks.findAll().map { it.title } }
        expect(1) { _get<Grid<*>>().dataProvider._size() }
    }
}
