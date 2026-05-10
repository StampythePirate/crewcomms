package com.crewcomms.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crewcomms.core.database.dao.CrewDao
import com.crewcomms.core.database.entity.MemberEntity
import com.crewcomms.core.database.entity.MessageEntity

@Database(
    entities = [MessageEntity::class, MemberEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class CrewDatabase : RoomDatabase() {
    abstract fun crewDao(): CrewDao
}
