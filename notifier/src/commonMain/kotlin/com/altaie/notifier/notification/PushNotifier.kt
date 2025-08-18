package com.altaie.notifier.notification

import com.altaie.notifier.logger.currentLogger
import com.altaie.notifier.logger.log

public typealias PayloadData = Map<String, *>

/**
 * Class represents push notification such as Firebase Push Notification
 */
public abstract class PushNotifier {

    /**
     * @return current push notification token
     */
    public abstract suspend fun getToken(): String?

    /**
     * Deletes user push notification. For log out cases for example
     */
    public abstract suspend fun deleteMyToken(): Boolean

    /**
     * Subscribing user to group.
     * @param topic  Topic name
     */
    public abstract suspend fun subscribeToTopic(topic: String): Boolean

    /**
     * Unsubscribe user from group.
     * @param topic  Topic name
     */
    public abstract suspend fun unSubscribeFromTopic(topic: String): Boolean

    protected fun <T> callSafe(
        onSuccess: (T) -> Unit = {},
        onFailure: (Throwable) -> Unit = currentLogger::log,
        block: () -> T,
    ): Result<T> = runCatching {
        block()
    }.onSuccess(onSuccess)
        .onFailure(onFailure)
}
