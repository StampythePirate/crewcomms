package com.crewcomms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crewcomms.core.model.CrewMember
import com.crewcomms.core.model.DeviceRole

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val endpointId: String?,
    val role: DeviceRole,
    val lastSeenAt: Long,
)

fun MemberEntity.toModel(): CrewMember = CrewMember(
    id = id,
    displayName = displayName,
    endpointId = endpointId,
    role = role,
    lastSeenAt = lastSeenAt,
)

fun CrewMember.toEntity(): MemberEntity = MemberEntity(
    id = id,
    displayName = displayName,
    endpointId = endpointId,
    role = role,
    lastSeenAt = lastSeenAt,
)
