package com.vaadin.pwademo

import com.github.vok.karibudsl.flow.VaadinDsl
import com.github.vok.karibudsl.flow.div
import com.github.vok.karibudsl.flow.icon
import com.github.vok.karibudsl.flow.init
import com.vaadin.flow.component.*
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcons

@Tag("app-header")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
open class AppHeader : Component(), HasComponents, HasSize {
    init {
        element.setAttribute("reveals", "")
    }
}

fun (@VaadinDsl HasComponents).appHeader(block: (@VaadinDsl AppHeader).() -> Unit = {}) = init(AppHeader(), block)

@Tag("app-toolbar")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
open class AppToolbar : Component(), HasComponents {
    val title: Div

    init {
        paperIconButton(VaadinIcons.MENU) {
            element.setAttribute("onclick", "drawer.toggle()")
        }
        title = div {
            element.setAttribute("main-title", "")
        }
    }
}

fun (@VaadinDsl HasComponents).appToolbar(block: (@VaadinDsl AppToolbar).() -> Unit = {}) = init(AppToolbar(), block)

@Tag("app-drawer")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
class AppDrawer : Component(), HasComponents {
    init {
        element.setAttribute("swipe-open", "")
        element.setAttribute("id", "drawer")
    }
}

fun (@VaadinDsl HasComponents).appDrawer(block: (@VaadinDsl AppDrawer).() -> Unit = {}) = init(AppDrawer(), block)

/**
 * A demo of how to use Polymer components. This particular component is the
 * [app-layout component](https://www.webcomponents.org/element/PolymerElements/app-layout).
 */
@Tag("app-header-layout")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
open class AppHeaderLayout : Component(), HasComponents, HasSize {
    init {
        setSizeFull(); element.setAttribute("fullbleed", "")
    }
}

/**
 * Creates an icon component that displays the given `icon` from given `collection`.
 * @param collection the icon collection
 * @param icon the icon name
 */
@Tag("paper-icon-button")
@HtmlImport("frontend://bower_components/paper-icon-button/paper-icon-button.html")
class PaperIconButton(collection: String, icon: String) : Component(), HasClickListeners<PaperIconButton> {
    /**
     * Creates an icon component that displays given Vaadin [icon].
     */
    constructor(icon: VaadinIcons) : this("vaadin", icon.name.toLowerCase().replace('_', '-'))
    init {
        element.setAttribute("icon", "$collection:$icon")
    }
}

fun (@VaadinDsl HasComponents).paperIconButton(icon: VaadinIcons, block: (@VaadinDsl PaperIconButton).() -> Unit = {}) = init(PaperIconButton(icon), block)
