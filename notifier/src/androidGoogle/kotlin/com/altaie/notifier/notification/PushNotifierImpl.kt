package com.altaie.notifier.notification

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.altaie.notifier.logger.currentLogger
import com.altaie.notifier.logger.log


internal class PushNotifierImpl : PushNotifier() {

    init {
        currentLogger.log("Google PushNotifier is initialized")
    }

    private val messaging by lazy { FirebaseMessaging.getInstance() }

    override suspend fun getToken(): String? = messaging.token.await(
        onSuccess = { currentLogger.log("Token retrieved successfully -> $it") },
        onFailure = { currentLogger.log("Error while getting token: $it") }
    ).getOrNull()

    override suspend fun deleteMyToken(): Boolean = callSafe(
        block = { messaging.deleteToken() },
        onSuccess = { currentLogger.log("Token deleted successfully") },
        onFailure = { currentLogger.log("Error while deleting token: $it") },
    ).isSuccess

    override suspend fun subscribeToTopic(topic: String): Boolean = callSafe(
        block = { messaging.subscribeToTopic(topic) },
        onSuccess = { currentLogger.log("Subscribed to topic($topic) successfully") },
        onFailure = { currentLogger.log("Error while subscribing to topic $topic: $it") },
    ).isSuccess

    override suspend fun unSubscribeFromTopic(topic: String): Boolean = callSafe(
        block = { messaging.unsubscribeFromTopic(topic) },
        onSuccess = { currentLogger.log("Unsubscribed from topic($topic) successfully") },
        onFailure = { currentLogger.log("Error while unsubscribing from topic $topic: $it") },
    ).isSuccess

    private suspend fun <T> Task<T>.await(
        onSuccess: (T) -> Unit = {},
        onFailure: (Throwable) -> Unit = currentLogger::log
    ) = callSafe(onSuccess = onSuccess, onFailure = onFailure) { Tasks.await<T>(this) }
}
