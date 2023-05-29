package io.github.zkit.extensions

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(json: JsonObject): T = fromJson(json, object : TypeToken<T>() {}.type)
