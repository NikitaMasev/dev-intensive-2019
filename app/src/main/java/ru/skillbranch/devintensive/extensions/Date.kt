package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time

    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value: Int): String {
        return when (this) {
            SECOND -> pluralSeconds(value)
            MINUTE -> pluralMinutes(value)
            HOUR -> pluralHours(value)
            else -> pluralDays(value)
        }
    }
}

private fun pluralDays(value: Int): String {
    return when {
        isSpecialDiapason(value.toLong()) -> "$value дней"
        isTwoToFourDiapason(value.toLong()) -> "$value дня"
        isOne(value.toLong()) -> "$value день"
        else -> "$value дней"
    }
}

private fun pluralHours(value: Int): String {
    return when {
        isSpecialDiapason(value.toLong()) -> "$value часов"
        isTwoToFourDiapason(value.toLong()) -> "$value часа"
        isOne(value.toLong()) -> "$value час"
        else -> "$value часов"
    }
}

private fun pluralMinutes(value: Int): String {
    return when {
        isSpecialDiapason(value.toLong()) -> "$value минут"
        isTwoToFourDiapason(value.toLong()) -> "$value минуты"
        isOne(value.toLong()) -> "$value минуту"
        else -> "$value минут"
    }
}

private fun pluralSeconds(value: Int): String {
    return when {
        isSpecialDiapason(value.toLong()) -> "$value секунд"
        isTwoToFourDiapason(value.toLong()) -> "$value секунды"
        isOne(value.toLong()) -> "$value секунду"
        else -> "$value секунд"
    }
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val differ = date.time - this.time

    return when {
        differ > 0 -> handlePast(differ)
        else -> handleFuture(differ.absoluteValue)
    }
}

private fun handlePast(differ: Long): String {
    return when {
        differ / DAY >= 1L || differ / HOUR > 22L -> handleDays(differ, DAY)
        differ / HOUR >= 1L || differ / MINUTE > 45L -> handleHours(differ, HOUR)
        differ / MINUTE >= 1L || differ / SECOND > 45L -> handleMinutes(differ, MINUTE)
        else -> handleSeconds(differ, SECOND)
    }
}

private fun handleFuture(differ: Long): String {
    return when {
        differ / DAY >= 1L || differ / HOUR > 22L -> {
            val res = handleDays(differ, DAY)
            return if (res.contains("более")) {
                "более чем через год"
            } else {
                "через ${res.replace("назад", "").trim()}"
            }
        }
        differ / HOUR >= 1L || differ / MINUTE > 45L -> {
            val res = handleHours(differ, HOUR)
            return "через ${res.replace("назад", "").trim()}"
        }
        differ / MINUTE >= 1L || differ / SECOND > 45L -> {
            val res = handleMinutes(differ, MINUTE)
            return "через ${res.replace("назад", "").trim()}"
        }
        else -> {
            val res = handleSeconds(differ, SECOND)
            return if (res.contains("только что")) {
                res
            } else {
                return "через ${res.replace("назад", "").trim()}"
            }
        }
    }
}

private fun handleSeconds(differ: Long, timeUnit: Long): String {
    return when {
        differ / timeUnit > 1L -> "несколько секунд назад"
        else -> "только что"
    }
}


private fun handleMinutes(differ: Long, timeUnit: Long): String {
    if (differ / SECOND < 75L) {
        return "минуту назад"
    }

    val minutes = differ / timeUnit

    return when {
        isSpecialDiapason(minutes) -> "$minutes минут назад"
        isTwoToFourDiapason(minutes) -> "$minutes минуты назад"
        isOne(minutes) -> "$minutes минуту назад"
        else -> "$minutes минут назад"
    }
}

private fun handleHours(differ: Long, timeUnit: Long): String {
    if (differ / MINUTE < 75L) {
        return "час назад"
    }

    val hours = differ / timeUnit

    return when {
        isSpecialDiapason(hours) -> "$hours часов назад"
        isTwoToFourDiapason(hours) -> "$hours часа назад"
        isOne(hours) -> "$hours час назад"
        else -> "$hours часов назад"
    }
}

private fun handleDays(differ: Long, timeUnit: Long): String {
    if (differ / HOUR < 27L) {
        return "день назад"
    }

    val days = differ / timeUnit

    return when {
        days >= 361L -> "более года назад"
        isSpecialDiapason(days) -> "$days дней назад"
        isTwoToFourDiapason(days) -> "$days дня назад"
        isOne(days) -> "$days день назад"
        else -> "$days дней назад"
    }
}

private fun isOne(days: Long): Boolean {
    val strDays = "$days"
    val lastSymbol = strDays[strDays.length - 1].toString().toInt()

    return lastSymbol == 1
}

private fun isTwoToFourDiapason(days: Long): Boolean {
    val strDays = "$days"
    val lastIndex = strDays.length - 1
    val lastSymbol = strDays[lastIndex].toString().toInt()

    return (lastSymbol.coerceIn(2, 4) == lastSymbol)
}

private fun isSpecialDiapason(days: Long): Boolean {
    if (days < 10L) {
        return false
    }

    val strDays = "$days"
    val lastIndex = strDays.length - 1
    val firstSymbol = strDays[lastIndex - 1]
    val secondSymbol = strDays[lastIndex]
    val newSymbol = "$firstSymbol$secondSymbol".toInt()
    return (newSymbol.coerceIn(10, 20) == newSymbol)
}

