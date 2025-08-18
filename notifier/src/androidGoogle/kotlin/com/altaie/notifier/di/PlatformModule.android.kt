package com.altaie.notifier.di

import com.altaie.notifier.notification.PushNotifier
import com.altaie.notifier.notification.PushNotifierImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module


internal actual val platformModule = module {
    commonPlatformModule()
    factoryOf(::PushNotifierImpl) bind PushNotifier::class
}
