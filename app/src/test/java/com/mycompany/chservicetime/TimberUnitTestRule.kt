package com.mycompany.chservicetime

import android.util.Log
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A JUnit {@link TestRule} that plants a {@link Timber.Tree} before a test is executed,
 * and uproots the {@link Timber.Tree} afterwards.
 * <p>
 * This is useful as a planted {@link Timber.Tree} would exist between test classes, which may be
 * undesirable.
 */
class TimberUnitTestRule(val rules: Rules) : TestRule {

    override fun apply(base: Statement, description: Description?): Statement {
        return TimberStatement(base, rules)
    }

    companion object {

        /**
         * @return a [Rules] class which is used as a builder to create a [TimberTestRule].
         */
        fun builder() = Rules()

        /**
         * @return a [TimberTestRule] that logs messages of any priority regardless of the
         * test outcome
         */
        fun logAllAlways() = Rules()
            .onlyLogWhenTestFails(false)
            .build()

        /**
         * @return a [TimberTestRule] that logs all messages, regardless of their priority.
         */
        fun logAllWhenTestFails() = Rules()
            .defaultRules()
            .build()

        /**
         * @return a [TimberTestRule] that only logs error messages regardless of the test
         * outcome.
         */
        fun logErrorsAlways() = Rules()
            .onlyLogWhenTestFails(false)
            .minPriority(Log.ERROR)
            .build()

        /**
         * @return a [TimberTestRule] that only logs error messages when a unit test fails.
         */
        fun logErrorsWhenTestFails() = Rules()
            .minPriority(Log.ERROR)
            .build()
    }
}

/**
 * Defines a set of rules in which the [TimberTestRule]'s internal Timber tree must
 * adhere to when intercepting log messages.
 *
 *
 * The types of rules applied are:
 *
 *  1. Min priority - What is the lowest level of log type that should be logged.
 *  1. Show thread - Whether the Thread ID and name should be logged.
 *  1. Show timestamp - Whether the current time should be logged.
 *
 */
class Rules(
    var mMinPriority: Int = 0,
    var mShowThread: Boolean = false,
    var mShowTimestamp: Boolean = false,
    var mOnlyLogWhenTestFails: Boolean = false
) {
    fun defaultRules() = apply {
        this.mMinPriority = Log.VERBOSE
        this.mShowThread = false
        this.mShowTimestamp = true
        this.mOnlyLogWhenTestFails = true
    }

    /**
     * Defines what the lowest level of log type that should be logged.
     *
     *
     * This can be:
     *
     *  1. [Log.VERBOSE]
     *  1. [Log.DEBUG]
     *  1. [Log.INFO]
     *  1. [Log.WARN]
     *  1. [Log.ERROR]
     *
     *
     * @param minPriority the Android log priority.
     * @return the mutated [Rules]
     */
    fun minPriority(minPriority: Int) = apply {
        this.mMinPriority = minPriority
    }

    /**
     * Defines whether the Thread ID and name should be logged
     *
     * @param showThread whether the thread details are shown.
     * @return the mutated [Rules]
     */
    fun showThread(showThread: Boolean) = apply {
        this.mShowThread = showThread
    }

    /**
     * Defines whether the timestamp should be logged
     *
     * @param showTimestamp whether the timestamp is shown.
     * @return the mutated [Rules]
     */
    fun showTimestamp(showTimestamp: Boolean) = apply {
        this.mShowTimestamp = showTimestamp
    }

    /**
     * Defines whether the logs are only output if the unit test fails.
     *
     * @param onlyLogWhenTestFails whether the logs are only output when a test fails.
     * @return the mutated [Rules]
     */
    fun onlyLogWhenTestFails(onlyLogWhenTestFails: Boolean) = apply {
        this.mOnlyLogWhenTestFails = onlyLogWhenTestFails
    }

    /**
     * Builds the JUnit test rule based on the defined rules.
     *
     * @return a new JUnit test rule instance.
     */
    fun build() =
        TimberUnitTestRule(Rules(mMinPriority, mShowThread, mShowTimestamp, mOnlyLogWhenTestFails))
}

/**
 * The JUnit statement that plants before the unit test, and uproots it after completion.
 */
private class TimberStatement internal constructor(private val mNext: Statement, rules: Rules) :
    Statement() {
    private val mTree: BufferedJUnitTimberTree = BufferedJUnitTimberTree(rules)

    @Throws(Throwable::class)
    override fun evaluate() {
        Timber.plant(mTree)
        try {
            mNext.evaluate()
        } catch (t: Throwable) {
            mTree.flushLogs()
            throw t
        } finally {
            // Ensure the tree is removed to avoid duplicate logging.
            Timber.uproot(mTree)
        }
    }
}

/**
 * A Timber tree that logs to the Java System.out rather than using the Android logger.
 */
private class BufferedJUnitTimberTree internal constructor(private val mRules: Rules) :
    Timber.DebugTree() {
    private val mLogMessageBuffer: MutableList<String> = ArrayList()
    private val bufferLock = Any()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logMessage = createLogMessage(mRules, priority, tag, message) ?: return

        if (mRules.mOnlyLogWhenTestFails) {
            synchronized(bufferLock) {
                mLogMessageBuffer.add(logMessage)
            }
        } else {
            println(logMessage)
        }
    }

    /**
     * Flushes all the previously stored log messages.
     */
    fun flushLogs() {
        synchronized(bufferLock) {
            val iterator = mLogMessageBuffer.iterator()
            while (iterator.hasNext()) {
                println(iterator.next())
                iterator.remove()
            }
        }
    }

    /**
     * Creates a log message based on the rules and Timber log details.
     *
     * @param rules    the rules used to construct the message.
     * @param priority the priority of the log.
     * @param tag      the tag of the log.
     * @param message  the message of the log.
     * @return a log message (may be null).
     */
    private fun createLogMessage(
        rules: Rules,
        priority: Int,
        tag: String?,
        message: String
    ): String? {
        // Avoid logging if the priority is too low.
        if (priority < rules.mMinPriority) {
            return null
        }

        // Obtain the correct log type prefix.
        val type: Char
        when (priority) {
            Log.VERBOSE -> type = 'V'

            Log.DEBUG -> type = 'D'

            Log.INFO -> type = 'I'

            Log.WARN -> type = 'W'

            Log.ERROR -> type = 'E'
            else -> type = 'E'
        }

        val logBuilder = StringBuilder()

        if (rules.mShowTimestamp) {
            logBuilder
                .append(
                    THREAD_LOCAL_FORMAT.get()?.format(System.currentTimeMillis()) ?: "Time invalid"
                )
                .append(" ")
        }

        if (rules.mShowThread) {
            val thread = Thread.currentThread()
            logBuilder
                .append(thread.id)
                .append("/")
                .append(thread.name)
                .append(" ")
        }

        logBuilder
            .append(type)
            .append("/")
            .append(tag)
            .append(": ")
            .append(message)

        return logBuilder.toString()
    }
}

/**
 * A thread local is used as the [DateFormat] class is not thread-safe.
 */
private val THREAD_LOCAL_FORMAT = object : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat {
        return SimpleDateFormat("HH:mm:ss:SSSSSSS", Locale.ENGLISH)
    }
}
