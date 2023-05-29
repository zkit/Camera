package io.github.zkit.app.mvp.presenters

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.google.gson.GsonBuilder
import com.microsoft.appcenter.crashes.Crashes
import com.qmuiteam.qmui.QMUILog
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.qmuiteam.qmui.skin.QMUISkinManager
import io.github.zkit.R
import io.github.zkit.app.mvp.contracts.IApplicationContractMain
import io.github.zkit.beans.enums.EnumAppTheme
import io.github.zkit.commons.loggers.LoggerKoin
import io.github.zkit.commons.loggers.LoggerQmui
import io.github.zkit.managers.ILoggerManager
import io.github.zkit.managers.IWatermarkManager
import io.github.zkit.managers.INetworkManager
import io.github.zkit.managers.IPreferencesManager
import io.github.zkit.managers.IRuntimeManager
import io.github.zkit.managers.impls.ImplLoggerManager
import io.github.zkit.managers.impls.ImplWatermarkManager
import io.github.zkit.managers.impls.ImplNetworkManager
import io.github.zkit.managers.impls.ImplPreferencesManager
import io.github.zkit.managers.impls.ImplRuntimeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

internal class ApplicationMainPresenter : IApplicationContractMain.IPresenter, KoinComponent, Thread.UncaughtExceptionHandler {

    private lateinit var mApplication: IApplicationContractMain.IApplication

    private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override suspend fun onCreate(app: IApplicationContractMain.IApplication): Unit = withContext(context = Dispatchers.IO) {
        mApplication = app
        initKoin()
        initLogger()
        initCrash()
        initQmui()
        initDatabase()
        initBroadcast()
        mLogger.info("==================== [APP START] ====================")
        return@withContext
    }

    override suspend fun initLogger() {
        get<ILoggerManager>().init()
    }

    override suspend fun initKoin() {
        startKoin {
            logger(logger = LoggerKoin(level = Level.DEBUG))
            androidContext(androidContext = mApplication.context)
            modules(mKoinManagerModule, mKoinCommonModule)
        }
    }

    override suspend fun initCrash(): Unit = withContext(context = Dispatchers.IO) {
        Thread.setDefaultUncaughtExceptionHandler(this@ApplicationMainPresenter)
    }

    override suspend fun initQmui(): Unit = withContext(context = Dispatchers.IO) {
        QMUILog.setDelegete(LoggerQmui())
        QMUISwipeBackActivityManager.init(get())

        val mQMUISkinManager: QMUISkinManager by inject()
        mQMUISkinManager.addSkin(EnumAppTheme.Light.ordinal, R.style.AppThemeLight)
        mQMUISkinManager.addSkin(EnumAppTheme.Dark.ordinal, R.style.AppThemeDark)
        mQMUISkinManager.addSkin(EnumAppTheme.DarkA.ordinal, R.style.AppThemeDarkA)
    }

    override suspend fun initDatabase() {
    }

    override suspend fun initBroadcast() {
    }

    private val mKoinManagerModule = module {
        single<IPreferencesManager> { ImplPreferencesManager() }
        single<ILoggerManager> { ImplLoggerManager() }
        single<INetworkManager> { ImplNetworkManager() }
        single<IWatermarkManager> { ImplWatermarkManager() }
        single<IRuntimeManager> { ImplRuntimeManager() }
    }

    private val mKoinCommonModule = module {
        single<Application> { mApplication.application }
        single { GsonBuilder().serializeNulls().create() }
        single { QMUISkinManager.defaultInstance(get()) }
        single { Handler(Looper.getMainLooper()) }
        single { get<INetworkManager>().github }
    }

    override fun uncaughtException(thread: Thread, e: Throwable) {
        mLogger.error("LOG:ApplicationMainPresenter:uncaughtException thread={}", thread, e)
        Crashes.trackError(e)
    }

}