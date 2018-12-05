package com.vaadin.pwademo

import com.github.mvysny.karibudsl.v10.div
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import com.vaadin.pwademo.components.*

/**
 * The main layout. It uses the app-layout component which makes the app look like an Android Material app. See [AppHeaderLayout]
 * for more details.
 */
@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
@PWA(name = "Vaadin Kotlin PWA Demo", shortName = "Vaadin Kotlin PWA Demo", iconPath = "icons/icon-512.png", themeColor = "#227aef", backgroundColor = "#227aef")
class MainLayout : AppHeaderLayout(), RouterLayout {
    private val content: Div
    init {
        appHeader {
            appToolbar {
                title.text = "Vaadin Kotlin PWA Demo"
                paperIconButton(VaadinIcon.FILE_REMOVE) {
                    addClickListener {
                        Notification.show("A toast!", 3000, Notification.Position.BOTTOM_CENTER)
                    }
                }
            }
        }
        appDrawer {
            navMenuItem(VaadinIcon.LIST, "Task List")
            navMenuItem(VaadinIcon.COG, "Settings")
            navMenuItem(VaadinIcon.QUESTION, "About")
        }
        content = div {
            setSizeFull(); classNames.add("app-content")
        }
    }

    override fun showRouterLayoutContent(content: HasElement) {
        this.content.element.appendChild(content.element)
    }
}
