package io.github.zkit.managers.impls.lua

import org.luaj.vm2.lib.jse.LuajavaLib

internal class LuaLibJava: LuajavaLib() {

    override fun classForName(name: String): Class<*> {
        val classLoader: ClassLoader? = LuaLibJava::class.java.classLoader
        return Class.forName(name, true, classLoader)
    }
}