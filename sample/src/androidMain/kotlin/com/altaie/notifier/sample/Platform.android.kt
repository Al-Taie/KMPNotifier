package com.altaie.notifier.sample

import android.content.ContentResolver
import android.net.Uri
import com.altaie.notifier.notification.NotifierManager
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration

actual fun onApplicationStartPlatformSpecific() {
    val customNotificationSound =
        Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + "com.altaie.notifier.sample" + "/" + R.raw.custom_notification_sound)
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.ic_launcher_foreground,
            showPushNotification = true,
            notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
                soundUri = customNotificationSound.toString()
            )
        )
    )
}