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
 
To properly run the application, you will need to download [google-services.json](https://drive.google.com/open?id=0B_MDZBcgZXIJellRT2VHLUh1ejA) and save it in the `app/` directory

   * Install project dependencies from __Tools > Android > Sync Gradle with Project Files__
   * Run `app` module on an emulator or an Android device
 

    **Note:** Please disable Instant Run feature on Android Studio before running the app. [SugarORM](https://github.com/satyan/sugar) has a [history](https://github.com/satyan/sugar/issues/75) of not creating tables when Instant Run is on.

## Navigating the Application

At startup, the user is prompted to create an account or log in using their email and password. On creation of a new account, their username is created using the front half of their email address. On a successful log in, the application navigates to the `Conversations` Fragment where they may create a new conversation or navigate to the `Users` or `Profile` Fragments.

### Updating Profile

Users can update their profile picture, full name (the name that is visible to other users) and their password by navigating to the `Profile` Fragment. Be sure to click `Save Changes` after making your desired changes.

### New Conversation

New conversations can be initiated by navigating to the `Users` Fragment or by clicking the `+` symbol on the `Conversations` Fragment. To create a 1-1 conversation, simply click on the user you wish to chat with. To create a group, long press a user and then proceed to select the users you wish to add to the group. Next, click `Create Group`, and you will be able to provide a group name and avatar before creating the offical group.

### Sending Messages

To send and receive messages, navigate to the `Conversation` Fragment and select the conversation you'd like to chat in. Once there, you may send messages and view all the messages that have been sent in the conversation.

### Notifications

When a new message has been sent to you while you are not within the application, it will be displayed in the notification drawer on your android device.


## Storage and User Authentication

All user data is being stored in a Firebase Database and Firebase Storage. Firebase also handles all user authentification. [Firebase Documentation](https://firebase.google.com/docs/)

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
