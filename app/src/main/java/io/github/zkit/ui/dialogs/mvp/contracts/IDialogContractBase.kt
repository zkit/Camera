package io.github.zkit.ui.dialogs.mvp.contracts

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import io.github.zkit.beans.interfaces.ILifecycleCoroutineScope

internal interface IDialogContractBase {

    interface IDialogBase : ILifecycleCoroutineScope {
        val context1: Context
        val lifecycleOwner: LifecycleOwner
        suspend fun onViewCreatedBySuspend(view: View, savedInstanceState: Bundle?)
        suspend fun dismissBySuspend()
    }

    interface IPresenterBase<T : IDialogBase> {
        val dialog: T
        suspend fun onViewCreated(view: View, savedInstanceState: Bundle?, dialog: T)
    }

    interface IModelBase

}