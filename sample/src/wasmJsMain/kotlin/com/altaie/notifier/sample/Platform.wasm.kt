package com.altaie.notifier.sample

import com.altaie.notifier.notification.NotifierManager
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration

actual fun onApplicationStartPlatformSpecific() {
    println("Web app is initialized")
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Web(
            askNotificationPermissionOnStart = true,
            notificationIconPath = null
        )
    )
}