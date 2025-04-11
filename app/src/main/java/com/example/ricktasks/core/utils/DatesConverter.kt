package com.example.ricktasks.core.utils


import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatesConverter {

    private val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatter)
    }


    fun toLocalDate(date: String): LocalDate{
        return  LocalDate.parse(date, formatter)
    }

}