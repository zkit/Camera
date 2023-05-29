package io.github.zkit.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.webview.QMUIWebView
import io.github.zkit.BuildConfig

@SuppressLint("SetJavaScriptEnabled")
internal class QMUIWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = android.R.attr.webViewStyle) : QMUIWebView(context, attrs, defStyle) {
    init {
        val webSettings: WebSettings = settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.defaultTextEncodingName = "GBK"
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.textZoom = 100
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        webSettings.displayZoomControls = false

        val screen: String = QMUIDisplayHelper.getScreenWidth(context).toString() + "x" + QMUIDisplayHelper.getScreenHeight(context)
        val userAgent = "Camera/${BuildConfig.VERSION_NAME}(Screen/$screen; Scale/${QMUIDisplayHelper.getDensity(context)})"
        val agent: String? = settings.userAgentString
        if (agent == null || !agent.contains(userAgent)) {
            settings.userAgentString = "$agent $userAgent"
        }
        // 开启调试
        if (BuildConfig.DEBUG) {
            setWebContentsDebuggingEnabled(true)
        }
    }
}