package com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions

import java.util.concurrent.TimeUnit

const val PATTERN_TIME = "%02d:%02d"
const val PATTERN_DETAIL_TIME = "%02dp%02ds"

fun Long.convertSecondToMilisecond(): Long {
    return this * 1000;
}

fun Long.toDateTimeMMSS(): String {
    return String.format(
        PATTERN_TIME,
        TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.HOURS.toMinutes(1)
    )
}

fun Long.toDateTimeDetail(): String {
    return String.format(
        PATTERN_DETAIL_TIME,
        TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.HOURS.toMinutes(1)
    )
}