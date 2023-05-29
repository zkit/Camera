package io.github.zkit.managers.impls

import android.content.Context
import android.webkit.WebSettings
import io.github.zkit.BuildConfig
import io.github.zkit.managers.INetworkManager
import io.github.zkit.managers.IRuntimeManager
import io.github.zkit.managers.impls.retrofit.INetworkGithub
import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.HttpMethod
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

internal class ImplNetworkManager : INetworkManager, KoinComponent {

    /**
     * OkHttp网络缓存
     */
    private val mOkHttpCache: okhttp3.Cache = okhttp3.Cache(get<Context>().cacheDir.resolve(relative = "http"), 50 * 1024 * 1024)

    /**
     * Okhttp拦截器：日志
     */
    private val mOkHttpLogger: HttpLoggingInterceptor.Logger = object : HttpLoggingInterceptor.Logger {
        private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)
        override fun log(message: String): Unit = mLogger.debug("LOG:Network {}", message)
    }

    /**
     * Okhttp
     */
    private val mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor(mOkHttpLogger).setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE))
        .addNetworkInterceptor(InterceptorOkHttpCache())
        .addNetworkInterceptor(InterceptorOkHttpHeader())
        .pingInterval(10, TimeUnit.SECONDS)
        .connectTimeout(timeout = 30, unit = TimeUnit.SECONDS)
        .readTimeout(timeout = 30, unit = TimeUnit.SECONDS)
        .writeTimeout(timeout = 30, unit = TimeUnit.SECONDS)
        .retryOnConnectionFailure(retryOnConnectionFailure = true)
        .cache(mOkHttpCache)
        .build()

    private val mGithub: INetworkGithub = Retrofit.Builder()
        .baseUrl(INetworkGithub.BASE_URL_PROXY)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(INetworkGithub::class.java)

    override val okHttpClient: OkHttpClient
        get() = mOkHttpClient

    override val github: INetworkGithub
        get() = mGithub

    /**
     * Okhttp拦截器：本地缓存拦截器
     */
    private class InterceptorOkHttpCache : Interceptor {
        companion object {
            const val CACHE_QUERY_NAME = INetworkManager.CACHE_QUERY_NAME
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val oldRequest: Request = chain.request()
            val newRequest: Request.Builder = chain.request().newBuilder()

            if (HttpMethod.invalidatesCache(oldRequest.method)) {
                return chain.proceed(oldRequest)
            }

            if (oldRequest.url.queryParameter(name = CACHE_QUERY_NAME).isNullOrBlank()) {
                return chain.proceed(oldRequest)
            }

            // 强制使用缓存
            newRequest.cacheControl(CacheControl.FORCE_CACHE)

            // 获取返回状态信息
            val response: Response = chain.proceed(newRequest.build())

            // 如果返回的不成功，将不进行缓存
            if (response.code !in 200..399)
                return response

            // 重新赋值到使用缓存链接
            val cacheUrl: HttpUrl = oldRequest.url.newBuilder().removeAllQueryParameters(name = CACHE_QUERY_NAME).addQueryParameter(name = CACHE_QUERY_NAME, value = "true").build()
            newRequest.url(cacheUrl)

            // 强行增加缓存信息
            return response.newBuilder()
                .removeHeader(name = "Pragma")
                .removeHeader(name = "Cache-Control")
                .addHeader(name = "Cache-Control", value = "public, max-age=${TimeUnit.DAYS.toSeconds(1)}")
                .request(newRequest.build())
                .build()
        }
    }

    /**
     * Okhttp拦截器：全局插入Header对象
     * @date   2020-04-08
     */
    private class InterceptorOkHttpHeader : Interceptor, KoinComponent {
        private val mUserAgent: String = WebSettings.getDefaultUserAgent(get())
        private val mDeviceId: String = get<IRuntimeManager>().deviceId
        override fun intercept(chain: Interceptor.Chain): Response {
            val builderOld: Request.Builder = chain.request().newBuilder()
            builderOld.removeHeader("User-Agent").addHeader("User-Agent", mUserAgent)
            builderOld.addHeader("x-app-package-name", BuildConfig.APPLICATION_ID)
            builderOld.addHeader("x-app-version-name", BuildConfig.VERSION_NAME)
            builderOld.addHeader("x-app-version-code", BuildConfig.VERSION_CODE.toString())
            builderOld.addHeader("x-device-id", mDeviceId)
            return chain.proceed(builderOld.build())
        }
    }
}