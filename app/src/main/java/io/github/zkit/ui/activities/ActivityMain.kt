package io.github.zkit.ui.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.qmuiteam.qmui.skin.QMUISkinManager
import io.github.zkit.beans.enums.EnumAppTheme
import io.github.zkit.managers.IPreferencesManager
import io.github.zkit.ui.activities.mvp.contracts.IActivityContractMain
import io.github.zkit.ui.fragments.FragmentSplash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ActivityMain : ActivityBase(), IActivityContractMain.IActivity, KoinComponent {

    private val mPreferencesManager: IPreferencesManager by inject()
    private val mQMUISkinManager: QMUISkinManager by inject()

    override val presenter: IActivityContractMain.IPresenter
        get() = throw NotImplementedError()

    override val context: Context
        get() = this

    override suspend fun onCreateBySuspend(savedInstanceState: Bundle?) {
        super.onCreateBySuspend(savedInstanceState)
        initTheme()
        startFragment(FragmentSplash.new())
    }

    override suspend fun initTheme(): Unit = withContext(context = Dispatchers.Main) {
        val context: Context = this@ActivityMain
        when (mPreferencesManager.theme) {
            EnumAppTheme.System -> {
                if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                    mQMUISkinManager.changeSkin(if (mPreferencesManager.isThemeUseAmoled) EnumAppTheme.DarkA.ordinal else EnumAppTheme.Dark.ordinal)
                } else {
                    mQMUISkinManager.changeSkin(EnumAppTheme.Light.ordinal)
                }
            }

            EnumAppTheme.Light  -> mQMUISkinManager.changeSkin(EnumAppTheme.Light.ordinal)
            EnumAppTheme.Dark   -> mQMUISkinManager.changeSkin(EnumAppTheme.Dark.ordinal)
            EnumAppTheme.DarkA  -> mQMUISkinManager.changeSkin(EnumAppTheme.DarkA.ordinal)
        }
    }

}