package com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getResourceColor(@ColorRes colorID: Int)
    = ContextCompat.getColor(this, colorID)