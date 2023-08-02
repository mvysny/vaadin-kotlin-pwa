// workaround for https://github.com/gradle/gradle/issues/9830
pluginManagement {
    val vaadinVersion: String by settings
    plugins {
        id("com.vaadin") version vaadinVersion
    }
}
