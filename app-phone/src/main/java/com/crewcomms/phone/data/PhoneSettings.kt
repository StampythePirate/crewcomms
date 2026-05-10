package com.crewcomms.phone.data

data class PhoneSettings(
    val displayName: String = "Captain",
    val vibrationRelayToWatch: Boolean = true,
    val keepScreenAwake: Boolean = false,
    val autoReconnect: Boolean = true,
    val useMockTransport: Boolean = true,
)
