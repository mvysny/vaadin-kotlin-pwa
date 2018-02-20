package com.vaadin.flow.demo.helloworld

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._click
import com.github.karibu.testing._get
import com.github.karibu.testing.autoDiscoverViews
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.button.Button
import kotlin.test.expect

class MainViewTest: DynaTest({
    beforeEach { MockVaadin.setup(autoDiscoverViews("com.vaadin.flow.demo")) }

    test("test greeting") {
        _get<Button> { caption = "Click me" } ._click()
        expect("Clicked!") { _get<ExampleTemplate>().value }
    }
})
