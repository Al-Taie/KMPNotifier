package com.altaie.notifier.sample

import com.altaie.notifier.extensions.composeDesktopResourcesPath
import com.altaie.notifier.notification.NotifierManager
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import java.io.File

actual fun onApplicationStartPlatformSpecific() {
    println("Desktop app is initialized")
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true,
            notificationIconPath = composeDesktopResourcesPath() + File.separator + "ic_notification.png"
        )
    )
}