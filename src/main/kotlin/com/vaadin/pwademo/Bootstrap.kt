package com.vaadin.pwademo

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.server.PWA
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.pool.HikariPool
import eu.vaadinonkotlin.VaadinOnKotlin
import eu.vaadinonkotlin.vokdb.dataSource
import org.flywaydb.core.Flyway
import org.h2.Driver
import org.slf4j.LoggerFactory
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

/**
 * Called by the Servlet Container to bootstrap your app. We need to bootstrap the Vaadin-on-Kotlin framework,
 * in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date.
 * After that's done, your app is ready to be serving client browsers.
 */
@WebListener
class Bootstrap : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) = try {
        log.info("Starting up")

        // this will configure your database. For demo purposes, an in-memory embedded H2 database is used. To use a production-ready database:
        // 1. fill in the proper JDBC URL here
        // 2. make sure to include the database driver into the classpath, by adding a dependency on the driver into the build.gradle file.
        val cfg = HikariConfig().apply {
            driverClassName = System.getenv("VOK_PWA_JDBC_DRIVER") ?: Driver::class.java.name
            jdbcUrl = System.getenv("VOK_PWA_JDBC_URL") ?: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            username = System.getenv("VOK_PWA_JDBC_USERNAME") ?: "sa"
            password = System.getenv("VOK_PWA_JDBC_PASSWORD") ?: ""
        }
        val isPostgreSQL = cfg.jdbcUrl.startsWith("jdbc:postgresql:")
        VaadinOnKotlin.dataSource = createConnectionPool(cfg)

        // Initializes the VoK framework
        log.info("Initializing VaadinOnKotlin")
        VaadinOnKotlin.init()

        // Makes sure the database is up-to-date
        log.info("Running DB migrations")
        val flyway: Flyway = Flyway.configure().apply {
            dataSource(VaadinOnKotlin.dataSource)
            if (isPostgreSQL) {
                locations("db.migration_postgresql")
            }
        } .load()
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

        /**
         * Repeatedly attempts to create a HikariCP
         */
        fun createConnectionPool(cfg: HikariConfig): HikariDataSource {
            while (true) {
                try {
                    return HikariDataSource(cfg)
                } catch (ex: HikariPool.PoolInitializationException) {
                    log.info("HikariPool failed to initialize. Backing off until the database becomes ready: $ex")
                    log.debug("HikariPool failed to initialize", ex)
                    Thread.sleep(5000L)
                }
            }
        }
    }
}

@BodySize(width = "100vw", height = "100vh")
@CssImport("./styles.css")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@PWA(name = "Vaadin Kotlin PWA Demo", shortName = "VoK PWA Demo", iconPath = "icons/icon-512.png", themeColor = "#227aef", backgroundColor = "#227aef")
class AppShell: AppShellConfigurator
