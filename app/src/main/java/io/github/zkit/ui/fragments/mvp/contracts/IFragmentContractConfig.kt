package io.github.zkit.ui.fragments.mvp.contracts

import io.github.zkit.beans.BeanWatermarkConfig
import io.github.zkit.databinding.FragmentConfigBinding
import io.github.zkit.ui.adapters.AdapterFragmentConfig

internal interface IFragmentContractConfig {

    interface IFragment : IFragmentContractBase.IFragmentBase {
        val viewBinding: FragmentConfigBinding
        val adapter: AdapterFragmentConfig
        val watermarkPath: String
        val presenter: IPresenter

        fun update()
        suspend fun updateTitle(title: String)
        suspend fun updateRecyclerView(list: MutableList<Pair<String, BeanWatermarkConfig>>)
    }

    interface IPresenter : IFragmentContractBase.IPresenterBase<IFragment> {
        suspend fun refresh()
        suspend fun save()
    }

}