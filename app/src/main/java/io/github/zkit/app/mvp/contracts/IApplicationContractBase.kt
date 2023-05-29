package io.github.zkit.app.mvp.contracts

import android.app.Application
import android.content.Context
import io.github.zkit.beans.interfaces.ILifecycleCoroutineScope

internal interface IApplicationContractBase {

    interface IAppBase : ILifecycleCoroutineScope {
        val context: Context
        val application: Application
    }

    interface IPresenterBase<T : IAppBase> {
        suspend fun onCreate(app: T)
    }

    interface IModelBase

}