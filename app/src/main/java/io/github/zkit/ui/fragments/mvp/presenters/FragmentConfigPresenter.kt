package io.github.zkit.ui.fragments.mvp.presenters

import android.view.View
import io.github.uhsk.kit.toFile
import io.github.zkit.beans.BeanWatermarkConfig
import io.github.zkit.managers.IWatermarkManager
import io.github.zkit.ui.fragments.mvp.contracts.IFragmentContractConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

internal class FragmentConfigPresenter : IFragmentContractConfig.IPresenter, KoinComponent {

    private lateinit var mFragment: IFragmentContractConfig.IFragment

    private val iWatermarkManager: IWatermarkManager by inject()

    override val fragment: IFragmentContractConfig.IFragment
        get() = mFragment

    @Suppress(names = ["DEPRECATION"])
    override suspend fun onViewCreated(rootView: View, fragment: IFragmentContractConfig.IFragment): Unit = withContext(context = Dispatchers.IO) {
        mFragment = fragment

        fragment.updateTitle(fragment.watermarkPath.toFile().name)
        refresh()
    }

    override suspend fun refresh() {
        val list: MutableList<Pair<String, BeanWatermarkConfig>> = iWatermarkManager.config.map { Pair(it.key, it.value) }.toMutableList()
        fragment.updateRecyclerView(list)
    }

    override suspend fun save() {
        val list: MutableList<Pair<String, BeanWatermarkConfig>> = fragment.adapter.data
        for (config: Pair<String, BeanWatermarkConfig> in list) {
            iWatermarkManager.config[config.first] = config.second
        }
        get<IWatermarkManager>().save()
    }

}
