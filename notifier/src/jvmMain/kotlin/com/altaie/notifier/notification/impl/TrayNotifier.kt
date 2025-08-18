package com.altaie.notifier.notification.impl

import com.altaie.notifier.logger.currentLogger
import com.altaie.notifier.notification.Notifier
import com.altaie.notifier.notification.NotifierBuilder
import com.altaie.notifier.notification.configuration.NotificationPlatformConfiguration
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import kotlin.random.Random

internal class TrayNotifier(private val configuration: NotificationPlatformConfiguration.Desktop) :
    Notifier {

    private val trayIcons: MutableMap<Int, TrayIcon> = mutableMapOf()

    companion object {
        val isSupported by lazy {
            SystemTray.isSupported().also {
                if (it.not()) {
                    currentLogger.log("Tray is not supported on the current platform.")
                }
            }
        }
    }

    override fun notify(title: String, body: String, payloadData: Map<String, String>): Int {
        if (isSupported.not()) return -1
        val notificationID = Random.nextInt(0, Int.MAX_VALUE)
        notify {
            this.id = notificationID
            this.title = title
            this.body = body
            this.payloadData = payloadData
        }
        return notificationID
    }

    override fun notify(
        id: Int,
        title: String,
        body: String,
        payloadData: Map<String, String>
    ) {
        notify {
            this.id = id
            this.title = title
            this.body = body
            this.payloadData = payloadData
        }
    }

    override fun notify(block: NotifierBuilder.() -> Unit) {
        val builder = NotifierBuilder().apply(block)
        if (isSupported.not()) return
        val icon = Toolkit.getDefaultToolkit().getImage(configuration.notificationIconPath)
        val trayIcon = TrayIcon(icon).apply {
            isImageAutoSize = true
        }
        SystemTray.getSystemTray().add(trayIcon)
            .also { trayIcons[builder.id] = trayIcon }
        trayIcon.displayMessage(builder.title, builder.body, TrayIcon.MessageType.INFO)
    }

    override fun remove(id: Int) {
        val systemTray = SystemTray.getSystemTray()
        val trayIcon = trayIcons.getOrDefault(id, null)
        trayIcon?.let { systemTray.remove(it) }
    }

    override fun removeAll() {
        val systemTray = SystemTray.getSystemTray()
        systemTray.trayIcons.forEach { systemTray.remove(it) }
    }
}
