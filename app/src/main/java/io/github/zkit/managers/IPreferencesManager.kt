package io.github.zkit.managers

import io.github.zkit.beans.enums.EnumAppTheme

internal interface IPreferencesManager {

    /**
     * 当前主题配置
     */
    var theme: EnumAppTheme

    /**
     * 黑色主题是否使用A屏配置
     */
    var isThemeUseAmoled: Boolean

    /**
     * 是否同意统计分析
     */
    var isAgreeAnalytics: Boolean

}