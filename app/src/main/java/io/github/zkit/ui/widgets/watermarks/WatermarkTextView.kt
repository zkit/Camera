package io.github.zkit.ui.widgets.watermarks

import android.content.Context
import android.util.AttributeSet
import org.koin.core.component.KoinComponent

internal class WatermarkTextView : WatermarkBase, KoinComponent {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}