package com.vaadin.pwademo.components

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.div
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v10.themes
import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon

@Tag("app-header")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
class AppHeader : Component(), HasComponents, HasSize {
    init {
        element.setAttribute("fixed", "")
    }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).appHeader(block: (@VaadinDsl AppHeader).() -> Unit = {}) = init(AppHeader(), block)

@Tag("app-toolbar")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
class AppToolbar : Component(), HasComponents {
    val title: Div

    init {
        paperIconButton(VaadinIcon.MENU) {
            element.setAttribute("onclick", "drawer.toggle()")
        }
        title = div {
            element.setAttribute("main-title", "")
        }
    }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).appToolbar(block: (@VaadinDsl AppToolbar).() -> Unit = {}) = init(AppToolbar(), block)

@Tag("app-drawer")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
class AppDrawer : Component(), HasComponents {
    init {
        element.setAttribute("swipe-open", "")
        element.setAttribute("id", "drawer")
    }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).appDrawer(block: (@VaadinDsl AppDrawer).() -> Unit = {}) = init(AppDrawer(), block)

/**
 * A demo of how to use Polymer components. This particular component is the
 * [app-layout component](https://www.webcomponents.org/element/PolymerElements/app-layout).
 */
@Tag("app-header-layout")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
class AppHeaderLayout : Component(), HasComponents, HasSize {
    init {
        setSizeFull(); element.setAttribute("fullbleed", "")
    }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).appHeaderLayout(block: (@VaadinDsl AppHeaderLayout).() -> Unit = {}) = init(AppHeaderLayout(), block)

class NavMenuItem(icon: VaadinIcon, caption: String) : Button(caption, Icon(icon)) {
    init {
        classNames.add("navmenuitem")
        themes.add("large")
    }
    var selected: Boolean
        get() = classNames.contains("selected")
        set(value) { classNames.set("selected", value) }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).navMenuItem(icon: VaadinIcon, caption: String, block: (@VaadinDsl NavMenuItem).() -> Unit = {}) = init(NavMenuItem(icon, caption), block)
