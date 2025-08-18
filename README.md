# KMPNotifier - Kotlin Multiplatform Push Notification
[![Build](https://github.com/Al-Taie/KMPNotifier/actions/workflows/build.yml/badge.svg)](https://github.com/Al-Taie/KMPNotifier/actions/workflows/build.yml) 
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.Al-Taie/notifier?color=blue)](https://search.maven.org/search?q=g:io.github.Al-Taie)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](https://img.shields.io/badge/platform-desktop-3474eb.svg?style=flat)
![badge-js](https://img.shields.io/badge/platform-js-fcba03.svg?style=flat)
![badge-wasm](https://img.shields.io/badge/platform-wasm-331f06.svg?style=flat)

[![Android Weekly badge](https://androidweekly.net/issues/issue-632/badge)](https://androidweekly.net/issues/issue-632)
[![Android Weekly badge](https://androidweekly.net/issues/issue-599/badge)](https://androidweekly.net/issues/issue-599)


Simple and easy to use Kotlin Multiplatform Push Notification library (using Firebase Cloud Messaging and Huawei Push Kit) targeting ios and android, and Local Notification targetting android, ios, desktop and web (js and wasm).  
This library is used in [FindTravelNow](https://github.com/Al-Taie/FindTravelNow-KMM/) production KMP project.
You can check out [Documentation](https://Al-Taie.github.io/KMPNotifier) for full library api information.  

![notifier](https://github.com/user-attachments/assets/a0f38159-b31d-4a47-97a7-cc230e15d30b)



**_Related Blog Posts_**  
[KMPNotifier Update: Web, Desktop, and New Features for Kotlin Multiplatform Notifications](https://proandroiddev.com/notifier-update-web-desktop-and-new-features-for-kotlin-multiplatform-notifications-529b489f5d9c)  
[How to implement Push Notifications in Kotlin Multiplatform](https://proandroiddev.com/how-to-implement-push-notification-in-kotlin-multiplatform-5006ff20f76c)  


## Features
  - 🔔 Local Notification (android, ios, desktop, js and wasm targets)  
  - 🔔 Push Notification (Firebase Cloud Messaging & Huawei Push Kit) (android and ios only)  
  - 📱 Multiplatform (android, iOS, desktop and web (js and wasm))  

## Installation
Before starting, ensure your project is set up in the respective cloud consoles:
- Firebase: Initialize your project in Firebase, add `google-services.json` to your Android app module, and `GoogleService-Info.plist` to your iOS app.
- Huawei: Initialize your project in Huawei AppGallery Connect and add `agconnect-services.json` to your Android app module.

## Minimum Requirements

- **Android:** `minSdkVersion 21`
- **iOS:** `iOS 14.1`


### Gradle Setup
KMPNotifier is available on Maven Central.

**1. Root `build.gradle.kts` (or `settings.gradle.kts`):**
Add `mavenCentral()` to repositories and declare the necessary service plugins.
```kotlin
// settings.gradle.kts or build.gradle.kts in project root
pluginManagement { // If in settings.gradle.kts
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
  // Apply KMPNotifier plugin (replace <latest_notifier_version> with the actual latest version)
  id("altaieaie.notifier") version "<latest_notifier_version>" apply false 
  id("com.android.application") version "<agp_version>" apply false // Or your AGP version
  id("org.jetbrains.kotlin.multiplatform") version "<kotlin_version>" apply false // Or your Kotlin version
  // For Firebase
  id("com.google.gms.google-services") version "4.4.3" apply false 
}
```

Then in your shared module you add dependency in `commonMain`. Latest version: [![Maven Central](https://img.shields.io/maven-central/v/io.github.Al-Taie/notifier?color=blue)](https://search.maven.org/search?q=g:io.github.Al-Taie). In iOS framework part export this library as well.
```kotlin

sourceSets {
  commonMain.dependencies {
    // Replace <latest_library_version> with the actual latest version from Maven Central
    api("io.github.Al-Taie:notifier:<latest_library_version>") 
  }
}

// For iOS, export the library in the framework block
listOf(iosX64(),iosArm64(),iosSimulatorArm64()).forEach { iosTarget ->
  iosTarget.binaries.framework {
    export("io.github.Al-Taie:notifier:<latest_library_version>")
    // ...
  }
}
```

**3. Android App Module (`androidApp/build.gradle.kts`):**
Apply the Android application plugin, the KMPNotifier plugin, and the respective service plugins.
```kotlin
plugins {
  id("com.android.application")
  id("altaieaie.notifier") // Apply KMPNotifier plugin
  
  // Apply only ONE of the following based on your needs:
  // For Firebase
  id("com.google.gms.google-services") 
  // OR
  // For Huawei (if you have a dedicated Huawei flavor or build)
}

android {
    // ...
    defaultConfig {
        // ...
        // Required for Huawei Push Kit if using Huawei services
        // The KMPNotifier plugin will use these to configure the manifest.
        // Ensure these match your agconnect-services.json
        manifestPlaceholders.putAll(
            mapOf(
                "huawei_agconnect_appid" to "YOUR_HUAWEI_APP_ID", // Replace with your actual App ID
                "huawei_agconnect_cpid" to "YOUR_HUAWEI_CPID"    // Replace with your actual CPID
            )
        )
    }
    // ...
}
```
**Note:** If you support both Firebase and Huawei, you will likely need to configure them per flavor. The `altaie.notifier` plugin aims to simplify this; check its documentation for advanced flavor-specific setups.

### Platform Setup
In all platforms on Application Start you need to initialize library using 
```kotlin 
//passing android, ios, desktop or web configuration depending on the platform
NotifierManager.initialize(NotificationPlatformConfiguration)  
```

<details>
  <summary>Android (Common Setup)</summary>

  ### Android Setup
  Regardless of whether you use Firebase or Huawei, initialize `NotifierManager` in your `Application` class.
  The `altaie.notifier` plugin, along with `com.google.gms.google-services` for firebase, will handle the necessary `AndroidManifest.xml` entries (like services and permissions).

 ```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /**
         * By default showPushNotification value is true.
         * When set showPushNotification to false foreground push notification will not be shown to user.
         * You can still get notification content using #onPushNotification listener method.
         */
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground, // Replace with your icon
                showPushNotification = true,
                // Add any other specific configurations if needed
            )
        )
    }
}
```

**Runtime Notification Permission (Android 13+):**
Starting from Android 13 (API Level 33), you need to request the `POST_NOTIFICATIONS` runtime permission.
```kotlin
// In your Activity or Composable
val permissionUtil by permissionUtil() // Assuming this is a utility you have
permissionUtil.askNotificationPermission() // This will ask permission on Android 13+
```
  
</details>

<details>
  <summary>Android (Huawei Specifics)</summary>
  
  ### Huawei Setup Notes
  1.  **`agconnect-services.json`**: Ensure this file from Huawei AppGallery Connect is placed in your Android app module's root directory (e.g., `androidApp/agconnect-services.json`).
  2.  **`manifestPlaceholders`**: As shown in the `androidApp/build.gradle.kts` setup, define `huawei_agconnect_appid` and `huawei_agconnect_cpid`. The `altaie.notifier` plugin uses these to configure the manifest correctly for Huawei services. You generally do not need to add Huawei services or permissions manually to the `AndroidManifest.xml` if the plugin is applied.
  3.  Initialize `NotifierManager` as shown in the "Android (Common Setup)" section.
</details>

<details>
  <summary>iOS</summary>

  ### iOS Setup
  First you just need to include FirebaseMessaging library to your ios app from Xcode. Then on application start you need to call both FirebaseApp initialization and NotifierManager initialization methods, and apnsToken setting as below. Don't forget to add Push Notifications and Background Modes (Remote Notifications) signing capability in Xcode.

```swift
import SwiftUI
import shared // Your shared module
import FirebaseCore
import FirebaseMessaging

class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

      FirebaseApp.configure() //important for Firebase
      
      //By default showPushNotification value is true.
      //When set showPushNotification to false foreground push  notification will not be shown.
      //You can still get notification content using #onPushNotification listener method.
      NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(
            showPushNotification: true,
            askNotificationPermissionOnStart: true, // Asks user for notification permission on app start
            notificationSoundName: nil // Optional: "default" or custom sound file e.g., "sound.caf"
          )
      )
      
    return true
  }

  func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken // For Firebase
        // If KMPNotifier supports direct APNS token handling for other services, add here.
  }
    
}

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```
</details>

<details>
  <summary>Desktop</summary>

### Desktop Setup
You need to put notification icon into `composeResources/drawable` (or your common resources folder for Compose Multiplatform). For more information:  
[Compose Desktop Resources](https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Native_distributions_and_local_execution/README.md#packaging-resources)
 ```kotlin
fun main() = application {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true,
            notificationIconPath = composeDesktopResourcesPath() + File.separator + "ic_notification.png"
        )
    )
    
    AppInitializer.onApplicationStart()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPNotifier Desktop",
    ) {
        println("Desktop app is started")
        App()

    }
}
```  

</details>

<details>
  <summary>Web</summary>

### Web Setup (Js and Wasm)
On application start initialize it using Web configuration
 ```kotlin
fun main()  {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Web(
            askNotificationPermissionOnStart = true,
            notificationIconPath = null // Or path to an icon accessible by the browser, e.g., "/icon.png"
        )
    )
    // Your web app initialization
}
```
**Note:**
If you are using mac make sure you also allow notifications for browser from system system settings in order to see web notifications.  
</details>

## Usage
You can send either local or push notification.

### Local Notification
Local notifications are supported on Android, iOS, Desktop, JS and Wasm targets. Image is supported on Android and iOS.
#### Send notification

```kotlin
val notifier = NotifierManager.getLocalNotifier()
notifier.notify {
  id = Random.nextInt(0, Int.MAX_VALUE)
  title = "Title from KMPNotifier"
  body = "Body message from KMPNotifier"
  payloadData = mapOf(
    Notifier.KEY_URL to "https://github.com/Al-Taie/KMPNotifier/",
    "extraKey" to "randomValue"
  )
  image = NotificationImage.Url("https://github.com/user-attachments/assets/a0f38159-b31d-4a47-97a7-cc230e15d30b")
}
```

#### Remove notification by Id or all notifications

```kotlin
 notifier.remove(notificationId) //Removes notification by Id  
 notifier.removeAll() //Removes all notification
```

### Push Notification
Push notifications are supported for Android (Firebase, Huawei) and iOS (Firebase/APNS).

#### Listen for push notification token changes
In this method you can send notification token to the server.
```kotlin
NotifierManager.addListener(object : NotifierManager.Listener {
  override fun onNewToken(token: String) {
    println("onNewToken: $token") //Update user token in the server if needed
  }
}) 
```

#### Receive Notification and Data payload in one callback
```kotlin
NotifierManager.addListener(object : NotifierManager.Listener {
  override fun onPushNotificationWithPayloadData(title: String?, body: String?, data: PayloadData) {
    //PayloadData is just typeAlias for Map<String,Any?>.
    println("Push Notification is received: Title: $title and Body: $body and Notification payloadData: $data")
  }
}) 
```

#### Receive notification type messages  
```kotlin
NotifierManager.addListener(object : NotifierManager.Listener {
  override fun onPushNotification(title:String?, body:String?) {
    println("Push Notification notification title: $title, body: $body")
  }
}) 
```

#### Receive data payload
```kotlin
NotifierManager.addListener(object : NotifierManager.Listener {
  override fun onPayloadData(data: PayloadData) {
    println("Push Notification payloadData: $data") //PayloadData is just typeAlias for Map<String,Any?>.
  }
}) 
```
And you need to call below platform-specific functions in order to receive payload data properly when the app is in the background or terminated.
##### Android
Call `NotifierManager.onCreateOrOnNewIntent(intent)` on launcher Activity's `onCreate` and `onNewIntent` methods.
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    NotifierManager.onCreateOrOnNewIntent(intent)
    // ...
}

override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    NotifierManager.onCreateOrOnNewIntent(intent)
}
```

##### iOS
Call `NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)` on application's `didReceiveRemoteNotification` method.
```swift
// In your AppDelegate.swift
func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
    NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
    return UIBackgroundFetchResult.newData
}
```  

#### Detecting notification click and get payload data
Make sure you follow previous step for getting payload data properly.
```kotlin
NotifierManager.addListener(object : NotifierManager.Listener {
    override fun onNotificationClicked(data: PayloadData) {
        super.onNotificationClicked(data)
        println("Notification clicked, Notification payloadData: $data")
    }
}) 
```   

#### Other functions
```kotlin
val pushNotifier = NotifierManager.getPushNotifier()
// pushNotifier.getToken() //Get current user push notification token
// pushNotifier.deleteMyToken() //Delete user's token for example when user logs out 
// pushNotifier.subscribeToTopic("new_users") 
// pushNotifier.unSubscribeFromTopic("new_users") 
```
For setting custom notification sound, check [#61](https://github.com/Al-Taie/KMPNotifier/pull/61#issuecomment-2275850021)  
For setting Intent data in Android (for deeplink), check [#60](https://github.com/Al-Taie/KMPNotifier/pull/60#issue-2454489089)    
For permissionUtil, or manually asking notification permission check [#27](https://github.com/Al-Taie/KMPNotifier/pull/27#issuecomment-2083639907)  

### Logging

If you want to see internal logs of the library, you can set a logger using:

```kotlin
NotifierManager.setLogger { message ->
    // Log the message, e.g., using your own logger or simply println
    println(message)
}
```
