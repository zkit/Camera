package io.github.zkit.managers.impls

import android.webkit.WebSettings
import io.github.zkit.constants.ConstantApp
import io.github.zkit.managers.IRuntimeManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class ImplRuntimeManager : IRuntimeManager, KoinComponent {
    private var mDeviceId: String = ConstantApp.EMPTY_STRING
    private val mUserAgent: String = WebSettings.getDefaultUserAgent(get())

    override var deviceId: String = mDeviceId
    override val userAgent: String = mUserAgent

}