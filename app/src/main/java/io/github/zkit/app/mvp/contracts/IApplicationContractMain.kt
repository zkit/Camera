package io.github.zkit.app.mvp.contracts

internal interface IApplicationContractMain {

    interface IApplication : IApplicationContractBase.IAppBase {
        val presenter: IPresenter
    }

    interface IPresenter : IApplicationContractBase.IPresenterBase<IApplication> {
        suspend fun initLogger()
        suspend fun initKoin()
        suspend fun initCrash()
        suspend fun initQmui()
        suspend fun initDatabase()
        suspend fun initBroadcast()
    }

    interface IModule : IApplicationContractBase.IModelBase
}