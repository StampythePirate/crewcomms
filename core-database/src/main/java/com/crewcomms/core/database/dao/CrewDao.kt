package com.crewcomms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crewcomms.core.database.entity.MemberEntity
import com.crewcomms.core.database.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CrewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMember(member: MemberEntity)

    @Query("SELECT * FROM messages WHERE roomId = :roomId ORDER BY timestamp DESC LIMIT :limit")
    fun observeMessages(roomId: String, limit: Int = 200): Flow<List<MessageEntity>>

    @Query("SELECT * FROM members ORDER BY displayName ASC")
    fun observeMembers(): Flow<List<MemberEntity>>

    @Query("DELETE FROM messages")
    suspend fun clearMessages()

    @Query("DELETE FROM members")
    suspend fun clearMembers()
}
