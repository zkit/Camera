package io.github.zkit.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.microsoft.appcenter.crashes.Crashes
import es.dmoral.toasty.Toasty
import io.github.zkit.app.mvp.contracts.IApplicationContractMain
import io.github.zkit.app.mvp.presenters.ApplicationMainPresenter
import kotlinx.coroutines.CoroutineExceptionHandler

internal class Application : ApplicationLifecycle(), IApplicationContractMain.IApplication {

    private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)
    private val mPresenter: IApplicationContractMain.IPresenter = ApplicationMainPresenter()
    private val mMainThreadCrashHandler: Handler = Handler(Looper.getMainLooper())

    private val mMainCoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        mLogger.error("coroutineContext={}", coroutineContext, throwable)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mMainThreadCrashHandler.post(mMainThreadSafeLoopRunnable)
    }

    override val presenter: IApplicationContractMain.IPresenter
        get() = mPresenter

    override val coroutineExceptionHandler: CoroutineExceptionHandler
        get() = mMainCoroutineExceptionHandler

    override val lifecycleCoroutineScope: LifecycleCoroutineScope
        get() = this.lifecycleScope

    override suspend fun onCreatedBySuspend() {
        presenter.onCreate(app = this)
    }

    private val mMainThreadSafeLoopRunnable = Runnable {
        while (true) {
            try {
                Looper.loop()
            } catch (throwable: Throwable) {
                Crashes.trackError(throwable)
                mLogger.error("LOG:Application:main thread={}", Thread.currentThread(), throwable)
                Toasty.error(this.applicationContext, "拦截到主线程崩溃：${throwable.message}", Toasty.LENGTH_LONG).show()
            }
        }
    }

}
