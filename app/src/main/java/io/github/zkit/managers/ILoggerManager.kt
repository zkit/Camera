package io.github.zkit.managers

import android.net.Uri

internal interface ILoggerManager {

    suspend fun init()

    suspend fun zip(): Uri
}