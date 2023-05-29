package io.github.zkit.ui.fragments.mvp.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.github.zkit.beans.interfaces.ILifecycleCoroutineScope
import io.github.zkit.ui.beans.annotations.FragmentResultCode

internal interface IFragmentContractBase {

    interface IFragmentBase : ILifecycleCoroutineScope {
        val context1: Context
        val activity1: QMUIFragmentActivity
        val manager: FragmentManager
        val lifecycleOwner: LifecycleOwner
        suspend fun popBackStackResult(@FragmentResultCode resultCode: Int = Activity.RESULT_OK, data: Intent? = null)
        suspend fun showTipDialog(text: String, icon: Int = QMUITipDialog.Builder.ICON_TYPE_INFO, isCanceledOnTouchOutside: Boolean = true, isCancelable: Boolean = true): QMUITipDialog
        suspend fun showTipDialog(res: Int, icon: Int = QMUITipDialog.Builder.ICON_TYPE_INFO, isCanceledOnTouchOutside: Boolean = true): QMUITipDialog
        suspend fun showToast(text: String)
        suspend fun showSnackBar(text: String, duration: Int = Snackbar.LENGTH_LONG, block: (Snackbar.() -> Unit)? = null)
        suspend fun dismissTipDialog(tipDialog: QMUITipDialog)
    }

    interface IPresenterBase<T : IFragmentBase> {
        val fragment: T
        suspend fun onViewCreated(rootView: View, fragment: T)
    }

    interface IModelBase
}