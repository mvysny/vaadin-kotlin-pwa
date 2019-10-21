[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Build Status](https://travis-ci.org/mvysny/vaadin-kotlin-pwa.svg?branch=master)](https://travis-ci.org/mvysny/vaadin-kotlin-pwa)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Heroku](https://heroku-badge.herokuapp.com/?app=vaadin-kotlin-pwa&style=flat&svg=1)](https://vaadin-kotlin-pwa.herokuapp.com/)

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

Vaadin packages enormous amount of predefined components so you will do not typically have to develop your own components.
You will simply orchestrate a pre-made components server-side, with a pure Java/Kotlin code. 

You can read more about the [benefits of Vaadin development over Android](https://mvysny.github.io/Android-SDK-Why-literally-any-other-platform-is-better/).

## Running The App

Now that the PWA thing is sorted out, let's construct the app UI. The [TaskListView.kt](src/main/kotlin/com/vaadin/pwademo/TaskListView.kt) hosts
the main view of the app. It uses the Karibu-DSL library to define UIs;
you can read more about the [Karibu-DSL](https://github.com/mvysny/karibu-dsl). 

To quickly run the app on your machine, just run the following from your terminal:

```bash
$ ./gradlew appRun
```

Gradle will automatically download an embedded servlet container (Jetty) and will run your app in it. Your app will be running on
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
| [simplelogger.properties](src/main/resources/simplelogger.properties) | We're using [Slf4j](https://www.slf4j.org/) for logging and this is the configuration file for Slf4j
| [db/migration/](src/main/resources/db/migration) | Database upgrade instructions for the [Flyway](https://flywaydb.org/) framework. Database is upgraded on every server boot, to ensure it's always up-to-date. See the [Migration Naming Guide](https://flywaydb.org/documentation/migrations#naming) for more details.
| [webapp/](src/main/webapp) | static files provided as-is to the browser. See below for explanation
| [frontend/styles.html](src/main/webapp/frontend/styles.html) | The CSS styles applied to your web app. Vaadin by default uses [Vaadin Lumo Theme](https://vaadin.com/themes/lumo); you can tweak the Lumo theme by the means of setting CSS variables.
| [src/main/kotlin/](src/main/kotlin) | The main Kotlin sources of your web app. You'll be mostly editing files located in this folder.
| [Bootstrap.kt](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt) | When Servlet Container (such as Tomcat) starts your app, it will run the `Bootstrap.contextInitialized()` function before any calls to your app are made. We need to bootstrap the Vaadin-on-Kotlin framework, in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date. After that's done, your app is ready to be serving client browsers. The PWA-related files are registered in the [CustomBootstrapListener](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt).
| [TaskListView.kt](src/main/kotlin/com/vaadin/pwademo/TaskListView.kt) | The main view of the app, it defines how the UI looks like and how the components are nested into one another. The UI is defined by the means of so-called DSL; see [Karibu-DSL examples](https://github.com/mvysny/karibu-dsl#how-to-write-dsls-for-vaadin-8-and-vaadin8-v7-compat) for more examples.
| [AddTaskForm.kt](src/main/kotlin/com/vaadin/pwademo/AddTaskForm.kt) | An example of a reusable component which can be placed anywhere into your UI. The component may contain both the UI code and the business logic code which accesses the database.
| [Task.kt](src/main/kotlin/com/vaadin/pwademo/Task.kt) | An entity which represents a row in the `Task` database table. We're using Vaadin-on-Kotlin `vok-db` library to access the database.

### Where Is The Service Worker `sw.js`?

The `sw.js`, all manifests and the offline page is now generated automatically
by Vaadin, via the [@PWA](https://vaadin.com/api/platform/14.0.10/com/vaadin/flow/server/PWA.html)
annotation. See [Creating PWA With Flow](https://vaadin.com/docs/v14/flow/pwa/tutorial-pwa-pwa-with-flow.html)
for more details.

## Layouts

Vaadin 10 of course uses different algorithms than Android to perform the layouting. Luckily,
Vaadin 10 (or, rather CSS) knows a layout quite similar to Android's `LinearLayout` - the flexbox.
Please read the
[Vaadin 10 server-side layouting for Vaadin 8 and Android developers](https://mvysny.github.io/Vaadin-10-server-side-layouting-for-Vaadin-8-and-Android-developers/)
article on how to use `VerticalLayout` and `HorizontalLayout` which use flexbox under the hood, but
sports an API which may be familar to Android developers.

## Develop with pleasure

You can download and install the [Intellij IDEA Community Edition](https://www.jetbrains.com/idea/download), then import this project into it. Android Studio is based on Intellij IDEA Community,
so you should feel immediately at home.

To launch your app, in Intellij just click on the rightmost Gradle tab, then select `Tasks / gretty / appRun`. Right-click on appRun and select Debug:
the app will start soon in a Jetty server. Just open your browser and hit [http://localhost:8080](http://localhost:8080).

The main meat of the UI is located in the [TaskListView.kt](src/main/kotlin/com/vaadin/pwademo/TaskListView.kt) - feel free to edit that file
and experiment for yourself. There are lots of pre-existing Vaadin components; you can check out the
[Beverage Buddy](https://github.com/mvysny/beverage-buddy-vok/) example app for more
examples of component usage. You should also read the [full Vaadin 10 (Flow) documentation](https://vaadin.com/docs/v14/flow/Overview.html). 

The browser is a very powerful IDE which can help you debug CSS- and layout-related issue. Take your time and read slowly through the following tutorials, to get acquinted with the browser
developer tools:

* [Chrome Developer Tools tutorial](https://developers.google.com/web/tools/chrome-devtools/)
* [Firefox Developer Tools tutorial](https://developer.mozilla.org/en-US/docs/Tools)

### Testing

It is very easy to test Vaadin-based apps - all you need to look up the components by selectors, for example a Button with the caption
of "Click Me". The [TaskListTest.kt](src/test/kotlin/com/vaadin/pwademo/TaskListTest.kt) sample test file shows a simple test which tests the
main screen. Read the [Browserless Testing Project](https://github.com/mvysny/karibu-testing) documentation regarding the background on this testing approach.

### Database

Without the database, we could store the task list into session only, which would then be gone when the server rebooted.
We will use the [Vaadin-on-Kotlin](http://vaadinonkotlin.eu/)'s Sql2o database support. To make things easy we'll
use in-memory H2 database which will be gone when the server is rebooted - *touche* :-D

We will use [Flyway](https://flywaydb.org/) for database migration. Check out [Bootstrap.kt](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt)
on how the [migration scripts](src/main/resources/db/migration) are ran when the app is initialized.

The [Task](src/main/kotlin/com/vaadin/pwademo/Task.kt) entity will be mapped to the database; inheriting from Entity and Dao
will make it inherit bunch of useful methods such as `findAll()` and `save()`. It will also gain means of
providing all of its instances via a `DataProvider`. See the [TaskListView.kt](src/main/kotlin/com/vaadin/pwademo/TaskListView.kt)
Grid configuration for details.

See the [Back to Base](https://mvysny.github.io/back-to-base-make-sql-great-again/) article on how the finder methods are attached to the entity,
and how the lookup and save works.

#### Using MySQL

To connect to MySQL instead of H2, just run the MySQL in Docker:

```bash
docker run --rm -ti -e MYSQL_ROOT_PASSWORD=PfJ739VoMMDrs -e MYSQL_DATABASE=vok_pwa -e MYSQL_USER=testuser -e MYSQL_PASSWORD=PfJ739VoMMDrs -p 127.0.0.1:3306:3306 mysql:5.7.21
```

Then, run this app as follows:

```bash
export VOK_PWA_JDBC_DRIVER=com.mysql.jdbc.Driver
export VOK_PWA_JDBC_URL="jdbc:mysql://localhost:3306/vok_pwa?useUnicode=true"
export VOK_PWA_JDBC_USERNAME="root"
export VOK_PWA_JDBC_PASSWORD=PfJ739VoMMDrs
./gradlew appRun
```

## Running in cloud

This app is nothing more but a plain war project. It can be run in any servlet container; you can run it inside Tomcat's docker image etc.
See [Running your app in cloud](https://mvysny.github.io/Launch-your-Vaadin-on-Kotlin-app-quickly-in-cloud/) for more details.

### Docker

To produce a Docker image from this app, just run

```bash
./gradlew clean build jibDockerBuild --image=test/vaadin-kotlin-pwa
```

To run the image, just run

```bash
docker run --rm -ti -p8080:8080 test/vaadin-kotlin-pwa
```

Done - your app now runs on [localhost:8080](http://localhost:8080). See
[Running WAR Apps in Docker JIB](https://mvysny.github.io/running-war-apps-in-docker-jib/) for more details.

### Heroku

This git repo also contains all files necessary for a seamless deployment onto Heroku. Just clone this git repo,
create a Heroku app, select github as the deployment method and press the `Deploy branch` button - your app should be
up and running in no time.

### Google Cloud

To run this app in Google Cloud Kubernetes and Google Cloud SQL with MySQL, simply follow
the [Deploy Your Vaadin App To Google Cloud Kubernetes](https://mvysny.github.io/Deploy-Vaadin-App-To-Google-Cloud-Kubernetes/)
tutorial.
