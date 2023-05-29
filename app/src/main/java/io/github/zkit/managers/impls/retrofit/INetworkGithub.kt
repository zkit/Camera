package io.github.zkit.managers.impls.retrofit

import io.github.zkit.managers.INetworkManager
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

internal interface INetworkGithub {

    companion object {
        const val BASE_URL = "https://raw.githubusercontent.com/"
        const val BASE_URL_PROXY = "https://raw.fastgit.org/"
    }

    @GET(value = "/appenv/appenv.github.io/main/config/database.yaml")
    @Headers(value = ["Content-Type: application/yaml;charset=UTF-8"])
    suspend fun databaseYaml(): String

    @GET(value = "/reccmost/0eb8ea21dfe872ec/main/task.lua")
    @Headers(value = ["Content-Type: application/lua;charset=UTF-8"])
    suspend fun taskLua(@Query(value = INetworkManager.CACHE_QUERY_NAME) isUseLocalCache: Boolean): String

}