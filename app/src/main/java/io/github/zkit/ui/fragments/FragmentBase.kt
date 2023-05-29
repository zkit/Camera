package io.github.zkit.ui.fragments

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import es.dmoral.toasty.Toasty
import io.github.zkit.beans.enums.EnumAppTheme
import io.github.zkit.extensions.getCurrentSkinEnum
import io.github.zkit.ui.fragments.mvp.contracts.IFragmentContractBase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal abstract class FragmentBase : QMUIFragment(), IFragmentContractBase.IFragmentBase, KoinComponent {

    private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    private val mDefaultMainCoroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        mLogger.error("LOG:FragmentDevelop:CoroutineExceptionHandler coroutineContext={}", coroutineContext, throwable)
        val dialog: QMUITipDialog = QMUITipDialog.Builder(requireContext())
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
            .setSkinManager(get())
            .setTipWord(throwable.message.toString())
            .create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    override val coroutineExceptionHandler: CoroutineExceptionHandler
        get() = mDefaultMainCoroutineExceptionHandler

    override val lifecycleCoroutineScope: LifecycleCoroutineScope
        get() = lifecycleScope

    override val context1: Context
        get() = requireContext()

    override val activity1: QMUIFragmentActivity
        get() = requireActivity() as QMUIFragmentActivity

    override val manager: FragmentManager
        get() = parentFragmentManager

    override val lifecycleOwner: LifecycleOwner
        get() = this.viewLifecycleOwner

    final override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        lifecycleScope.launch(context = Dispatchers.Main + coroutineExceptionHandler) {
            onViewCreatedBySuspend(rootView)
        }
    }

    open suspend fun onViewCreatedBySuspend(rootView: View) {
    }

    @Suppress(names = ["DEPRECATION"])
    override suspend fun popBackStackResult(resultCode: Int, data: Intent?) = withContext(context = Dispatchers.Main) {
        setFragmentResult(resultCode, data)
        popBackStack()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(context = Dispatchers.Main + coroutineExceptionHandler) {
            onResumeChangeStatusBarMode()
        }
    }

    open suspend fun onResumeChangeStatusBarMode(): Unit = withContext(context = Dispatchers.Main) {
        when (get<QMUISkinManager>().getCurrentSkinEnum()) {
            EnumAppTheme.System -> QMUIStatusBarHelper.setStatusBarLightMode(requireActivity())
            EnumAppTheme.Light  -> QMUIStatusBarHelper.setStatusBarLightMode(requireActivity())
            EnumAppTheme.Dark   -> QMUIStatusBarHelper.setStatusBarDarkMode(requireActivity())
            EnumAppTheme.DarkA  -> QMUIStatusBarHelper.setStatusBarDarkMode(requireActivity())
        }
    }

    override suspend fun showTipDialog(text: String, icon: Int, isCanceledOnTouchOutside: Boolean, isCancelable: Boolean): QMUITipDialog = withContext(context = Dispatchers.Main) {
        val dialog: QMUITipDialog = QMUITipDialog.Builder(requireContext())
            .setSkinManager(get())
            .setTipWord(text)
            .setIconType(icon)
            .create()
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        dialog.show()
        return@withContext dialog
    }

    override suspend fun showTipDialog(res: Int, icon: Int, isCanceledOnTouchOutside: Boolean): QMUITipDialog =
        showTipDialog(text = requireContext().getString(res), icon, isCanceledOnTouchOutside)

    override suspend fun dismissTipDialog(tipDialog: QMUITipDialog): Unit = withContext(context = Dispatchers.Main) {
        tipDialog.dismiss()
    }

    override suspend fun showToast(text: String): Unit = withContext(context = Dispatchers.Main) {
        Toasty.normal(requireContext(), text, Toasty.LENGTH_LONG).show()
    }

    override suspend fun showSnackBar(text: String, duration: Int, block: (Snackbar.() -> Unit)?) = withContext(context = Dispatchers.Main) {
        val snackBar: Snackbar = Snackbar.make(requireView(), text, duration)
        block?.let { snackBar.apply(block) }
        snackBar.show()
    }

}