package io.github.zkit.ui.adapters

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.github.zkit.R
import io.github.zkit.beans.BeanWatermarkConfig
import io.github.zkit.databinding.FragmentConfigContentBinding
import io.github.zkit.ui.beans.enums.EnumDialogResultCode
import io.github.zkit.ui.dialogs.DialogInput
import io.github.zkit.ui.fragments.mvp.contracts.IFragmentContractConfig
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

internal class AdapterFragmentConfig(private val mFragment: IFragmentContractConfig.IFragment) : BaseQuickAdapter<Pair<String, BeanWatermarkConfig>, BaseViewHolder>(R.layout.fragment_config_content, mutableListOf()), KoinComponent, View.OnClickListener {

    private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override fun convert(holder: BaseViewHolder, item: Pair<String, BeanWatermarkConfig>) {
        val viewBinding: FragmentConfigContentBinding = FragmentConfigContentBinding.bind(holder.itemView)
        viewBinding.tvTitle.text = item.second.title
        viewBinding.tvContent.text = item.second.value.ifBlank { item.second.default }
        viewBinding.root.setOnClickListener(this)
        viewBinding.root.setTag(R.id.view_tag_param_1, item)
    }

    override fun onClick(view: View) {
        mFragment.lifecycleCoroutineScope.launch(context = mFragment.coroutineExceptionHandler) {
            onClickBySuspend(view)
        }
    }

    @Suppress(names = ["UNCHECKED_CAST"])
    private suspend fun onClickBySuspend(view: View) {
        val item: Pair<String, BeanWatermarkConfig> = view.getTag(R.id.view_tag_param_1) as Pair<String, BeanWatermarkConfig>
        val config: BeanWatermarkConfig = item.second
        val dialog = DialogInput()
        dialog.showNow(mFragment.manager, config.title)
        dialog.updateTitle(config.title)
        dialog.updateEditText(config.value.ifBlank { config.default })
        val value: EnumDialogResultCode = dialog.await()
        if (value != EnumDialogResultCode.Confirm) {
            return
        }
        val newValue: String = dialog.viewBinding.etInput.text.toString()
        config.value = newValue

        val index: Int = data.indexOf(item)
        mFragment.adapter.notifyItemChanged(index)
    }

}