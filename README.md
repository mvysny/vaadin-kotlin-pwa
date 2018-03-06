[![Build Status](https://travis-ci.org/mvysny/vaadin-kotlin-pwa.svg?branch=master)](https://travis-ci.org/mvysny/vaadin-kotlin-pwa)

# Vaadin Kotlin PWA Demo

A very simple Vaadin-based PWA demo with no JavaScript code whatsoever. Tailored towards Android developers who want to
use familiar strongly-typed language and don't feel at home with all that JavaScript browser stuff. Don't worry -
we will avoid JavaScript as much as we can.

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
the app launches completely full-screen, without any URL bar; now it completely mimics a native app.

Since we're going to implement the logic server-side
to avoid JavaScript, offline mode obviously won't work. So we'll make the app progressive just enough - we'll
include all necessary things like the `manifest.json` and service workers, but they'll just show
the "You're offline" page when offline.

The PWAs also tend to adapt to the screen size (so-called Responsiveness), typically with a CSS rules.
You can check out what PWAs are, at [Vaadin Progressive Web Apps](https://vaadin.com/pwa).

The PWA-related files are registered in the [CustomBootstrapListener](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt).

### Comparison with Android development

We'll use the [Vaadin](https://vaadin.com/) framework. The reason behind this is that Vaadin framework is a component-oriented Java-based web framework.
Because of that, Vaadin's programming model closely
resembles and feels familiar to Android developers. You just switch your Android Views into Vaadin Components
and you continue nesting your Buttons into a bunch of Vertical/HorizontalLayouts - exactly as you would do with the Android development. 

All that without the pain Android development typically brings:

* No Fragments - no crazy lifecycle of create/start/resume/whatever. The app simply always runs.
  In Vaadin, components are used for everything; nesting components is perfectly
  fine as opposed to nesting fragments, which tend to produce weird random crashes (the famous `moveToState()` method).
* Vaadin components are lightweight - they don't use any native resources, they just produce html for the browser to draw. There is no complex
  lifecycle - the components simply attach and detach to the screen (represented by the `UI` class) as the user uses the app.
  Since the components do not use any native resources, they are simply garbage-collected when not needed anymore.
  All UI components are Serializable; in a rare case when the session passivation is needed, they are automatically saved along with the http session when there are no requests ongoing.
* Components are the unit of reuse - you compose components into more powerful components, into forms, or even into views. The component
  may be as small as a Button, or as big as a VerticalLayout with a complete form in it.
* You use Kotlin code to build your UIs in a DSL fashion. You structure the code as you see fit; no need to have 123213 layout XMLs for 45 screen sizes in one large folder.
* You use a single file CSS to style your app - you don't have to analyze Android Theme shattered into 3214132 XML files.
* You don't need Emulator nor a device - the browser can render the page as if in a mobile phone; you can switch
  between various resolutions in an instant.
* No DEX compilation. The compilation is fast.
* No need to fetch stuff in background thread - the Vaadin UI thread lives server-side and it is completely separated from browser’s UI thread.
  It is quite typical for a Vaadin app to block in the UI code until the data is fetched.
* PWAs do not have to be installed. Your users can simply browse your site; if they like the app, they can use their browser to easily create a
  shortcut to your app on their home screen.
* You will support both Android and iPhone with one code base.
* Avoid the trouble of publishing your app on the app stores: the app does not need to be reviewed by anybody; you will not need to pay $100 yearly to Apple; you don't need
  to share 30% of your income with Google and Apple.
* No data syncing necessary since the data will reside on the server.
* Android's way of requesting for runtime permissions is *so* horrible: it will call back not your Runnable, but your Activity/Fragment.
  That forbids you from spliting your code into smaller objects as you'd like; instead you have to keep all of your logic in an Activity/Fragment.
  And since [runtime permissions will be required for all Android apps at Google Play](https://android-developers.googleblog.com/2017/12/improving-app-security-and-performance.html) at the end of 2018, instead of having to port your app to runtime permissions you can move to another platform.

Of course there is no silver bullet. This approach has the following disadvantages:

* No access to native APIs - only the browser-provided APIs are available. While you can access GPS, you can't access for example the SD card (with local user photos), can't make calls etc.
* No offline mode unless you develop your app fully or in partial in JavaScript, as a part of the service worker.
* You won't be able to achieve the same performance as with the native app; so no games.
* You will need a SSL certificate since PWAs only work over https
* You will need to pay for cloud hosting, SSL certificate and DNS domain yourself. The most easy way to set up all that is with Heroku;
  you can also pay for a virtualized Linux server, get the SSL certificate for free from Let's Encrypt and setup Tomcat to host your app.
* You are responsible for your app's security; when your app is hacked, all of your user data may be compromised.
* All of your users will use the same VM; 10000+ users can kill your server unless you load-balance. Luckily it's quite easy with most good cloud providers.

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
| [Bootstrap.kt](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt) | When Servlet Container (such as Tomcat) starts your app, it will run the `Bootstrap.contextInitialized()` function before any calls to your app are made. We need to bootstrap the Vaadin-on-Kotlin framework, in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date. After that's done, your app is ready to be serving client browsers.
| [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt) | The main view of the app, it defines how the UI looks like and how the components are nested into one another. The UI is defined by the means of so-called DSL; see [Karibu-DSL examples](https://github.com/mvysny/karibu-dsl#how-to-write-dsls-for-vaadin-8-and-vaadin8-v7-compat) for more examples.
| [AddTaskForm.kt](src/main/kotlin/com/vaadin/pwademo/AddTaskForm.kt) | An example of a reusable component which can be placed anywhere into your UI. The component may contain both the UI code and the business logic code which accesses the database.
| [Task.kt](src/main/kotlin/com/vaadin/pwademo/Task.kt) | An entity which represents a row in the `Task` database table. We're using Vaadin-on-Kotlin `vok-db` library to access the database.


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

