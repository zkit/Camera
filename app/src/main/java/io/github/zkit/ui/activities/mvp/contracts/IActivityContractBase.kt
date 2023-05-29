package io.github.zkit.ui.activities.mvp.contracts

import android.content.Context
import android.os.Bundle
import io.github.zkit.beans.interfaces.ILifecycleCoroutineScope

internal interface IActivityContractBase {

    interface IActivityBase : ILifecycleCoroutineScope {
        val context: Context
    }

    interface IPresenterBase<T : IActivityBase> {
        suspend fun onCreate(savedInstanceState: Bundle?, activity: T)
    }

    interface IModelBase

}