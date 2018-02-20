[![Build Status](https://travis-ci.org/mvysny/vaadin-kotlin-pwa.svg?branch=master)](https://travis-ci.org/mvysny/vaadin-kotlin-pwa)

# Vaadin Kotlin PWA Demo

A very simple Vaadin-based PWA demo with no JavaScript code whatsoever. Tailored towards Android developers.

### What's PWA

Progressive Web App is something that can work offline. Since we're going to implement the logic server-side
to avoid JavaScript, that obviously won't work. So we'll make the app progressive enough - we'll
include all necessary things like the `manifest.json` and service workers, but they'll just show
the "You're offline" page when offline.

The PWA elements are registered in the [CustomBootstrapListener](src/main/kotlin/com/vaadin/pwademo/Bootstrap.kt).

### The UI Components

Now that the PWA thing is sorted out, let's construct the app UI. Vaadin framework is component-oriented,
which makes it feel familiar to Android developers. Generally you nest your Buttons into a
lot of Vertical/HorizontalLayouts.

You can read more about the Karibu-DSL here: [Karibu-DSL](https://github.com/mvysny/karibu-dsl).

The [MainView.kt](src/main/kotlin/com/vaadin/pwademo/MainView.kt) hosts the main view of the app.

To run the app, just run the following from your terminal:

```bash
$ ./gradlew appRun
```

Your app will be running on [http://localhost:8080](http://localhost:8080).

### Live Demo

[Vaadin Kotlin PWA](https://vaadin-kotlin-pwa.herokuapp.com/) running on Heroku.

