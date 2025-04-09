package com.example.ricktasks.data.model

import java.time.LocalDate

data class Task(
    val id: Long,
    var title: String,
    var description: String,
    var date: LocalDate?,
    var isCompleted: Boolean
)
