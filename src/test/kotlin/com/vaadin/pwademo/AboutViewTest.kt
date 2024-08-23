package com.vaadin.pwademo

import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.Test

/**
 * Tests the main screen.
 */
class AboutViewTest : AbstractAppTest() {
    @Test fun smoke() {
        UI.getCurrent().navigate(AboutView::class.java)
        _expectOne<AboutView>()
    }
}
