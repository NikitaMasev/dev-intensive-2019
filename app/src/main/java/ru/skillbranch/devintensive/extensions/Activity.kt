package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.graphics.Rect
import android.view.ViewGroup




fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.isKeyboardOpen(vg : ViewGroup): Boolean {
    val rect = Rect()
    vg.getWindowVisibleDisplayFrame(rect)
    val heightDiff = vg.rootView.height - (rect.bottom - rect.top)

    return heightDiff > 300
}

fun Activity.isKeyboardClosed(vg : ViewGroup) = !isKeyboardOpen(vg)

