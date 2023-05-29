package io.github.zkit.ui.fragments

import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.zkit.databinding.FragmentMainBinding
import io.github.zkit.managers.IWatermarkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.core.component.KoinComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

internal class FragmentMain private constructor() : FragmentBase(), KoinComponent, View.OnClickListener {
    companion object {
        fun new(): FragmentMain {
            return FragmentMain()
        }
    }

    private lateinit var mViewBinding: FragmentMainBinding

    private val mLogger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun onCreateView(): View {
        mViewBinding = FragmentMainBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override suspend fun onViewCreatedBySuspend(rootView: View) {
        super.onViewCreatedBySuspend(rootView)
        mViewBinding.btnReload.setOnClickListener(this)
        reload()
    }

    private suspend fun reload() = withContext(context = Dispatchers.Main) {
        val folder: File = requireContext().getExternalFilesDir("水印控件")!!.resolve(relative = "工作").resolve(relative = "电子时钟")
        mLogger.debug("LOG:FragmentMain:onViewCreatedBySuspend f={}", folder)
        get<IWatermarkManager>().load(folder)
        val viewGroup: ViewGroup = get<IWatermarkManager>().layout(context1)
        viewGroup.setOnClickListener { startFragment(FragmentConfig.new(folder.absolutePath)) }
        mViewBinding.llWatermark.removeAllViews()
        mViewBinding.llWatermark.addView(viewGroup)
    }

    override fun onClick(view: View) {
        lifecycleCoroutineScope.launch(context = coroutineExceptionHandler) {
            onClickBySuspend(view)
        }
    }

    private suspend fun onClickBySuspend(view: View) = withContext(context = Dispatchers.Main) {
        when (view) {
            mViewBinding.btnReload -> reload()
        }
    }

}