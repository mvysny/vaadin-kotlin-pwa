[![Build Status](https://travis-ci.org/mvysny/vaadin-kotlin-pwa.svg?branch=master)](https://travis-ci.org/mvysny/vaadin-kotlin-pwa)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# Vaadin Kotlin PWA Demo

A very simple [Vaadin-on-Kotlin](http://vaadinonkotlin.eu)-based PWA demo. Tailored towards Android developers who want to
use familiar strongly-typed language and don't feel at home with all that JavaScript browser stuff.
The development model of this app focuses on writing your logic in server-side Kotlin code; no JavaScript development is required.

The app is a very simple task list app, designed to showcase the following features:

* accessing the database
* validation in forms
* a web page masquerading as a native app

### Live Demo

The app [runs live on Heroku](https://vaadin-kotlin-pwa.herokuapp.com/). Try it out with your mobile phone:
since the app is a PWA, the browser on your phone will allow you to add the link to the app to your home screen.
When you launch the app from the home screen launcher, a full-screen browser is launched which resembles an actual Android app.

## What's a PWA

Progressive Web App is a web page that the mobile phone browser can download and it can then work offline, to a certain degree.
PWA also allows the user to save an app shortcut as an icon to his/her home screen; when launched from that icon,
the app launches completely full-screen, without any URL bar, mimicking a native app completely.

Since we're going to implement the logic server-side
to avoid JavaScript, offline mode obviously won't work. So we'll make the app progressive just enough - we'll
include all necessary things like the `manifest.json` and service workers, but they'll just show
the "You're offline" page when offline. However, there is an effort ongoing in this area, so let's wait and see.

The PWAs also tend to adapt to the screen size (so-called Responsiveness), typically with a CSS rules.
You can check out what PWAs are, at [Vaadin Progressive Web Apps](https://vaadin.com/pwa).

### Comparison with Android development

We'll use the [Vaadin](https://vaadin.com/) framework. The reason behind this is that Vaadin framework is a component-oriented Java-based web framework.
Because of that, Vaadin's programming model closely
resembles and feels familiar to Android developers. You just switch your Android Views into Vaadin Components
and you continue nesting your Buttons into a bunch of Vertical/HorizontalLayouts - exactly as you would do with the Android development, but
without the pains of using the Android development model.

You can read more about the [benefits of Vaadin development over Android](http://mavi.logdown.com/posts/6848317).

## Running The App

Now that the PWA thing is sorted out, let's construct the app UI. The [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt) hosts
the main view of the app. It uses the Karibu-DSL library to define UIs;
you can read more about the [Karibu-DSL](https://github.com/mvysny/karibu-dsl). 

To quickly run the app on your machine, just run the following from your terminal:

```bash
$ ./gradlew appRun
```

Gradle will download an embedded servlet container (Jetty) and will run your app in it. Your app will be running on
[http://localhost:8080](http://localhost:8080).

## Dissection of project files

Let's look at all files that this PWA project is composed of, and what are the points where you'll add functionality:

| Files | Meaning
| ----- | -------
| [settings.gradle](settings.gradle), [build.gradle](build.gradle) | [Gradle](https://gradle.org/) build tool configuration files. Gradle is used to compile your app, download all dependency jars and build a war file
| [gradlew](gradlew), [gradlew.bat](gradlew.bat), [gradle/](gradle) | Gradle runtime files, so that you can build your app from command-line simply by running `./gradlew`, without having to download and install Gradle distribution yourself.
| [.travis.yml](.travis.yml) | Configuration file for [Travis-CI](http://travis-ci.org/) which tells Travis how to build the app. Travis watches your repo; it automatically builds your app and runs all the tests after every commit.
| [Procfile](Procfile) | Tells [Heroku](https://www.heroku.com/) hosting service how to run your app in a cloud. See below on how to deploy your app on Heroku for free.
| [.gitignore](.gitignore) | Tells [Git](https://git-scm.com/) to ignore files that can be produced from your app's sources - be it files produced by Gradle, Intellij project files etc.
| [src/main/resources/](src/main/resources) | A bunch of static files not compiled by Kotlin in any way; see below for explanation.
| [logback.xml](src/main/resources/logback.xml) | We're using [Slf4j](https://www.slf4j.org/) for logging and this is the configuration file for Slf4j
| [VaadinServiceInitListener](src/main/resources/META-INF/services/com.vaadin.flow.server.VaadinServiceInitListener) | A Java Service registration for the [CustomVaadinServiceInitListener](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt) class which will allow us to modify the html page a bit, to include icons, the PWA manifest file etc.
| [db/migration/](src/main/resources/db/migration) | Database upgrade instructions for the [Flyway](https://flywaydb.org/) framework. Database is upgraded on every server boot, to ensure it's always up-to-date. See the [Migration Naming Guide](https://flywaydb.org/documentation/migrations#naming) for more details.
| [webapp/](src/main/webapp) | static files provided as-is to the browser. See below for explanation
| [manifest.json](src/main/webapp/manifest.json) | the PWA app descriptor file. The file is linked to in the `index.html` file `head` element, by the `CustomVaadinServiceInitListener`
| [sw.js](src/main/webapp/sw.js) | The service worker which will continue running even when you navigate away from the web page. Allows for notification and other stuff. In this app we're using it only to show a nice "You're offline" page when the phone is offline.
| [sw-register.js](src/main/webapp/sw-register.js) | Registers the service worker into the browser. This script is run by linking onto it from the `index.html` file `head` element, by the `CustomVaadinServiceInitListener`
| [offline-page.html](src/main/webapp/offline-page.html) | A static web page shown when the phone is offline. Just open this file with your browser straight from the file system and edit it any way you like.
| [images/](src/main/webapp/images), [icons/](src/main/webapp/icons) | Images used when offline (they are referenced from the `offline-page.html` file); icons are used when the user stores a link to your web app into his phone's home screen (they are referenced from the `manifest.json` file).
| [frontend/styles.html](src/main/webapp/frontend/styles.html) | The CSS styles applied to your web app. Vaadin by default uses [Vaadin Lumo Theme](https://vaadin.com/themes/lumo); you can tweak the Lumo theme by the means of setting CSS variables.
| [src/main/kotlin/](src/main/kotlin) | The main Kotlin sources of your web app. You'll be mostly editing files located in this folder.
| [Bootstrap.kt](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt) | When Servlet Container (such as Tomcat) starts your app, it will run the `Bootstrap.contextInitialized()` function before any calls to your app are made. We need to bootstrap the Vaadin-on-Kotlin framework, in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date. After that's done, your app is ready to be serving client browsers. The PWA-related files are registered in the [CustomBootstrapListener](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt).
| [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt) | The main view of the app, it defines how the UI looks like and how the components are nested into one another. The UI is defined by the means of so-called DSL; see [Karibu-DSL examples](https://github.com/mvysny/karibu-dsl#how-to-write-dsls-for-vaadin-8-and-vaadin8-v7-compat) for more examples.
| [AddTaskForm.kt](src/main/kotlin/com/vaadin/pwademo/AddTaskForm.kt) | An example of a reusable component which can be placed anywhere into your UI. The component may contain both the UI code and the business logic code which accesses the database.
| [Task.kt](src/main/kotlin/com/vaadin/pwademo/Task.kt) | An entity which represents a row in the `Task` database table. We're using Vaadin-on-Kotlin `vok-db` library to access the database.

## Layouts

Vaadin 10 of course uses different algorithms than Android to perform the layouting. Luckily,
Vaadin 10 (or, rather CSS) knows a layout quite similar to Android's `LinearLayout` - the flexbox.
Please read the
[Vaadin 10 server-side layouting for Vaadin 8 and Android developers](http://mavi.logdown.com/posts/6855605)
article on how to use `VerticalLayout` and `HorizontalLayout` which use flexbox under the hood, but
sports an API which may be familar to Android developers.

## Develop with pleasure

You can download and install the [Intellij IDEA Community Edition](https://www.jetbrains.com/idea/download), then import this project into it. Android Studio is based on Intellij IDEA Community,
so you should feel immediately at home.

To launch your app, in Intellij just click on the rightmost Gradle tab, then select `Tasks / gretty / appRun`. Right-click on appRun and select Debug:
the app will start soon in a Jetty server. Just open your browser and hit [http://localhost:8080](http://localhost:8080).

The main meat of the UI is located in the [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt) - feel free to edit that file
and experiment for yourself. There are lots of pre-existing Vaadin components; you can check out the
[Beverage Buddy](https://github.com/mvysny/beverage-buddy-vok/) example app for more
examples of component usage. You should also read the [full Vaadin 10 (Flow) documentation](https://vaadin.com/docs/v10/flow/Overview.html). 

The browser is a very powerful IDE which can help you debug CSS- and layout-related issue. Take your time and read slowly through the following tutorials, to get acquinted with the browser
developer tools:

* [Chrome Developer Tools tutorial](https://developers.google.com/web/tools/chrome-devtools/)
* [Firefox Developer Tools tutorial](https://developer.mozilla.org/en-US/docs/Tools)

### Testing

It is very easy to test Vaadin-based apps - all you need to look up the components by selectors, for example a Button with the caption
of "Click Me". The [MainViewTest.kt](src/test/kotlin/com/vaadin/pwademo/MainViewTest.kt) sample test file shows a simple test which tests the
main screen. Read the [Browserless Testing](http://mavi.logdown.com/posts/3147601) article regarding the technical background on testing.

### Database

Without the database, we could store the task list into session only, which would then be gone when the server rebooted.
We will use the [Vaadin-on-Kotlin](http://vaadinonkotlin.eu/)'s Sql2o database support. To make things easy we'll
use in-memory H2 database which will be gone when the server is rebooted - *touche* :-D

We will use [Flyway](https://flywaydb.org/) for database migration. Check out [Bootstrap.kt](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt)
on how the [migration scripts](src/main/resources/db/migration) are ran when the app is initialized.

The [Task](src/main/kotlin/com/vaadin/pwademo/Task.kt) entity will be mapped to the database; inheriting from Entity and Dao
will make it inherit bunch of useful methods such as `findAll()` and `save()`. It will also gain means of
providing all of its instances via a `DataProvider`. See the [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt)
Grid configuration for details.

See the [Back to Base](http://mavi.logdown.com/posts/5771422) article on how the finder methods are attached to the entity,
and how the lookup and save works.

## Running in cloud

This app is nothing more but a plain war project. It can be run in any servlet container; you can run it inside Tomcat's docker image etc.
See [Running your app in cloud](http://mavi.logdown.com/posts/2870868) for more details.

This git repo also contains all files necessary for a seamless deployment onto Heroku. Just clone this git repo,
create a Heroku app, select github as the deployment method and press the `Deploy branch` button - your app should be
up and running in no time.

