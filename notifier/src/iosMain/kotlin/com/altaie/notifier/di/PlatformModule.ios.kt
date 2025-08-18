package com.altaie.notifier.di

import com.altaie.notifier.firebase.FirebasePushNotifierImpl
import com.altaie.notifier.notification.IosNotifier
import com.altaie.notifier.notification.Notifier
import com.altaie.notifier.notification.PushNotifier
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import com.altaie.notifier.permission.IosPermissionUtil
import com.altaie.notifier.permission.PermissionUtil
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.UserNotifications.UNUserNotificationCenter


internal actual val platformModule = module {
    factory { Platform.Ios } bind Platform::class
    factory { IosPermissionUtil(notificationCenter = UNUserNotificationCenter.currentNotificationCenter()) } bind PermissionUtil::class
    factory {
        val configuration =
            get<NotificationPlatformConfiguration>() as NotificationPlatformConfiguration.Ios
        IosNotifier(
            permissionUtil = get(),
            notificationCenter = UNUserNotificationCenter.currentNotificationCenter(),
            iosNotificationConfiguration = configuration
        )
    } bind Notifier::class

    factory {
        FirebasePushNotifierImpl()
    } bind PushNotifier::class


}