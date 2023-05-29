package io.github.zkit.managers.impls.lua

import com.qmuiteam.qmui.util.QMUIDisplayHelper
import io.github.zkit.managers.IRuntimeManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

internal class LuaLibAndroid : TwoArgFunction(), KoinComponent {

    private val mLogger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override fun call(modName: LuaValue, env: LuaValue): LuaValue {
        val table = LuaTable()
        table.set("log", mLuaLog)
        table.set("currentTimeMillis", mLuaCurrentTimeMillis)
        table.set("deviceId", get<IRuntimeManager>().deviceId)
        table.set("dp2px", mLuaDp2px)
        table.set("sp2px", mLuaSp2px)

        env.set("Android", table)
        env.get("package").get("loaded").set("Android", table)
        return table
    }

    private val mLuaLog = object : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            mLogger.info(arg.toString())
            return LuaValue.NIL
        }
    }

    private val mLuaCurrentTimeMillis = object : ZeroArgFunction() {
        override fun call(): LuaValue {
            return LuaValue.valueOf(System.currentTimeMillis().toInt())
        }
    }

    private val mLuaDp2px = object : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return LuaValue.valueOf(QMUIDisplayHelper.dp2px(get(), arg.checkint()))
        }
    }

    private val mLuaSp2px = object : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            return LuaValue.valueOf(QMUIDisplayHelper.sp2px(get(), arg.checkint()))
        }
    }
}