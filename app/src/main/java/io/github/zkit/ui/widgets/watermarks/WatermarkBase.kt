package io.github.zkit.ui.widgets.watermarks

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.github.zkit.ui.widgets.watermarks.mvp.contracts.IWatermarkBase
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal abstract class WatermarkBase : AppCompatTextView, IWatermarkBase, KoinComponent {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val mLogger: Logger = LoggerFactory.getLogger(this.javaClass)
    private val mLifecycleCoroutineScope: LifecycleCoroutineScope = (context as AppCompatActivity).lifecycleScope
    private val mFragmentManager: FragmentManager = (context as AppCompatActivity).supportFragmentManager

    private var mLuaOnContentChangedCallback: LuaFunction? = null

    override val coroutineExceptionHandler: CoroutineExceptionHandler
        get() = mDefaultMainCoroutineExceptionHandler

    override val lifecycleCoroutineScope: LifecycleCoroutineScope
        get() = mLifecycleCoroutineScope

    override var onContentChangedCallback: LuaFunction? = mLuaOnContentChangedCallback

    private val mDefaultMainCoroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        mLogger.error("LOG:DialogBase:CoroutineExceptionHandler coroutineContext={}", coroutineContext, throwable)
        val dialog: QMUITipDialog = QMUITipDialog.Builder(this.context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
            .setSkinManager(get())
            .setTipWord(throwable.message.toString())
            .create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

}