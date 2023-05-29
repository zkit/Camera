package io.github.zkit.ui.fragments.mvp.presenters

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import com.github.gzuliyujiang.oaid.DeviceID
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import io.github.zkit.BuildConfig
import io.github.zkit.app.mvp.contracts.IApplicationContractMain
import io.github.zkit.commons.loggers.LoggerAppCenter
import io.github.zkit.constants.ConstantApp
import io.github.zkit.managers.IPreferencesManager
import io.github.zkit.managers.IRuntimeManager
import io.github.zkit.ui.fragments.mvp.contracts.IFragmentContractSplash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.UUID

internal class FragmentSplashPresenter : IFragmentContractSplash.IPresenter, KoinComponent {

    private lateinit var mFragment: IFragmentContractSplash.IFragment

    override val fragment: IFragmentContractSplash.IFragment
        get() = mFragment

    override suspend fun onViewCreated(rootView: View, fragment: IFragmentContractSplash.IFragment): Unit = withContext(context = Dispatchers.IO) {
        mFragment = fragment
        onPolicyAgree()
    }

    override suspend fun onPolicyAgree(): Unit = withContext(context = Dispatchers.IO) {
        initDeviceId()
        initAppCenter()
        fragment.gotoFragmentMain()
        return@withContext
    }

    override suspend fun initDeviceId(): Unit = withContext(context = Dispatchers.IO) {
        DeviceID.register(get<Context>() as Application)
        withTimeout(timeMillis = 3000L) {
            while (DeviceID.getClientId().isNullOrBlank()) {
                yield()
                delay(timeMillis = 100)
            }
        }
        get<IRuntimeManager>().deviceId = UUID.nameUUIDFromBytes(DeviceID.getClientId().toByteArray(charset = Charsets.UTF_8)).toString()
    }

    override suspend fun initAppCenter(): Unit = withContext(context = Dispatchers.IO) {
        AppCenter.setLogger(LoggerAppCenter())
        AppCenter.setLogLevel(if (BuildConfig.DEBUG) Log.VERBOSE else Log.ASSERT)
        AppCenter.start(get(), ConstantApp.APP_CENTER_SECRET, Analytics::class.java, Crashes::class.java)

        AppCenter.setEnabled(get<IPreferencesManager>().isAgreeAnalytics)
        AppCenter.setUserId(get<IRuntimeManager>().deviceId)
    }


}