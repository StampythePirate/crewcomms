package com.crewcomms.core.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeFormatter {
    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun formatTime(timestamp: Long): String = formatter.format(Date(timestamp))
}
