package io.github.zkit.beans

import android.view.View
import com.google.gson.annotations.SerializedName
import io.github.zkit.beans.enums.EnumWatermarkType
import io.github.zkit.constants.ConstantApp
import org.luaj.vm2.LuaTable

internal data class BeanWatermarkConfig(
    @SerializedName(value = "title")
    val title: String,

    @SerializedName(value = "value")
    var value: String = ConstantApp.EMPTY_STRING,

    @SerializedName(value = "type")
    val type: EnumWatermarkType = EnumWatermarkType.Text,

    @SerializedName(value = "visibility")
    var visibility: Int = View.VISIBLE,


    @SerializedName(value = "default")
    val default: String = ConstantApp.EMPTY_STRING
) {

    companion object {

        /**
         * 不允许控制显示状态
         */
        const val VISIBLE_DISABLED = -1

        /**
         *
         */
        fun new(luaTable: LuaTable): BeanWatermarkConfig {
            return BeanWatermarkConfig(
                title = luaTable["title"].checkjstring(),
                type = EnumWatermarkType.values().firstOrNull { it.value == luaTable["type"].checkjstring() } ?: EnumWatermarkType.Text,
                visibility = luaTable["visibility"].checkint(),
                default = luaTable["default"].checkjstring()
            )
        }

    }

}
