package com.crewcomms.core.common

object PermissionSets {
    val phoneRuntimePermissions = listOf(
        "android.permission.BLUETOOTH_SCAN",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.BLUETOOTH_ADVERTISE",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.NEARBY_WIFI_DEVICES",
        "android.permission.POST_NOTIFICATIONS",
        "android.permission.RECORD_AUDIO",
    )

    val watchRuntimePermissions = listOf(
        "android.permission.VIBRATE",
    )
}
