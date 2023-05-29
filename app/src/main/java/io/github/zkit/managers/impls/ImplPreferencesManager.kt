package io.github.zkit.managers.impls

import android.content.Context
import android.content.SharedPreferences
import io.github.zkit.beans.enums.EnumAppTheme
import io.github.zkit.managers.IPreferencesManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress(names = ["DEPRECATION"])
internal class ImplPreferencesManager : IPreferencesManager, KoinComponent {

    private val mContext: Context by inject()
    private val mDefaultSharedPreferences: SharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext)

    override var theme: EnumAppTheme
        get() = EnumAppTheme.values().firstOrNull { it.ordinal == mDefaultSharedPreferences.getInt("0x01001", 0) } ?: EnumAppTheme.System
        set(value) = mDefaultSharedPreferences.edit().putInt("0x01001", value.ordinal).apply()

    override var isThemeUseAmoled: Boolean
        get() = mDefaultSharedPreferences.getBoolean("0x01002", false)
        set(value) = mDefaultSharedPreferences.edit().putBoolean("0x01002", value).apply()

    override var isAgreeAnalytics: Boolean
        get() = mDefaultSharedPreferences.getBoolean("0x01003", false)
        set(value) = mDefaultSharedPreferences.edit().putBoolean("0x01003", value).apply()

}