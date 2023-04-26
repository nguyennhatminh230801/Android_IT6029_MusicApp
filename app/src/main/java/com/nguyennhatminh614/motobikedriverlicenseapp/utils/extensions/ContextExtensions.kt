package com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

val isCurrentDarkModeByDefault : Boolean
    get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

val isCurrentLightModeByDefault : Boolean
    get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO

fun Context.getResourceColor(@ColorRes colorID: Int)
    = ContextCompat.getColor(this, colorID)

fun getColorWithAlpha(yourColor: Int, alpha: Int): Int {
    val red: Int = Color.red(yourColor)
    val blue: Int = Color.blue(yourColor)
    val green: Int = Color.green(yourColor)
    return Color.argb(alpha, red, green, blue)
}