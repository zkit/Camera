package io.github.zkit.extensions

import androidx.appcompat.widget.ContentFrameLayout
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialogView
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView
import io.github.zkit.beans.enums.EnumAppTheme

internal fun QMUISkinManager.getCurrentSkinEnum(): EnumAppTheme {
    val enumAppTheme: EnumAppTheme? = EnumAppTheme.values().firstOrNull { it.ordinal == currentSkin }
    return enumAppTheme ?: EnumAppTheme.System
}

internal fun QMUITipDialog.updateText(text: String) {
    val content: ContentFrameLayout = this.findViewById(android.R.id.content)!!
    val dialogView: QMUITipDialogView = content.getChildAt(0) as QMUITipDialogView
    val textView: QMUISpanTouchFixTextView = dialogView.getChildAt(1) as QMUISpanTouchFixTextView
    textView.text = text
}
