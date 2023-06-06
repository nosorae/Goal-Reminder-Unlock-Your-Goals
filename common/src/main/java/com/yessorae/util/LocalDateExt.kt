package com.yessorae.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

fun LocalDateTime.getMonthDisplay(): String =
    this.date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

fun LocalDateTime.getWeekDisplay(): String =
    this.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

fun LocalDate.getMonthDisplay(): String =
    this.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

fun LocalDate.getWeekDisplay(): String =
    this.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

fun LocalDateTime.getWeekScopeDisplay(): String {
    val ordinal = this.date.dayOfWeek.ordinal
    val firstDayOfWeek = this.date.minus(ordinal, DateTimeUnit.DAY)
    val lastDayOfWarnings = firstDayOfWeek.plus(6, DateTimeUnit.DAY)

    return "${firstDayOfWeek.dayOfMonth}-${lastDayOfWarnings.dayOfMonth}"
}

fun LocalDateTime.Companion.now(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDate.Companion.now(): LocalDate =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

