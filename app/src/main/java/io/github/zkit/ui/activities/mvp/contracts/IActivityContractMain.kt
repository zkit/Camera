package io.github.zkit.ui.activities.mvp.contracts

internal interface IActivityContractMain {

    interface IActivity : IActivityContractBase.IActivityBase {
        val presenter: IPresenter
        suspend fun initTheme()
    }

    interface IPresenter : IActivityContractBase.IPresenterBase<IActivity>

    interface IModule

}