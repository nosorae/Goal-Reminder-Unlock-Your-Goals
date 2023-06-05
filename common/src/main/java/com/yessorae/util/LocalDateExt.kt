package com.yessorae.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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

