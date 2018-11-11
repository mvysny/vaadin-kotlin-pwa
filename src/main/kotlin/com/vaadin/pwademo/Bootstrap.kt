package com.vaadin.pwademo

import eu.vaadinonkotlin.VaadinOnKotlin
import com.vaadin.flow.server.*
import eu.vaadinonkotlin.sql2o.dataSource
import eu.vaadinonkotlin.sql2o.dataSourceConfig
import org.flywaydb.core.Flyway
import org.h2.Driver
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

/**
 * Modifies the Vaadin bootstrap page (the HTTP response) in order to
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

/**
 * Called by the Vaadin Servlet; we will use it to hook into the initialization process, in order to be able to modify the html page a bit.
 */
class CustomVaadinServiceInitListener : VaadinServiceInitListener {
    override fun serviceInit(event: ServiceInitEvent) {
        event.addBootstrapListener(CustomBootstrapListener())
    }
}

/**
 * Called by the Servlet Container to bootstrap your app. We need to bootstrap the Vaadin-on-Kotlin framework,
 * in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date.
 * After that's done, your app is ready to be serving client browsers.
 */
@WebListener
class Bootstrap: ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) = try {
        log.info("Starting up")

        // this will configure your database. For demo purposes, an in-memory embedded H2 database is used. To use a production-ready database:
        // 1. fill in the proper JDBC URL here
        // 2. make sure to include the database driver into the classpath, by adding a dependency on the driver into the build.gradle file.
        VaadinOnKotlin.dataSourceConfig.apply {
            driverClassName = Driver::class.java.name
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
        }

        // Initializes the VoK framework
        log.info("Initializing VaadinOnKotlin")
        VaadinOnKotlin.init()

        // Makes sure the database is up-to-date
        log.info("Running DB migrations")
        val flyway = Flyway.configure()
            .dataSource(VaadinOnKotlin.dataSource)
            .load()
        flyway.migrate()
        log.info("Initialization complete")

        // pre-populates the database with a demo data
        Task.generateSampleData()
    } catch (t: Throwable) {
        log.error("Bootstrap failed!", t)
        throw t
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        log.info("Shutting down")
        log.info("Destroying VaadinOnKotlin")
        VaadinOnKotlin.destroy()
        log.info("Shutdown complete")
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(Bootstrap::class.java)
    }
}
