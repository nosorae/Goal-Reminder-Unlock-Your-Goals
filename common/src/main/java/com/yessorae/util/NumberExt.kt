package com.yessorae.util

fun Double.truncateToOneDecimalPlace(): Double {
    val multiplier = 10.0
    return kotlin.math.round(this * multiplier) / multiplier
}

