package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

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
    DAY
}

fun Date.humanizeDiff(date:Date = Date()): String {
    val differYears = Utils.getDifferNumberDate(date,this,"yyyy")
    val differDays = Utils.getDifferNumberDate(date,this,"dd")
    val differHours = Utils.getDifferNumberDate(date,this,"HH")
    val differMinutes = Utils.getDifferNumberDate(date,this,"mm")
    val differSeconds = Utils.getDifferNumberDate(date,this,"ss")

    return when {
        differYears > 0 -> "более года назад"
        differDays > 0 -> "$differDays дней назад"
        else -> "hui"
    }

}