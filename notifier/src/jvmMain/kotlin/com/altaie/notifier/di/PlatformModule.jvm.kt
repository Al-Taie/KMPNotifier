package com.altaie.notifier.di

import com.altaie.notifier.notification.DesktopNotifierFactory
import com.altaie.notifier.notification.EmptyPushNotifierImpl
import com.altaie.notifier.notification.Notifier
import com.altaie.notifier.notification.PushNotifier
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import com.altaie.notifier.permission.EmptyPermissionUtilImpl
import com.altaie.notifier.permission.PermissionUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factory { Platform.Desktop } bind Platform::class

    factory {
        val configuration =
            get<NotificationPlatformConfiguration>() as NotificationPlatformConfiguration.Desktop
        DesktopNotifierFactory.getNotifier(configuration = configuration)
    } bind Notifier::class
    factoryOf(::EmptyPermissionUtilImpl) bind PermissionUtil::class
    factoryOf(::EmptyPushNotifierImpl) bind PushNotifier::class
}