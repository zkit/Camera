package io.github.zkit.managers

import io.github.zkit.managers.impls.retrofit.INetworkGithub

internal interface INetworkManager {

    companion object {
        const val CACHE_QUERY_NAME = "__is_use_local_cache__"
    }

    val okHttpClient: okhttp3.OkHttpClient

    val github: INetworkGithub
}