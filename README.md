[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://github.com/mvysny/vaadin-kotlin-pwa/actions/workflows/gradle.yml/badge.svg)](https://github.com/mvysny/vaadin-kotlin-pwa/actions/workflows/gradle.yml)

# Vaadin Kotlin PWA Demo

A very simple [Vaadin-on-Kotlin](http://vaadinonkotlin.eu)-based PWA demo. Tailored towards Android developers who want to
use familiar strongly-typed language and don't feel at home with all that JavaScript browser stuff.
The development model of this app focuses on writing your logic in server-side Kotlin code; no JavaScript development is required.

The app is a very simple task list app, designed to showcase the following features:

* accessing the database
* validation in forms
* a web page masquerading as a native app

See the [online demo](https://v-herd.eu/vaadin-kotlin-pwa/).

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

# Preparing Environment

Please see the [Vaadin Boot](https://github.com/mvysny/vaadin-boot#preparing-environment) documentation
on how you run, develop and package this Vaadin-Boot-based app.

## Where Is The Service Worker `sw.js`?

The `sw.js`, all manifests and the offline page is now generated automatically
by Vaadin, via the [@PWA](https://vaadin.com/api/platform/14.0.10/com/vaadin/flow/server/PWA.html)
annotation. See [Creating PWA With Flow](https://vaadin.com/docs/flow/pwa/tutorial-pwa-pwa-with-flow.html)
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

To launch your app, simply open `Main.kt` and run the `main()` function. Just open your browser and hit [http://localhost:8080](http://localhost:8080).

The main meat of the UI is located in the [TaskListView.kt](src/main/kotlin/com/vaadin/pwademo/TaskListView.kt) - feel free to edit that file
and experiment for yourself. There are lots of pre-existing Vaadin components; you can check out the
[Beverage Buddy](https://github.com/mvysny/beverage-buddy-vok/) example app for more
examples of component usage. You should also read the [full Vaadin documentation](https://vaadin.com/docs/flow/Overview.html). 

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

## Running in cloud

This app is nothing more but a plain war project. It can be run in any servlet container; you can run it inside Tomcat's docker image etc.
See [Running your app in cloud](https://mvysny.github.io/Launch-your-Vaadin-on-Kotlin-app-quickly-in-cloud/) for more details.

### Docker

To produce a Docker image from this app, just run

```bash
$ docker build --no-cache -t test/vaadin-kotlin-pwa:latest .
```

To run the image, just run

```bash
$ docker run --rm -ti -p8080:8080 test/vaadin-kotlin-pwa
```

Done - your app now runs on [localhost:8080](http://localhost:8080). See
[Running WAR Apps in Docker JIB](https://mvysny.github.io/running-war-apps-in-docker-jib/) for more details.

### Google Cloud

To run this app in Google Cloud Kubernetes and Google Cloud SQL with MySQL, simply follow
the [Deploy Your Vaadin App To Google Cloud Kubernetes](https://mvysny.github.io/Deploy-Vaadin-App-To-Google-Cloud-Kubernetes/)
tutorial.

### Kubernetes/MicroK8s

See [Running Vaadin-on-Kotlin app in microk8s](https://mvysny.github.io/running-vaadin-app-in-microk8s/)
for more details.
