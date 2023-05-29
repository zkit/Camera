package io.github.zkit.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.github.zkit.ui.dialogs.mvp.contracts.IDialogContractBase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal abstract class DialogBase : BottomSheetDialogFragment(), IDialogContractBase.IDialogBase, KoinComponent {

    private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    private val mDefaultMainCoroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        mLogger.error("LOG:DialogBase:CoroutineExceptionHandler coroutineContext={}", coroutineContext, throwable)
        val dialog: QMUITipDialog = QMUITipDialog.Builder(requireContext())
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
            .setSkinManager(get())
            .setTipWord(throwable.message.toString())
            .create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    override val context1: Context
        get() = requireContext()

    override val lifecycleOwner: LifecycleOwner
        get() = viewLifecycleOwner

    override val coroutineExceptionHandler: CoroutineExceptionHandler
        get() = mDefaultMainCoroutineExceptionHandler

    override val lifecycleCoroutineScope: LifecycleCoroutineScope
        get() = this.lifecycleScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        get<QMUISkinManager>().register(view)
        lifecycleCoroutineScope.launch(context = coroutineExceptionHandler) {
            onViewCreatedBySuspend(view, savedInstanceState)
        }
    }

    override suspend fun onViewCreatedBySuspend(view: View, savedInstanceState: Bundle?) {
    }

    override suspend fun dismissBySuspend(): Unit = withContext(context = Dispatchers.Main) {
        dismiss()
    }
}