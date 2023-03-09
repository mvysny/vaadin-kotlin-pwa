package com.vaadin.pwademo

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.router.RouterLayout
import com.vaadin.pwademo.tasks.TaskListView

/**
 * The main layout. It uses the app-layout component which makes the app look like an Android Material app. See [AppHeaderLayout]
 * for more details.
 */
class MainLayout : KComposite(), RouterLayout {
    private lateinit var contentPane: Div
    private val root = ui {
        appLayout {
            isDrawerOpened = false

            navbar {
                drawerToggle()
                h3("Vaadin Kotlin PWA Demo")
                button(icon = VaadinIcon.FILE_REMOVE.create()) {
                    addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON)
                    onLeftClick {
                        Notification.show("A toast!", 3000, Notification.Position.BOTTOM_CENTER)
                    }
                }
            }
            drawer {
                verticalLayout {
                    routerLink(VaadinIcon.LIST, "Task List", TaskListView::class)
                    routerLink(VaadinIcon.QUESTION, "About", AboutView::class)
                }
            }

            content {
                contentPane = div {
                    setSizeFull(); classNames.add("app-content")
                }
            }
        }
    }

    override fun showRouterLayoutContent(content: HasElement) {
        contentPane.element.appendChild(content.element)
    }
}

