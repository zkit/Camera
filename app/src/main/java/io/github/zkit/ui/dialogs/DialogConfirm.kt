package io.github.zkit.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.zkit.databinding.DialogConfirmBinding
import io.github.zkit.ui.beans.enums.EnumDialogResultCode
import io.github.zkit.ui.dialogs.mvp.contracts.IDialogContractConfirm
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

internal class DialogConfirm : DialogBase(), IDialogContractConfirm.IDialog {

    private lateinit var mViewBinding: DialogConfirmBinding

    private var mResultCode: EnumDialogResultCode = EnumDialogResultCode.None
    private var mCancellableContinuation: CancellableContinuation<EnumDialogResultCode>? = null

    override val viewBinding: DialogConfirmBinding
        get() = mViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewBinding = DialogConfirmBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override suspend fun onViewCreatedBySuspend(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBySuspend(view, savedInstanceState)
        viewBinding.btnConfirm.setOnClickListener(mButtonConfirmOnClickListener)
        viewBinding.btnClose.setOnClickListener(mButtonCancelOnClickListener)
        dialog?.setOnDismissListener(mOnDismissListener)
    }

    override suspend fun updateTitle(title: String) = withContext(context = Dispatchers.Main) {
        viewBinding.tvTitle.text = title
    }

    override suspend fun updateMessage(message: String) {
        viewBinding.tvContent.text = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
    }

    override suspend fun updateButtonConfirm(text: String) = withContext(context = Dispatchers.Main) {
        viewBinding.btnConfirm.text = text
    }

    private val mButtonCancelOnClickListener = View.OnClickListener {
        mResultCode = EnumDialogResultCode.Close
        mOnDismissListener.onDismiss(dialog)
        dismiss()
    }

    private val mButtonConfirmOnClickListener = View.OnClickListener {
        mResultCode = EnumDialogResultCode.Confirm
        mOnDismissListener.onDismiss(dialog)
        dismiss()
    }

    override suspend fun await(): EnumDialogResultCode = suspendCancellableCoroutine { it: CancellableContinuation<EnumDialogResultCode> -> mCancellableContinuation = it }

    private val mOnDismissListener = DialogInterface.OnDismissListener {
        if (mCancellableContinuation?.isActive == true) {
            mCancellableContinuation?.resumeWith(Result.success(mResultCode))
        }
    }
}