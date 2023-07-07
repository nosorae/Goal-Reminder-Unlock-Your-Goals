package com.yessorae.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.text.NumberFormat
import java.time.format.TextStyle
import java.util.Locale

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.toMilliSecond(): Long {
    return this.toInstant(TimeZone.currentSystemDefault()).epochSeconds * 1000
}

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

fun LocalDate.getWeekRange(): IntRange {
    val ordinal = this.dayOfWeek.ordinal
    val firstDayOfWeek = this.minus(ordinal, DateTimeUnit.DAY)
    val lastDayOfWarnings = firstDayOfWeek.plus(6, DateTimeUnit.DAY)
    return firstDayOfWeek.dayOfMonth..lastDayOfWarnings.dayOfMonth
}

fun LocalDate.getWeekRangePair(): Pair<LocalDate, LocalDate> {
    val ordinal = this.dayOfWeek.ordinal
    val firstDayOfWeek = this.minus(ordinal, DateTimeUnit.DAY)
    val lastDayOfWarnings = firstDayOfWeek.plus(6, DateTimeUnit.DAY)
    return firstDayOfWeek to lastDayOfWarnings
}
fun LocalDateTime.Companion.now(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDate.Companion.now(): LocalDate =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalTime.Companion.getStartOfDay(): LocalTime {
    return fromSecondOfDay(0)
}

fun LocalDate.toStartLocalDateTime(): LocalDateTime {
    return atStartOfDayIn(TimeZone.currentSystemDefault()).toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDate.toEndLocalDateTime(): LocalDateTime {
    return atTime(hour = 23, minute = 59, second = 59)
}
fun LocalTime.Companion.fromHourMinute(hour: Int, minute: Int, second: Int = 0): LocalTime {
    return fromSecondOfDay((hour * 60 * 60) + (minute * 60) + second)
}

fun LocalDate.toLocalDateTime(hour: Int? = null, minute: Int? = null, second: Int? = null): LocalDateTime {
    return this.atTime(LocalTime.fromHourMinute(hour = hour ?: 0, minute = minute ?: 0, second = second ?: 0))
}

fun Int.toLocalString(): String {
    val formatter = NumberFormat.getIntegerInstance()
    formatter.isGroupingUsed = false
    return formatter.format(this)
}