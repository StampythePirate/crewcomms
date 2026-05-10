package com.crewcomms.core.transport

import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.CrewRoom
import com.crewcomms.core.model.TransportEvent
import kotlinx.coroutines.flow.Flow

interface CrewTransport {
    val events: Flow<TransportEvent>
    val connectionStatus: Flow<ConnectionStatus>

    suspend fun startAdvertising(room: CrewRoom)
    suspend fun startDiscovery()
    suspend fun stop()
    suspend fun connectToCrew(endpointId: String, pin: String? = null)
    suspend fun sendMessage(message: CrewMessage)
}
