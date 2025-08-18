package com.altaie.notifier.sample

import com.altaie.notifier.notification.NotifierManager
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration

actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Ios(
            showPushNotification = true,
            askNotificationPermissionOnStart = true,
            notificationSoundName = "custom_notification_sound.wav"
        )
    )
}