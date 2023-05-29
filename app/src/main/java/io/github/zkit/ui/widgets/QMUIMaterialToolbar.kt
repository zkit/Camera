package io.github.zkit.ui.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import androidx.collection.SimpleArrayMap
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.android.material.appbar.MaterialToolbar
import com.qmuiteam.qmui.skin.IQMUISkinHandlerView
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.skin.defaultAttr.IQMUISkinDefaultAttrProvider
import com.qmuiteam.qmui.util.QMUIResHelper

class QMUIMaterialToolbar : MaterialToolbar, IQMUISkinDefaultAttrProvider, IQMUISkinHandlerView {

    companion object {
        private val sDefaultSkinAttrs: SimpleArrayMap<String, Int> = SimpleArrayMap<String, Int>().apply {
            put(QMUISkinValueBuilder.TEXT_COLOR, com.qmuiteam.qmui.R.attr.qmui_skin_support_topbar_title_color)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getDefaultSkinAttrs(): SimpleArrayMap<String, Int> {
        return sDefaultSkinAttrs
    }

    override fun handle(manager: QMUISkinManager, skinIndex: Int, theme: Resources.Theme, attrs: SimpleArrayMap<String, Int>?) {
        if (attrs == null) {
            return
        }
        for (i in 0 until attrs.size()) {
            when (attrs.keyAt(i)) {
                QMUISkinValueBuilder.TEXT_COLOR -> QMUIResHelper.getAttrColor(theme, attrs.valueAt(i)).let { color ->
                    setTitleTextColor(color)
                    setNavigationIconTint(color)
                    for (j in 0 until menu.size()) {
                        when (val icon = menu.getItem(j).icon) {
                            is VectorDrawableCompat -> icon.setTintList(ColorStateList.valueOf(color))
                            is VectorDrawable       -> icon.setTintList(ColorStateList.valueOf(color))
                        }
                    }
                }
            }
        }
    }
}