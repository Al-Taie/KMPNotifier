package com.altaie.notifier.sample

import com.altaie.notifier.notification.NotifierManager
import com.altaie.notifier.notification.PayloadData


object AppInitializer {
    fun onApplicationStart() {
        onApplicationStartPlatformSpecific()
        NotifierManager.setLogger { message ->
            println("DEBUGGING -> Notifier: $message")
        }
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                println("DEBUGGING -> Push Notification onNewToken: $token")
            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                println("DEBUGGING -> Push Notification notification type message is received: Title: $title and Body: $body")
            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                println("DEBUGGING -> Push Notification payloadData: $data")
            }

            override fun onPushNotificationWithPayloadData(
                title: String?,
                body: String?,
                data: PayloadData
            ) {
                super.onPushNotificationWithPayloadData(title, body, data)
                println("DEBUGGING -> Push Notification is received: Title: $title and Body: $body and Notification payloadData: $data")
            }

            override fun onNotificationClicked(data: PayloadData) {
                println("DEBUGGING -> Notification clicked, Notification payloadData: $data")
            }
        })
    }
}
