package io.github.zkit.managers.impls

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.android.LogcatAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize
import io.github.zkit.BuildConfig
import io.github.zkit.commons.loggers.LoggerAppCenter
import io.github.zkit.commons.loggers.LoggerKoin
import io.github.zkit.managers.ILoggerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.compress.archivers.zip.Zip64Mode
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.io.FileUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileFilter

internal class ImplLoggerManager : ILoggerManager, KoinComponent {

    private val mContext: Context by inject()
    override suspend fun init() = withContext(context = Dispatchers.IO) {
        val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.reset()

        val logger: Logger = loggerContext.getLogger("ROOT")
        logger.addAppender(initSlf4JLogCat())
        logger.addAppender(initSlf4JFile())

        (LoggerFactory.getLogger(LoggerKoin::class.java) as Logger).level = ch.qos.logback.classic.Level.OFF
        (LoggerFactory.getLogger(LoggerAppCenter::class.java) as Logger).level = ch.qos.logback.classic.Level.OFF

        logger.level = ch.qos.logback.classic.Level.DEBUG
        logger.isAdditive = false
        return@withContext
    }

    private fun initSlf4JLogCat(): LogcatAppender {
        val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

        val patternLayout = PatternLayoutEncoder()
        patternLayout.context = loggerContext
        patternLayout.pattern = "\\(%F:%L\\) SLF4J [%thread] %m%n"
        patternLayout.charset = Charsets.UTF_8
        patternLayout.start()

        val logcatAppender = LogcatAppender()
        logcatAppender.context = loggerContext
        logcatAppender.encoder = patternLayout
        logcatAppender.start()
        return logcatAppender
    }

    private fun initSlf4JFile(): RollingFileAppender<ILoggingEvent> {
        val cacheRootDir: String = get<Context>().externalCacheDir!!.absolutePath
        val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

        val patternLayout = PatternLayoutEncoder()
        patternLayout.context = loggerContext
        patternLayout.pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%-27thread] [%logger{36}:%L] - %m%n"
        patternLayout.charset = Charsets.UTF_8
        patternLayout.start()

        val rollingFileAppender = RollingFileAppender<ILoggingEvent>()
        rollingFileAppender.context = loggerContext
        rollingFileAppender.encoder = patternLayout
        rollingFileAppender.isAppend = true
        rollingFileAppender.isPrudent = false
        rollingFileAppender.start()

        val timeBasedRollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>()
        timeBasedRollingPolicy.context = loggerContext
        timeBasedRollingPolicy.fileNamePattern = "$cacheRootDir/logs/%d{yyyy-MM-dd}.log"
        timeBasedRollingPolicy.maxHistory = 7
        timeBasedRollingPolicy.setTotalSizeCap(FileSize.valueOf("200MB"))
        timeBasedRollingPolicy.isCleanHistoryOnStart = true
        timeBasedRollingPolicy.setParent(rollingFileAppender)
        timeBasedRollingPolicy.start()

        rollingFileAppender.rollingPolicy = timeBasedRollingPolicy
        rollingFileAppender.start()

        return rollingFileAppender
    }

    override suspend fun zip(): Uri = withContext(context = Dispatchers.IO) {
        val cacheRootDir: File = get<Context>().externalCacheDir!!
        val sharedRootDir: File = get<Context>().externalCacheDir!!.resolve(relative = "shared")
        val logDirFile: File = cacheRootDir.resolve(relative = "logs")
        val outputFile = File(sharedRootDir, "logs.zip")
        if (outputFile.exists()) {
            outputFile.delete()
        }
        FileUtils.forceMkdir(outputFile.parentFile)

        val zipArchiveOutputStream = ZipArchiveOutputStream(outputFile)
        zipArchiveOutputStream.setUseZip64(Zip64Mode.AsNeeded)
        zipArchiveOutputStream.encoding = "UTF-8"

        logDirFile
            .listFiles(FileFilter { it.extension == ".log" })
            ?.forEach { logFile ->
                val zipArchiveEntry = ZipArchiveEntry(logFile, logFile.name)
                zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry)
                zipArchiveOutputStream.write(logFile.readBytes())
                zipArchiveOutputStream.closeArchiveEntry()
            }
        zipArchiveOutputStream.finish()
        zipArchiveOutputStream.close()

        return@withContext FileProvider.getUriForFile(mContext, "${BuildConfig.APPLICATION_ID}.providers.file", outputFile)
    }
}