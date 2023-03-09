package com.vaadin.pwademo

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.router.Route
import com.vaadin.pwademo.tasks.Task

/**
 * A simple "About" view.
 */
@Route("about", layout = MainLayout::class)
class AboutView : KComposite() {
    private val root = ui {
        verticalLayout {
            h3("About")
            span("This project demoes a PWA app made in Vaadin, using the Vaadin-on-Kotlin framework")
            span("Vaadin ${VaadinVersion.get} Flow ${VaadinVersion.flow}, Kotlin ${KotlinVersion.CURRENT}, JVM $jvmVersion")
            button("Re-generate data") {
                onLeftClick { Task.regenerateSampleData() }
            }
        }
    }
}

val jvmVersion: String get() = System.getProperty("java.version")
