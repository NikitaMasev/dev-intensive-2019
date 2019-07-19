package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.graphics.Rect
import android.view.View


fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean {
    val view = findViewById<View>(android.R.id.content)
    val rect = Rect()
    view.getWindowVisibleDisplayFrame(rect)
    return view.height > rect.height()
}

fun Activity.isKeyboardClosed() = !isKeyboardOpen()

