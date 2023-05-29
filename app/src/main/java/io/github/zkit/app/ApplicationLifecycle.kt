package io.github.zkit.app

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import io.github.zkit.app.mvp.contracts.IApplicationContractBase
import kotlinx.coroutines.runBlocking

internal abstract class ApplicationLifecycle : Application(), LifecycleOwner, IApplicationContractBase.IAppBase {

    private val mRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private val mHandle: Handler = Handler(Looper.myLooper()!!)
    private var mLastDispatchRunnable: DispatchRunnable? = null

    abstract suspend fun onCreatedBySuspend()

    override fun getLifecycle(): Lifecycle = mRegistry

    final override fun onCreate() {
        postDispatchRunnable(Lifecycle.Event.ON_CREATE)
        postDispatchRunnable(Lifecycle.Event.ON_START)
        super.onCreate()
        runBlocking(context = coroutineExceptionHandler) {
            onCreatedBySuspend()
        }
    }

    override fun onTerminate() {
        postDispatchRunnable(Lifecycle.Event.ON_STOP)
        postDispatchRunnable(Lifecycle.Event.ON_DESTROY)
        super.onTerminate()
    }

    private fun postDispatchRunnable(event: Lifecycle.Event) {
        mLastDispatchRunnable?.run()
        mLastDispatchRunnable = DispatchRunnable(mRegistry, event)
        mHandle.postAtFrontOfQueue(mLastDispatchRunnable!!)
    }

    private class DispatchRunnable(private val mRegistry: LifecycleRegistry, private val mEvent: Lifecycle.Event) : Runnable {
        private var mWasExecuted: Boolean = false
        override fun run() {
            if (mWasExecuted) {
                return
            }
            mRegistry.handleLifecycleEvent(mEvent)
            mWasExecuted = true
        }
    }

    override val context: Context
        get() = this@ApplicationLifecycle.applicationContext

    override val lifecycleCoroutineScope: LifecycleCoroutineScope
        get() = lifecycleScope

    override val application: Application
        get() = this@ApplicationLifecycle
}