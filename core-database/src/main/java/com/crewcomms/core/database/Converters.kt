package com.crewcomms.core.database

import androidx.room.TypeConverter
import com.crewcomms.core.model.MessageType
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.core.model.DeviceRole

class Converters {
    @TypeConverter
    fun toMessageType(value: String): MessageType = MessageType.valueOf(value)

    @TypeConverter
    fun fromMessageType(value: MessageType): String = value.name

    @TypeConverter
    fun toQuickCommand(value: String?): QuickCommand? = value?.let { QuickCommand.valueOf(it) }

    @TypeConverter
    fun fromQuickCommand(value: QuickCommand?): String? = value?.name

    @TypeConverter
    fun toDeviceRole(value: String): DeviceRole = DeviceRole.valueOf(value)

    @TypeConverter
    fun fromDeviceRole(value: DeviceRole): String = value.name
}
