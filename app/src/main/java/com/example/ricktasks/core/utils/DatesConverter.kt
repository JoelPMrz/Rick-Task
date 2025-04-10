package com.example.ricktasks.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatesConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverters
    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverters
    fun toLocalDate(date: String): LocalDate{
        return  LocalDate.parse(date, formatter)
    }

}