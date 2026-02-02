package com.vaadin.pwademo

import com.github.mvysny.ktormvaadin.ActiveKtorm
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.lumo.Lumo
import com.vaadin.pwademo.tasks.Tasks
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.h2.Driver
import org.jetbrains.annotations.VisibleForTesting
import org.slf4j.LoggerFactory
import java.io.File
import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener
import jakarta.servlet.annotation.WebListener
import org.ktorm.database.Database

/**
 * Called by the Servlet Container to bootstrap your app. We need to bootstrap the Vaadin-on-Kotlin framework,
 * in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date.
 * After that's done, your app is ready to be serving client browsers.
 */
@WebListener
class Bootstrap : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        log.info("Starting up")

        log.info("Connecting to the database")
        // this will configure your database. For demo purposes, an in-memory embedded H2 database is used. To use a production-ready database:
        // 1. fill in the proper JDBC URL here
        // 2. make sure to include the database driver into the classpath, by adding a dependency on the driver into the build.gradle file.
        val cfg = HikariConfig().apply {
            driverClassName = Driver::class.java.name
            val db = File("/var/lib/vaadin-kotlin-pwa/db")
            jdbcUrl = if (db.exists() && !forceInmemoryDb) {
                "jdbc:h2:${db.absolutePath}/h2"
            } else {
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            }
            log.info("Using H2 database $jdbcUrl")
            username = "sa"
            password = ""
        }
        dataSource = HikariDataSource(cfg)
        ActiveKtorm.database = Database.connect(dataSource)

        // Makes sure the database is up-to-date
        log.info("Running DB migrations")
        val flyway: Flyway = Flyway.configure()
            .dataSource(dataSource)
            .load()
        flyway.migrate()
        log.info("Initialization complete")

        // pre-populates the database with a demo data if the database is empty
        Tasks.generateSampleData()
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        log.info("Shutting down")
        log.info("Closing connections to the database")
        dataSource.close()
        log.info("Shutdown complete")
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(Bootstrap::class.java)

        @VisibleForTesting
        var forceInmemoryDb = false

        @Volatile
        private lateinit var dataSource: HikariDataSource
    }
}

@BodySize(width = "100vw", height = "100vh")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@PWA(name = "Vaadin Kotlin PWA Demo", shortName = "VoK PWA Demo", iconPath = "icons/icon-512.png", themeColor = "#227aef", backgroundColor = "#227aef")
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("styles.css")
class AppShell: AppShellConfigurator
