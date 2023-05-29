package io.github.zkit.ui.widgets

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import androidx.collection.SimpleArrayMap
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.qmuiteam.qmui.skin.IQMUISkinHandlerView
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.skin.defaultAttr.IQMUISkinDefaultAttrProvider
import com.qmuiteam.qmui.util.QMUIResHelper

internal class QMUICollapsingToolbarLayout : CollapsingToolbarLayout, IQMUISkinDefaultAttrProvider, IQMUISkinHandlerView {

    companion object {
        private val sDefaultSkinAttrs: SimpleArrayMap<String, Int> = SimpleArrayMap<String, Int>().apply {
            put(QMUISkinValueBuilder.TEXT_COLOR, com.qmuiteam.qmui.R.attr.qmui_skin_text_color)
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
                    setExpandedTitleColor(color)
                    setCollapsedTitleTextColor(color)
                }
            }
        }
    }

}
















