package io.github.zkit.ui.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import io.github.zkit.R


internal class QMUINestedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : NestedScrollView(context, attrs, defStyle) {

    private var mMaxHeight: Int = -1

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.QMUINestedScrollView)
        mMaxHeight = typedArray.getLayoutDimension(R.styleable.QMUINestedScrollView_maxHeight, mMaxHeight)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMeasureSpecReal = if (mMaxHeight > 0) MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST) else heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpecReal)
    }
}