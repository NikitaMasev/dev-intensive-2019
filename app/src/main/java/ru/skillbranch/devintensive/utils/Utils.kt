package ru.skillbranch.devintensive.utils

object Utils {
    //TODO
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

    fun transliteration(s: String, divider: String = " "): String {
        s.trim()
        var result : String

        return "nick"
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
}