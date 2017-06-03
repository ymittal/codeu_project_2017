# ChatMe

ChatMe is a Firebase-powered chat application built for Android. This app was developed for the Google CodeU Spring 2017 program. It grew from what was originally a simple Java Swing based GUI application to its current state over a period of three months.

All user data is being stored using Firebase Database and Storage. We also authenticate our users through Firebase Authentication. Refer to [Firebase Docs](https://firebase.google.com/docs/) for details.

## Getting started

### Prerequisites

* Android Studio, preferably 2.3.+
* Android SDK Build Tools v25.0.1
* Emulator or Android Kitkat 4.4 or above 

### Installation

* Download [`google-services.json`](https://drive.google.com/open?id=0B_MDZBcgZXIJellRT2VHLUh1ejA) and save the file inside `android/app/` directory.
* Import `android/` folder in [Android Studio](https://developer.android.com/studio/index.html) and download all SDK requirements.
* Install project dependencies from __Tools > Android > Sync Gradle with Project Files__
* Run `app` module on an emulator or an Android device

_OR_

Install the lasest release [here](https://github.com/ymittal/codeu_project_2017/releases). You would need to enable **Install from Unknown Sources** in the Security Settings on your Android device.

## Navigating ChatMe

At startup, a user is prompted to create an account or log in using their email and password. On sign up, a unique username is assigned using their email address. On a successful log in, the application navigates to the conversations screen where they may create a new conversation (one-on-one or group) or navigate to their profile or the list of users.

#### Updating Profile

Users can update their profile picture, full name (name visible to the other users) and their password by navigating to the `ProfileFragment`. Be sure to click **Save Changes** after making your desired changes.

#### New Conversation

Conversations can be initiated by navigating to `UsersFragment` or by clicking the **+** symbol on `ConversationsFragment`. To create a 1-1 conversation, simply click on the user you wish to chat with. To create a group, long press on a user and proceed to select the users you wish to add to the group. Afterwads, click **Create Group** and you will be able to provide a group avatar and name before officially creating the group.

#### Sending Messages

To send and receive messages, navigate to `ConversationsFragment` and select the conversation you would like to view the messages of. Once there, you may send messages or view all the messages sent in the conversation previously.

#### Notifications

When someone sends you a message while the app is not in focus (minimized or killed), a notification in the status bar will be displayed, clicking on which will take you directly to the specific conversation.

## Testing

The `JUnit` tests can be found in `android/app/src/test` directory. Of course, you do not need a device to run these tests.

In Android, UI tests are called Instrumentation Tests, and they can be found in `android/app/src/androidTest`. You can run the Instrumentation Tests as follows:
* Use the left pane to change the Project view to __Project__ (default)
* Open `app/src/androidTest/`
* Right-click on `androidTest/java/` and click __Run 'All Tests'__
* Select your preferred device to run UI tests on

## Documentation

Please have a look at [**ChatMe Design Doc**](https://docs.google.com/document/d/1vKqHBiuqkTzTM-3H5VVxTm3h1JvIpV3foNUE4OLFWZM/edit?usp=sharing) for extensive documentation on our application, the technologies utilized, and our design process.

## Authors

This project is brought to you by the following people:
* [**Yash Mittal**](https://github.com/ymittal) <yashmittal2009@gmail.com>
* [**Brandon Long**](https://github.com/blong1996) <dev.brandon.long@gmail.com>
* [**Cory Brooks**](https://github.com/corybrooks) <corybrooks007@gmail.com>

Let us know if you have any questions about CodeU program or the project.