package com.challenge.randomuser.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun formatRegisteredDate(isoDate: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoDate)
        zonedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: Exception) {
        isoDate
    }
}