package com.crewcomms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crewcomms.core.model.CrewMessage
import com.crewcomms.core.model.MessageType
import com.crewcomms.core.model.QuickCommand

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val body: String,
    val type: MessageType,
    val quickCommand: QuickCommand?,
    val timestamp: Long,
)

fun MessageEntity.toModel(): CrewMessage = CrewMessage(
    id = id,
    roomId = roomId,
    senderId = senderId,
    senderName = senderName,
    body = body,
    type = type,
    quickCommand = quickCommand,
    timestamp = timestamp,
)

fun CrewMessage.toEntity(): MessageEntity = MessageEntity(
    id = id,
    roomId = roomId,
    senderId = senderId,
    senderName = senderName,
    body = body,
    type = type,
    quickCommand = quickCommand,
    timestamp = timestamp,
)
