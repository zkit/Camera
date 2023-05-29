package io.github.zkit.commons.loggers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.logging.Level

internal class LoggerAppCenter : java.util.logging.Logger("AppCenter", null) {
    private val mLogger: Logger = LoggerFactory.getLogger(this.javaClass)
    override fun log(level: Level, msg: String?) {
        super.log(level, msg)
        when (level.intValue()) {
            Level.ALL.intValue()     -> mLogger.trace("LOG:LoggerAppCenter:log level={}, msg={}", level, msg)
            Level.FINE.intValue()    -> mLogger.debug("LOG:LoggerAppCenter:log level={}, msg={}", level, msg)
            Level.INFO.intValue()    -> mLogger.info("LOG:LoggerAppCenter:log level={}, msg={}", level, msg)
            Level.WARNING.intValue() -> mLogger.warn("LOG:LoggerAppCenter:log level={}, msg={}", level, msg)
            Level.SEVERE.intValue()  -> mLogger.error("LOG:LoggerAppCenter:log level={}, msg={}", level, msg)
        }
    }
}