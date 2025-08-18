package com.altaie.notifier.di

import com.altaie.notifier.notification.EmptyPushNotifierImpl
import com.altaie.notifier.notification.Notifier
import com.altaie.notifier.notification.PushNotifier
import com.altaie.notifier.notification.WebConsoleNotifier
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import com.altaie.notifier.permission.PermissionUtil
import com.altaie.notifier.permission.WebPermissionUtilImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module


internal actual val platformModule = module {
    factory { Platform.Web } bind Platform::class
    factoryOf(::WebPermissionUtilImpl) bind PermissionUtil::class
    factory {
        val configuration =
            get<NotificationPlatformConfiguration>() as NotificationPlatformConfiguration.Web
        WebConsoleNotifier(configuration = configuration, permissionUtil = get())
    } bind Notifier::class
    factoryOf(::EmptyPushNotifierImpl) bind PushNotifier::class
}