package com.vaadin.pwademo.utils

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.PageTitle
import kotlin.reflect.KClass

private fun getRouteTitle(routeClass: KClass<*>): String {
    val title = routeClass.java.getAnnotation(PageTitle::class.java)
    return title?.value ?: routeClass.simpleName ?: ""
}

@VaadinDsl
fun (@VaadinDsl HasComponents).sideNav(label: String? = null, block: (@VaadinDsl SideNav).() -> Unit = {}) = init(
    SideNav(label), block)

@VaadinDsl
fun (@VaadinDsl SideNav).route(
    routeClass: KClass<out Component>,
    icon: VaadinIcon,
    title: String = getRouteTitle(routeClass),
    block: (@VaadinDsl SideNavItem).() -> Unit = {}
): SideNavItem {
    val item = SideNavItem(title, routeClass.java, icon.create())
    block(item)
    addItem(item)
    return item
}
