package com.vaadin.pwademo

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.span
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.router.Route

/**
 * @author Martin Vysny <mavi@vaadin.com>
 */
@Route("about", layout = MainLayout::class)
class AboutView : KComposite() {
    private val root = ui {
        verticalLayout {
            h3("About")
            span("This project demoes a PWA app made in Vaadin, using the Vaadin-on-Kotlin framework")
        }
    }
}