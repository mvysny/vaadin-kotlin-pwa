package com.vaadin.pwademo.utils

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.PageTitle
import kotlin.reflect.KClass

/**
 * Taken from the [App Layout](https://vaadin.com/docs/latest/components/app-layout) component documentation.
 */
class NavMenuBar : Tabs(), AfterNavigationObserver {
    /**
     * Maps route class to its tab.
     */
    private val tabs: MutableMap<Class<*>, Tab> = mutableMapOf()

    init {
        orientation = Orientation.VERTICAL
    }

    override fun afterNavigation(event: AfterNavigationEvent) {
        val currentRoute: Class<out HasElement> = event.activeChain[0].javaClass
        selectedTab = tabs[currentRoute]
    }

    fun addRoute(icon: VaadinIcon, routeClass: KClass<out Component>, title: String = getRouteTitle(routeClass)) {
        val tab = tab {
            routerLink(viewType = routeClass) {
                navIcon(icon)
                span(title)
            }
        }
        tabs[routeClass.java] = tab
    }

    fun addButton(icon: VaadinIcon, title: String, clickListener: () -> Unit) {
        tab {
            div {
                navIcon(icon)
                span(title)
                onLeftClick { clickListener() }
            }
        }
    }
}

private fun getRouteTitle(routeClass: KClass<*>): String {
    val title = routeClass.java.getAnnotation(PageTitle::class.java)
    return title?.value ?: routeClass.simpleName ?: ""
}

@VaadinDsl
fun (@VaadinDsl HasComponents).navMenuBar(block: (@VaadinDsl NavMenuBar).() -> Unit = {}) = init(
    NavMenuBar(), block)

@VaadinDsl
private fun (@VaadinDsl HasComponents).navIcon(icon: VaadinIcon) {
    icon(icon) {
        style["box-sizing"] = "border-box"
        style["margin-inline-end"] = "var(--lumo-space-m)"
        style["margin-inline-start"] = "var(--lumo-space-xs)"
        style["padding"] = "var(--lumo-space-xs)"
    }
}
