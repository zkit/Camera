package io.github.zkit.ui.fragments.mvp.contracts

internal interface IFragmentContractSplash {

    interface IFragment : IFragmentContractBase.IFragmentBase {
        val presenter: IPresenter
        suspend fun gotoFragmentMain()
    }

    interface IPresenter : IFragmentContractBase.IPresenterBase<IFragment> {
        suspend fun onPolicyAgree()
        suspend fun initDeviceId()
        suspend fun initAppCenter()
    }

    interface IModule : IFragmentContractBase.IModelBase

}