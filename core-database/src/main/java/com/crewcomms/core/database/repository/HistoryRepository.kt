package com.crewcomms.core.database.repository

import com.crewcomms.core.database.dao.CrewDao
import com.crewcomms.core.database.entity.toEntity
import com.crewcomms.core.database.entity.toModel
import com.crewcomms.core.model.CrewMember
import com.crewcomms.core.model.CrewMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val crewDao: CrewDao,
) {
    fun observeMessages(roomId: String): Flow<List<CrewMessage>> =
        crewDao.observeMessages(roomId).map { entities -> entities.map { it.toModel() } }

    fun observeMembers(): Flow<List<CrewMember>> =
        crewDao.observeMembers().map { entities -> entities.map { it.toModel() } }

    suspend fun saveMessage(message: CrewMessage) {
        crewDao.upsertMessage(message.toEntity())
    }

    suspend fun saveMember(member: CrewMember) {
        crewDao.upsertMember(member.toEntity())
    }

    suspend fun clearAll() {
        crewDao.clearMembers()
        crewDao.clearMessages()
    }
}
