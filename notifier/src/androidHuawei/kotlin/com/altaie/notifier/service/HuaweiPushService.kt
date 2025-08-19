package com.altaie.notifier.service

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.altaie.notifier.logger.currentLogger
import com.altaie.notifier.notification.NotificationHandler
import com.altaie.notifier.notification.NotifierManagerImpl
import com.altaie.notifier.notification.PushNotifierImpl


internal class HuaweiPushService : HmsMessageService() {
    private val notifierManager by lazy { NotifierManagerImpl }
    private val notifier by lazy { notifierManager.getLocalNotifier() }
    private val handler by lazy { NotificationHandler(notifierManager, notifier) }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        currentLogger.log("HuaweiPushService: onNewToken is called")
        token?.let { notifierManager.onNewToken(it) }
        PushNotifierImpl.token = token
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        currentLogger.log("HuaweiPushService: onMessageReceived is called with message: $message")
        handler.handle(
            title = message?.notification?.title,
            body = message?.notification?.body,
            data = message?.dataOfMap.orEmpty()
        )
    }
}
