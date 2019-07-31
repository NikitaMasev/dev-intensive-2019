package ru.skillbranch.devintensive.utils

import android.content.res.Resources
import androidx.annotation.Px
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.trim()?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return when {
            firstName == "" -> Pair(null, lastName)
            lastName == "" -> Pair(firstName, null)
            else -> Pair(firstName, lastName)
        }
    }

    fun transliteration(source: String, divider: String = " "): String {
        source.trim()
        var result = ""

        for (currentSymbol in source) {
            result += if (currentSymbol.toString() == " ") divider else translateToEng(currentSymbol)
        }

        return result
    }

    private fun translateToEng(currentSymbol: Char): String {
        val rusToEng = hashMapOf(
            "а" to "a",
            "б" to "b",
            "в" to "v",
            "г" to "g",
            "д" to "d",
            "е" to "e",
            "ё" to "e",
            "ж" to "zh",
            "з" to "z",
            "и" to "i",
            "й" to "i",
            "к" to "k",
            "л" to "l",
            "м" to "m",
            "н" to "n",
            "о" to "o",
            "п" to "p",
            "р" to "r",
            "с" to "s",
            "т" to "t",
            "у" to "u",
            "ф" to "f",
            "х" to "h",
            "ц" to "c",
            "ч" to "ch",
            "ш" to "sh",
            "щ" to "sh'",
            "ъ" to "",
            "ы" to "i",
            "ь" to "",
            "э" to "e",
            "ю" to "yu",
            "я" to "ya"
        )

        return when {
            currentSymbol.isLowerCase() -> rusToEng.getOrElse(currentSymbol.toString()) { currentSymbol.toString() }
            currentSymbol.isUpperCase() -> {
                val eng = rusToEng.getOrElse(currentSymbol.toString().toLowerCase()) { currentSymbol.toString() }
                    .toUpperCase()
                return if (eng.length > 1)
                    eng.toCharArray()[0] + eng.toCharArray()[1].toLowerCase().toString()
                else
                    eng
            }
            else -> ""
        }
    }


    fun toInitials(firstName: String?, lastName: String?): String? {
        val copyFirstName = firstName?.trim()
        val copyLastName = lastName?.trim()

        if (copyFirstName.isNullOrEmpty() && copyLastName.isNullOrEmpty()) {
            return null
        }

        return when {
            copyFirstName.isNullOrEmpty() -> copyLastName?.get(0).toString().toUpperCase()
            copyLastName.isNullOrEmpty() -> copyFirstName?.get(0).toString().toUpperCase()
            else -> "${copyFirstName[0].toUpperCase()}${copyLastName[0].toUpperCase()}"
        }
    }

    fun getDifferNumberDate(currentDate: Date, date: Date, pattern: String): Int {
        return currentDate.format(pattern).toInt() - date.format(pattern).toInt()
    }

    fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

    fun spToPx(sp: Int) = (sp * Resources.getSystem().displayMetrics.scaledDensity).toInt()

    fun pxToDp(px: Int) = (px / Resources.getSystem().displayMetrics.density).toInt()

}