package com.vaadin.pwademo.utils

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.function.SerializableRunnable
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.RouterLink

/**
 * Taken from the [App Layout](https://vaadin.com/docs/latest/components/app-layout) component documentation.
 */
class NavMenuBar : Tabs(), AfterNavigationObserver {
    /**
     * Maps route class to its tab.
     */
    private val tabs: MutableMap<Class<*>, Tab> = HashMap()

    init {
        orientation = Orientation.VERTICAL
    }

    override fun afterNavigation(event: AfterNavigationEvent) {
        val currentRoute: Class<out HasElement?> = event.activeChain[0].javaClass
        selectedTab = tabs[currentRoute]
    }

    @JvmOverloads
    fun addRoute(
        icon: VaadinIcon,
        routeClass: Class<out Component?>,
        title: String = getRouteTitle(routeClass)
    ) {
        val routerLink = RouterLink(routeClass)
        addNavIcon(routerLink, icon)
        routerLink.add(Span(title))
        val tab = Tab(routerLink)
        add(tab)
        tabs[routeClass] = tab
    }

    fun addButton(
        icon: VaadinIcon,
        title: String,
        clickListener: () -> Unit
    ) {
        val div = Div()
        addNavIcon(div, icon)
        div.add(Span(title))
        div.addClickListener { clickListener() }
        add(Tab(div))
    }

    private fun addNavIcon(parent: HasComponents, icon: VaadinIcon) {
        val i = icon.create()
        parent.add(i)
        i.style.set("box-sizing", "border-box")
            .set("margin-inline-end", "var(--lumo-space-m)")
            .set("margin-inline-start", "var(--lumo-space-xs)")["padding"] =
            "var(--lumo-space-xs)"
    }

    companion object {
        private fun getRouteTitle(routeClass: Class<*>): String {
            val title = routeClass.getAnnotation(
                PageTitle::class.java
            )
            return title?.value ?: routeClass.simpleName
        }
    }
}
