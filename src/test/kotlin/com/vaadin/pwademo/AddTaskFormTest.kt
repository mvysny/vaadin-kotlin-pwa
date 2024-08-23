package com.vaadin.pwademo

import com.github.mvysny.kaributesting.v10._click
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.pwademo.tasks.AddTaskForm
import com.vaadin.pwademo.tasks.Task
import org.junit.jupiter.api.Test
import kotlin.test.expect
import kotlin.test.fail

/**
 * Demonstrates a test suite for a single component.
 * @author mavi
 */
class AddTaskFormTest : AbstractAppTest() {
    @Test fun `the field is initially empty`() {
        expect("") { AddTaskForm()._get<TextField>().value }
    }

    @Test fun `clicking 'add' does nothing if the title is empty`() {
        val form = AddTaskForm()
        form.onAddTask = { fail("Unexpected") }
        form._get<Button>()._click()
        // check that the validation has been triggered
        expect(true) { form._get<TextField>().isInvalid }
    }

    @Test fun `filling in proper title fires onAddTask`() {
        val form = AddTaskForm()
        lateinit var task: Task
        form.onAddTask = { task = it }
        form._get<TextField>().value = "My Task"
        form._get<Button>()._click()
        expect("My Task") { task.title }
        expect(false) { task.completed }
        expect(null) { task.id }
        expect("") { form._get<TextField>().value }
    }
}
