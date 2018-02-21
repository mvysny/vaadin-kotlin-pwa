package com.vaadin.pwademo

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._click
import com.github.karibu.testing._get
import com.github.karibu.testing.autoDiscoverViews
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Label
import kotlin.test.expect

class MainViewTest: DynaTest({
    beforeGroup { Bootstrap().contextInitialized(null) }
    afterGroup { Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup(autoDiscoverViews("com.vaadin.pwademo")) }

    test("test greeting") {
        _get<Button> { caption = "Click me" } ._click()
        expect("Clicked!") { _get<Label>().text }
    }
})
