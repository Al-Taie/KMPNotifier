package com.altaie.notifier.notification

import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import com.altaie.notifier.notification.impl.JOptionPaneNotifier
import com.altaie.notifier.notification.impl.TrayNotifier

internal object DesktopNotifierFactory {
    fun getNotifier(configuration: NotificationPlatformConfiguration.Desktop): Notifier {
        return when {
            TrayNotifier.isSupported -> TrayNotifier(configuration = configuration)
            //TODO for now return JOptionPaneNotifier for not supported platforms
            else -> JOptionPaneNotifier(configuration = configuration)
        }
    }
}