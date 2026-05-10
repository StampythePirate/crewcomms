package com.crewcomms.core.transport

import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.CrewRoom
import kotlinx.serialization.Serializable

@Serializable
internal sealed class TransportPayload {
    @Serializable
    data class RoomAnnouncement(
        val room: CrewRoom,
        val sourceId: String,
    ) : TransportPayload()

    @Serializable
    data class CrewMessagePayload(
        val message: CrewMessage,
        val sourceId: String,
    ) : TransportPayload()
}
