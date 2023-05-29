package io.github.zkit.ui.activities

import android.os.Bundle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.github.zkit.beans.enums.EnumAppTheme
import io.github.zkit.ui.activities.mvp.contracts.IActivityContractBase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal abstract class ActivityBase : QMUIFragmentActivity(), IActivityContractBase.IActivityBase, KoinComponent, QMUISkinManager.OnSkinChangeListener {

    private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override val lifecycleCoroutineScope: LifecycleCoroutineScope
        get() = lifecycleScope

    override val coroutineExceptionHandler: CoroutineExceptionHandler
        get() = mDefaultMainCoroutineExceptionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.skinManager = get()
        this.skinManager.addSkinChangeListener(this)

        lifecycleScope.launch(context = Dispatchers.Main + coroutineExceptionHandler) {
            onCreateBySuspend(savedInstanceState)
        }
    }

    open suspend fun onCreateBySuspend(savedInstanceState: Bundle?) {
    }

    override fun onSkinChange(skinManager: QMUISkinManager, oldSkin: Int, newSkin: Int) {
        when (EnumAppTheme.values().first { it.ordinal == newSkin }) {
            EnumAppTheme.System -> TODO()
            EnumAppTheme.Light  -> QMUIStatusBarHelper.setStatusBarLightMode(this)

            EnumAppTheme.Dark,
            EnumAppTheme.DarkA  -> QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }
    }

    private val mDefaultMainCoroutineExceptionHandler: CoroutineExceptionHandler
        get() = CoroutineExceptionHandler { coroutineContext, throwable ->
            mLogger.error("LOG:FragmentDevelop:CoroutineExceptionHandler coroutineContext={}", coroutineContext, throwable)
            val dialog: QMUITipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setSkinManager(get())
                .setTipWord(throwable.message.toString())
                .create()
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }


}