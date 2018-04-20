package com.vaadin.pwademo

import com.github.karibu.testing.*
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.TextField
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.expect
import kotlin.test.fail

/**
 * Demonstrates a test suite for a single component.
 * @author mavi
 */
class AddTaskFormTest : DynaTest({
    beforeEach { MockVaadin.setup(Routes().autoDiscoverViews("com.vaadin.pwademo")) }

    test("the field is initially empty") {
        expect("") { AddTaskForm()._get<TextField>().value }
    }

    test("clicking 'add' does nothing if the title is empty") {
        val form = AddTaskForm()
        form.onAddTask = { fail("Unexpected") }
        form._get<Button>()._click()
        // check that the validation has been triggered
        expect(true) { form._get<TextField>().isInvalid }
    }

    test("filling in proper title fires onAddTask") {
        val form = AddTaskForm()
        val task = AtomicReference<Task>()
        form.onAddTask = { task.set(it) }
        form._get<TextField>().value = "My Task"
        form._get<Button>()._click()
        expect("My Task") { task.get()!!.title }
        expect(false) { task.get()!!.completed }
        expect(null) { task.get()!!.id }
        expect("") { form._get<TextField>().value }
    }
})