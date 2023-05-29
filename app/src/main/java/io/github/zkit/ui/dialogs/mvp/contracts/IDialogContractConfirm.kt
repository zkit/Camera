package io.github.zkit.ui.dialogs.mvp.contracts

import io.github.zkit.databinding.DialogConfirmBinding
import io.github.zkit.ui.beans.enums.EnumDialogResultCode

internal interface IDialogContractConfirm {

    interface IDialog : IDialogContractBase.IDialogBase {
        val viewBinding: DialogConfirmBinding
        suspend fun await(): EnumDialogResultCode
        suspend fun updateTitle(title: String)
        suspend fun updateMessage(message: String)
        suspend fun updateButtonConfirm(text: String)
    }

    interface IPresenter : IDialogContractBase.IPresenterBase<IDialog> {
        suspend fun onClickConfirm()
    }

    interface IModel : IDialogContractBase.IModelBase {
    }
}