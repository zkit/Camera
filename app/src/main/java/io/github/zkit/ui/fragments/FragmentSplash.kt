package io.github.zkit.ui.fragments

import android.view.View
import io.github.zkit.databinding.FragmentSplashBinding
import io.github.zkit.ui.fragments.mvp.contracts.IFragmentContractSplash
import io.github.zkit.ui.fragments.mvp.presenters.FragmentSplashPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

internal class FragmentSplash private constructor() : FragmentBase(), IFragmentContractSplash.IFragment {

    companion object {
        fun new(): FragmentSplash {
            return FragmentSplash()
        }
    }

    private val mPresenter: IFragmentContractSplash.IPresenter = FragmentSplashPresenter()

    private lateinit var mViewBinding: FragmentSplashBinding

    override val presenter: IFragmentContractSplash.IPresenter
        get() = mPresenter

    override fun onCreateView(): View {
        mViewBinding = FragmentSplashBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override suspend fun onViewCreatedBySuspend(rootView: View) {
        super.onViewCreatedBySuspend(rootView)
        delay(timeMillis = 1000L)
        mPresenter.onViewCreated(rootView, fragment = this)
    }

    override suspend fun gotoFragmentMain() = withContext(context = Dispatchers.Main) {
        startFragmentAndDestroyCurrent(FragmentMain.new())
        return@withContext
    }

}