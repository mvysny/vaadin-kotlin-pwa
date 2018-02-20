package com.vaadin.flow.demo.helloworld

import com.vaadin.flow.server.BootstrapPageResponse
import com.vaadin.flow.server.BootstrapListener
import org.jsoup.nodes.Element
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener

/**
 * Modifies the Vaadin bootstrap page (the HTTP repoponse) in order to
 *
 *  * add links to favicons
 *  * add a link to the web app manifest
 *  * registers a service worker which offers the "You are offline" web page
 */
class CustomBootstrapListener : BootstrapListener {
    override fun modifyBootstrapPage(response: BootstrapPageResponse) {

        response.document.body().appendElement("script")
            .attr("src", "sw-register.js")
            .attr("async", "true")
            .attr("defer", "true")

        val head = response.document.head()

        // manifest needs to be prepended before scripts or it won't be loaded
        head.prepend("""<meta name="theme-color" content="#227aef">""")
        head.prepend("""<link rel="manifest" href="manifest.json">""")

        addFavIconTags(head)
    }

    private fun addFavIconTags(head: Element) {
        head.append("""<link rel="shortcut icon" href="icons/favicon.ico">""")
        head.append("""<link rel="icon" sizes="512x512" href="icons/icon-512.png">""")
        head.append("""<link rel="icon" sizes="192x192" href="icons/icon-192.png">""")
        head.append("""<link rel="icon" sizes="96x96" href="icons/icon-96.png">""")
        head.append("""<link rel="apple-touch-icon" sizes="512x512" href="icons/icon-512.png">""")
        head.append("""<link rel="apple-touch-icon" sizes="192x192" href="icons/icon-192.png">""")
        head.append("""<link rel="apple-touch-icon" sizes="96x96" href="icons/icon-96.png">""")
    }
}

class CustomVaadinServiceInitListener : VaadinServiceInitListener {

    override fun serviceInit(event: ServiceInitEvent) {
        event.addBootstrapListener(CustomBootstrapListener())
    }
}
