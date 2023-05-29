package io.github.zkit.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import io.github.zkit.R
import io.github.zkit.beans.BeanWatermarkConfig
import io.github.zkit.commons.differs.DifferFragmentConfig
import io.github.zkit.constants.ConstantApp
import io.github.zkit.databinding.FragmentConfigBinding
import io.github.zkit.ui.adapters.AdapterFragmentConfig
import io.github.zkit.ui.fragments.mvp.contracts.IFragmentContractConfig
import io.github.zkit.ui.fragments.mvp.presenters.FragmentConfigPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class FragmentConfig private constructor() : FragmentBase(), IFragmentContractConfig.IFragment, KoinComponent, Toolbar.OnMenuItemClickListener {

    private lateinit var mViewBinding: FragmentConfigBinding
    private lateinit var mWatermarkPath: String

    private val mAdapter: AdapterFragmentConfig = AdapterFragmentConfig(mFragment = this)
    private val mPresenter: IFragmentContractConfig.IPresenter = FragmentConfigPresenter()


    companion object {
        fun new(path: String): FragmentConfig {
            val fragment = FragmentConfig()
            val bundle = android.os.Bundle()
            bundle.putString(ConstantApp.INTENT_PARAM_1, path)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val viewBinding: FragmentConfigBinding
        get() = mViewBinding

    override val adapter: AdapterFragmentConfig
        get() = mAdapter

    override val watermarkPath: String
        get() = mWatermarkPath

    override val presenter: IFragmentContractConfig.IPresenter
        get() = mPresenter

    override fun onCreateView(): View {
        mViewBinding = FragmentConfigBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override suspend fun onViewCreatedBySuspend(rootView: View) {
        super.onViewCreatedBySuspend(rootView)
        initToolbar()
        initRecyclerView()
        initValue()
        mPresenter.onViewCreated(rootView, fragment = this@FragmentConfig)
    }

    private suspend fun initToolbar(): Unit = withContext(context = Dispatchers.Main) {
        viewBinding.toolbar.setNavigationOnClickListener(mToolbarOnNavigationClickListener)
        viewBinding.toolbar.setOnMenuItemClickListener(this@FragmentConfig)
        return@withContext
    }

    private suspend fun initRecyclerView(): Unit = withContext(context = Dispatchers.Main) {
        viewBinding.recyclerView.adapter = mAdapter
        viewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        viewBinding.recyclerView.addItemDecoration(mItemDecoration)

        adapter.animationEnable = true
        adapter.isAnimationFirstOnly = false
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn)
        adapter.setEmptyView(R.layout.common_list_loading)
        adapter.headerWithEmptyEnable = true

        return@withContext
    }

    private suspend fun initValue() {
        mWatermarkPath = arguments?.getString(ConstantApp.INTENT_PARAM_1) ?: ConstantApp.EMPTY_STRING
    }

    @Suppress(names = ["DEPRECATION"])
    override fun onBackPressed() {
        runBlocking(context = coroutineExceptionHandler) { presenter.save() }
        setFragmentResult(Activity.RESULT_OK, Intent())
        super.onBackPressed()
    }

    override suspend fun updateTitle(title: String): Unit = withContext(context = Dispatchers.Main) {
        viewBinding.collapsingToolbarLayout.title = title
        return@withContext
    }

    override suspend fun updateRecyclerView(list: MutableList<Pair<String, BeanWatermarkConfig>>): Unit = withContext(context = Dispatchers.Main) {
        if (list.isEmpty()) {
            adapter.setEmptyView(R.layout.common_list_empty)
            adapter.setNewInstance(list)
            return@withContext
        }
        adapter.setDiffNewData(DiffUtil.calculateDiff(DifferFragmentConfig(new = list, old = adapter.data)), list)
        return@withContext
    }

    override fun update() {
        lifecycleCoroutineScope.launch(context = coroutineExceptionHandler) { mPresenter.refresh() }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return true
    }


    private val mToolbarOnNavigationClickListener = View.OnClickListener {
        lifecycleCoroutineScope.launch(context = Dispatchers.Main + coroutineExceptionHandler) {
            popBackStackResult(resultCode = Activity.RESULT_CANCELED)
        }
    }

    private val mItemDecoration: RecyclerView.ItemDecoration = object : RecyclerView.ItemDecoration() {
        private val mSpace: Int = QMUIDisplayHelper.dp2px(get(), 8)
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val layoutParams: StaggeredGridLayoutManager.LayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams

            if (layoutParams.isFullSpan) {
                outRect.left = mSpace
                outRect.bottom = mSpace
                return
            }

            if (view is ConstraintLayout) {
                when (layoutParams.spanIndex) {
                    0 -> outRect.right = mSpace
                    1 -> outRect.left = mSpace
                }
                outRect.bottom = mSpace * 2
                return
            }
        }
    }

}