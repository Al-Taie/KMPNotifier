package com.altaie.notifier.di

import android.content.Context
import androidx.startup.Initializer
import com.altaie.notifier.notification.AndroidNotifier
import com.altaie.notifier.notification.NotificationChannelFactory
import com.altaie.notifier.notification.Notifier
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import com.altaie.notifier.permission.AndroidMockPermissionUtil
import com.altaie.notifier.permission.PermissionUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

internal lateinit var applicationContext: Context
    private set

internal class ContextInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        applicationContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}

internal fun Module.commonPlatformModule() {
    factory { Platform.Android } bind Platform::class
    single { applicationContext }
    factoryOf(::AndroidMockPermissionUtil) bind PermissionUtil::class
    factory {
        val configuration =
            get<NotificationPlatformConfiguration>() as NotificationPlatformConfiguration.Android
        AndroidNotifier(
            context = get(),
            androidNotificationConfiguration = configuration,
            notificationChannelFactory = NotificationChannelFactory(
                context = get(),
                channelData = configuration.notificationChannelData
            ),
            permissionUtil = get()
        )
    } bind Notifier::class
}
