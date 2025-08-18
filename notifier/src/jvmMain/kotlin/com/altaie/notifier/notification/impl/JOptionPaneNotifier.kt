package com.altaie.notifier.notification.impl

import com.altaie.notifier.logger.currentLogger
import com.altaie.notifier.notification.Notifier
import com.altaie.notifier.notification.NotifierBuilder
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import javax.swing.ImageIcon
import javax.swing.JOptionPane

internal class JOptionPaneNotifier(private val configuration: NotificationPlatformConfiguration.Desktop) :
    Notifier {

    override fun notify(title: String, body: String, payloadData: Map<String, String>): Int {
        val id = -1
        notify {
            this.id = id
            this.title = title
            this.body = body
            this.payloadData = payloadData
        }
        return id
    }

    override fun notify(id: Int, title: String, body: String, payloadData: Map<String, String>) {
        notify {
            this.id = id
            this.title = title
            this.body = body
            this.payloadData = payloadData
        }
    }

    override fun notify(block: NotifierBuilder.() -> Unit) {
        val builder = NotifierBuilder().apply(block)
        JOptionPane.showMessageDialog(
            null,
            builder.body,
            builder.title,
            JOptionPane.INFORMATION_MESSAGE,
            ImageIcon(configuration.notificationIconPath)
        )
    }

    override fun remove(id: Int) {
        currentLogger.log("No remove functionality")
    }

    override fun removeAll() {
        currentLogger.log("No removeAll functionality")
    }
}
