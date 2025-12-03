package com.example.nanopost.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun dateTimeFormatter(seconds: Long): String {
    val instant = Instant.fromEpochSeconds(seconds)
    val time = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return time.format(formatter)
}

@OptIn(FormatStringsInDatetimeFormats::class)
var formatter = LocalDateTime.Format {
    byUnicodePattern("dd.MM.yyyy HH:mm")
}