package io.github.zkit.ui.widgets.watermarks.mvp.contracts

import io.github.zkit.beans.interfaces.ILifecycleCoroutineScope
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue

internal interface IWatermarkBase: ILifecycleCoroutineScope {

    var onContentChangedCallback: LuaFunction?


}