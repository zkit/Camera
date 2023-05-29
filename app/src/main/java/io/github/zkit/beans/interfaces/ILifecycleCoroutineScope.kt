package io.github.zkit.beans.interfaces

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineExceptionHandler

internal interface ILifecycleCoroutineScope {

    val coroutineExceptionHandler: CoroutineExceptionHandler
    val lifecycleCoroutineScope: LifecycleCoroutineScope

}