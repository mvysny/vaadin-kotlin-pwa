[![Build Status](https://travis-ci.org/mvysny/vaadin-kotlin-pwa.svg?branch=master)](https://travis-ci.org/mvysny/vaadin-kotlin-pwa)

# Vaadin Kotlin PWA Demo

A very simple Vaadin-based PWA demo with no JavaScript code whatsoever. Tailored towards Android developers who want to
use familiar strongly-typed language and don't feel at home with all that JavaScript browser stuff. Don't worry -
we will avoid JavaScript as much as we can.

### What's a PWA

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

We'll use the Vaadin framework. Vaadin framework is component-oriented,
which makes it feel familiar to Android developers (since Views are also components). Generally you nest your Buttons into a
lot of Vertical/HorizontalLayouts, but without the pain Android development typically brings.

* No Fragments - no crazy lifecycle of create/start/resume/whatever. The app simply always runs; the components attach and detach
  as you use the app. All UI components are Serializable; if needed they are automatically saved to a http session when there are no requests ongoing.
* Components are the unit of reuse - you compose components into more powerful components, or into views.
* You use Kotlin code to build your UIs in a DSL fashion. You structure the code as you see fit, no need to have 123213 layout XMLs for 45 screen sizes.
* You use CSS to style your app - you don't have to analyze Android Theme split into 3214132 XML files.
* You don't need Emulator nor a device - the browser can render the page as if in a mobile phone; you can switch
  between various resolutions in an instant.
* No DEX compilation. The compilation is fast.
* No install necessary - your users can simply browse your site; if they like the app, they can use their browser to easily create a
  shortcut to your app on their home screen.
* You will support both Android and iPhone with one code base.
* Avoid the trouble of publishing your app on the app stores; you don't need to pay $100 yearly to Apple; you don't need
  to share 30% of your income with Google and Apple.

Disadvantages:

* No access to native APIs - only the browser-provided APIs are available.
* No offline mode unless you develop your app in JavaScript, as a part of the service worker.
* You won't be able to achieve the same performance as with the native app; so no games.

### The UI Components

Now that the PWA thing is sorted out, let's construct the app UI. The [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt) hosts the main view of the app. It uses the Karibu-DSL library to define UIs;
you can read more about the [Karibu-DSL](https://github.com/mvysny/karibu-dsl). 

To quickly run the app, just run the following from your terminal:

```bash
$ ./gradlew appRun
```

Your app will be running on [http://localhost:8080](http://localhost:8080).

### Live Demo

[Vaadin Kotlin PWA](https://vaadin-kotlin-pwa.herokuapp.com/) running on Heroku. Please try it out with your mobile phone:
since the app is a PWA, the browser will allow you to add the link to the app to your home screen.
When you launch the app, a full-screen browser is launched which resembles an actual Android app.

### Develop with pleasure

You can download and install the [Intellij IDEA Community Edition](https://www.jetbrains.com/idea/download), then import this project into it. Android Studio is based on Intellij IDEA Community,
so you should feel immediately at home.

To launch your app, in Intellij just click on the rightmost Gradle tab, then select `Tasks / gretty / appRun`. Right-click on appRun and select Debug:
the app will start soon in a Jetty server. Just open your browser and hit [http://localhost:8080](http://localhost:8080).

The main meat of the UI is located in the [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt) - feel free to edit that file
and experiment for yourself. There are lots of pre-existing Vaadin components; you can check out the
[Beverage Buddy](https://github.com/mvysny/karibu-dsl#gradle-quickstart-application-vaadin-10flow) example app for more
examples of component usage. You should also read the [full Vaadin 10 (Flow) documentation](https://vaadin.com/docs/v10/flow/Overview.html). 

The browser is a very powerful IDE which can help you debug CSS- and layout-related issue. Take your time and read slowly through the following tutorials, to get acquinted with the browser
developer tools:

* [Chrome Developer Tools tutorial](https://developers.google.com/web/tools/chrome-devtools/)
* [Firefox Developer Tools tutorial](https://developer.mozilla.org/en-US/docs/Tools)

### Testing

It is very easy to test Vaadin-based apps - all you need to look up the components by selectors, for example a Button with the caption
of "Click Me". The [MainViewTest.kt](src/test/kotlin/com/vaadin/pwademo/MainViewTest.kt) sample test file shows a simple test which tests the
main screen. Read the [Serverless Testing](http://mavi.logdown.com/posts/3147601) article regarding the technical background on testing.

### Database

Without the database, we could store the task list into session only, which would then be gone when the server rebooted.
We will use the [Vaadin-on-Kotlin](http://vaadinonkotlin.eu/)'s Sql2o database support.

### Running in cloud

This app is nothing more but a plain war project. It can be run in any servlet container; you can run it inside Tomcat's docker image etc.
See [Running your app in cloud](http://mavi.logdown.com/posts/2870868) for more details.

This git repo also contains all files necessary for a seamless deployment onto Heroku. Just clone this git repo,
create a Heroku app, select github as the deployment method and press the `Deploy branch` button - your app should be
up and running in no time.
