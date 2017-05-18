# Chat Me

Chat Me is a chat application built for Android. This app was developed for the Google CodeU 2017 Cohort

This application grew from what was originally a GUI application to what it is now over a three month period. 



## Getting Started

### Prerequisites

* Android Studio, preferably 2.3.+
* Android SDK Build Tools v25.0.1
* Emulator or Android Kitkat 4.4 or above 

### Installation

 Import the entire project in [Android Studio](https://developer.android.com/studio/index.html) and download all SDK requirements.
    * Install project dependencies from __Tools > Android > Sync Gradle with Project Files__
    * Run `app` module on an emulator or an Android device 

    **Note:** Please disable Instant Run feature on Android Studio before running the app. [`SugarORM`](https://github.com/satyan/sugar) has a [history](https://github.com/satyan/sugar/issues/75) of not creating tables when Instant Run is on.

## Testing

In Android, our tests are called Instrumentation Tests, and they can be found in `app/src/androidTest`. You can run the Instrumentation Tests as follows:
* Using the left pane, change the Project view to __Project__ (default)
* Open `app/src/androidTest/`
* Right-click on `androidTest/java/` and click __Run 'All Tests'__
* Select your preferred device to run UI tests on

In addition, the `JUnit` tests can be found in `app/src/test`. You do not need a device to run these tests.


## Documentation

For  extensive documentation about our application and the technologies utilized as well as details about why we chose to build it the way we did, see our [**Chat Me Design Doc**](https://docs.google.com/document/d/1vKqHBiuqkTzTM-3H5VVxTm3h1JvIpV3foNUE4OLFWZM/edit?usp=sharing)
## Authors

This project is brought to you by the following people:
* [**Yash Mittal**](https://github.com/ymittal) <yashmittal2009@gmail.com>
* [**Brandon Long**](https://github.com/blong1996) <dev.brandon.long@gmail.com>
* [**Cory Brooks**](https://github.com/corybrooks) <corybrooks007@gmail.com>
