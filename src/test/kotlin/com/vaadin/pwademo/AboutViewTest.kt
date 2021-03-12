package com.vaadin.pwademo

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI

/**
 * Tests the main screen.
 */
class AboutViewTest : DynaTest({
    usingApp()

    test("smoke") {
        UI.getCurrent().navigate(AboutView::class.java)
        _expectOne<AboutView>()
    }
})
