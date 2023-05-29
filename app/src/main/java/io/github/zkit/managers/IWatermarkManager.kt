package io.github.zkit.managers

import android.content.Context
import android.view.ViewGroup
import io.github.zkit.beans.BeanWatermarkConfig
import java.io.File

internal interface IWatermarkManager {

    val config: HashMap<String, BeanWatermarkConfig>

    suspend fun load(path: File)

    suspend fun layout(context: Context): ViewGroup

    suspend fun save()
}