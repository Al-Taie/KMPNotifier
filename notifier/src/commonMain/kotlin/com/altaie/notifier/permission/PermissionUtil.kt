package com.altaie.notifier.permission

/**
 * Permission utility class
 */
public interface PermissionUtil {


    public fun hasNotificationPermission(onPermissionResult: (isGranted: Boolean) -> Unit = {})
    public fun askNotificationPermission(onPermissionResult: (isGranted: Boolean) -> Unit = {})
}