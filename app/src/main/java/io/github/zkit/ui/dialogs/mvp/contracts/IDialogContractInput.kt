package io.github.zkit.ui.dialogs.mvp.contracts

import io.github.zkit.databinding.DialogInputBinding
import io.github.zkit.ui.beans.enums.EnumDialogResultCode

internal interface IDialogContractInput {

    interface IDialog : IDialogContractBase.IDialogBase {
        val viewBinding: DialogInputBinding
        suspend fun await(): EnumDialogResultCode
        suspend fun updateTitle(title: String)
        suspend fun updateContent(content: String)
        suspend fun updateEditText(text: String)
        suspend fun updateButtonConfirm(text: String)
    }

    interface IPresenter : IDialogContractBase.IPresenterBase<IDialog> {
        suspend fun onClickConfirm()
    }

    interface IModel : IDialogContractBase.IModelBase {
    }
}