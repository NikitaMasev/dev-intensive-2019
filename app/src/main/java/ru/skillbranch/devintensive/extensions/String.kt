package ru.skillbranch.devintensive.extensions

fun String.truncate(value: Int = 16): String {
    val payload = "..."
    val newStr = this.trim()

    return when {
        newStr.length > value -> newStr.substring(0, value).trim().plus(payload)
        else -> newStr
    }
}

fun String.stripHtml(): String = this
    .replace("<[^>]*>".toRegex(),"")
    .replace("[ ]+".toRegex()," ")


