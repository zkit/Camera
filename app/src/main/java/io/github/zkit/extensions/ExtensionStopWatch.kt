package io.github.zkit.extensions

import com.google.common.base.Stopwatch
import java.util.Locale
import java.util.concurrent.TimeUnit

internal fun Stopwatch.toStringSeconds(): String {
    val appStartElapsedTimeNano: Long = this.elapsed(TimeUnit.NANOSECONDS)
    val appStartElapsedTime: Double = appStartElapsedTimeNano.toDouble() / TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS).toDouble()
    return String.format(Locale.ENGLISH, "%.1f s", appStartElapsedTime)
}

internal fun Stopwatch.toStringMinutes(): String {
    val appStartElapsedTimeNano: Long = this.elapsed(TimeUnit.NANOSECONDS)
    val appStartElapsedTime: Double = appStartElapsedTimeNano.toDouble() / TimeUnit.NANOSECONDS.convert(1, TimeUnit.MINUTES).toDouble()
    return String.format(Locale.ENGLISH, "%.1f m", appStartElapsedTime)
}

