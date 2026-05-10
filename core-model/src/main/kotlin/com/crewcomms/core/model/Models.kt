package com.crewcomms.core.model

import kotlinx.serialization.Serializable

@Serializable
data class CrewRoom(
    val id: String,
    val name: String,
    val hostName: String,
    val hostEndpointId: String? = null,
    val pinRequired: Boolean = false,
    val pinCode: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)

@Serializable
data class CrewMember(
    val id: String,
    val displayName: String,
    val endpointId: String? = null,
    val role: DeviceRole,
    val lastSeenAt: Long = System.currentTimeMillis(),
)

@Serializable
data class CrewMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val body: String,
    val type: MessageType,
    val quickCommand: QuickCommand? = null,
    val timestamp: Long = System.currentTimeMillis(),
)

@Serializable
enum class MessageType {
    TEXT,
    QUICK_COMMAND,
    SYSTEM,
    VOICE_NOTE_PLACEHOLDER,
}

@Serializable
enum class ConnectionStatus {
    DISCONNECTED,
    DISCOVERING,
    ADVERTISING,
    CONNECTING,
    CONNECTED,
    SYNCING,
    ERROR,
}

@Serializable
enum class QuickCommand {
    PING,
    HELP,
    READY,
    COME_HERE,
    WAIT,
}

@Serializable
enum class DeviceRole {
    CAPTAIN,
    CREW,
    WATCH,
}

@Serializable
data class NearbyCrew(
    val endpointId: String,
    val roomId: String,
    val roomName: String,
    val hostName: String,
    val pinRequired: Boolean,
    val signalStrengthHint: Int? = null,
)

@Serializable
sealed class TransportEvent {
    @Serializable
    data class CrewDiscovered(val crew: NearbyCrew) : TransportEvent()

    @Serializable
    data class CrewLost(val endpointId: String) : TransportEvent()

    @Serializable
    data class MessageReceived(val message: CrewMessage) : TransportEvent()

    @Serializable
    data class MemberJoined(val member: CrewMember) : TransportEvent()

    @Serializable
    data class MemberLeft(val memberId: String) : TransportEvent()

    @Serializable
    data class ConnectionError(val reason: String) : TransportEvent()
}

@Serializable
data class WatchCommand(
    val commandId: String,
    val roomId: String?,
    val command: QuickCommand,
    val senderDeviceName: String,
    val timestamp: Long = System.currentTimeMillis(),
)

@Serializable
data class WatchMessagePreview(
    val senderName: String,
    val body: String,
    val timestamp: Long,
)

@Serializable
data class WatchState(
    val roomId: String? = null,
    val roomName: String? = null,
    val connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val lastMessages: List<WatchMessagePreview> = emptyList(),
    val memberCount: Int = 0,
    val lastEventTimestamp: Long = System.currentTimeMillis(),
)
