package com.azimolabs.conditionwatcher

/**
 * Created by F1sherKK on 08/10/15.
 */
class ConditionWatcher private constructor() {

    private var timeoutLimit = DEFAULT_TIMEOUT_LIMIT
    private var watchInterval = DEFAULT_INTERVAL

    companion object {
        val CONDITION_NOT_MET = 0
        val CONDITION_MET = 1
        val TIMEOUT = 2

        val DEFAULT_TIMEOUT_LIMIT = 1000 * 60
        val DEFAULT_INTERVAL = 250

        private var conditionWatcher: ConditionWatcher? = null

        val instance: ConditionWatcher
            get() {
                if (conditionWatcher == null) {
                    conditionWatcher = ConditionWatcher()
                }
                return conditionWatcher!!
            }

        /**
         * Wait for a condition such as a view to be visible.
         *
         * @param instruction The instruction that determines the condition to be met
         * @param conditionUnmetCallback A block to run each time the condition is unmet such as
         * pull down to refresh.
         */
        @Throws(Exception::class)
        @JvmStatic
        fun waitForCondition(instruction: Instruction, conditionWatcherCallback: ConditionWatcherCallback?) {
            var status = CONDITION_NOT_MET
            var elapsedTime = 0

            do {
                if (instruction.checkCondition()) {
                    status = CONDITION_MET
                } else {
                    elapsedTime += instance.watchInterval
                    Thread.sleep(instance.watchInterval.toLong())
                    conditionWatcherCallback?.conditionUnmet()
                }

                if (elapsedTime >= instance.timeoutLimit) {
                    status = TIMEOUT
                    break
                }
            } while (status != CONDITION_MET)

            if (status == TIMEOUT)
                throw Exception(instruction.description + " - took more than " + instance.timeoutLimit / 1000 + " seconds. Test stopped.")
        }

        fun setWatchInterval(watchInterval: Int) {
            instance.watchInterval = watchInterval
        }

        fun setTimeoutLimit(ms: Int) {
            instance.timeoutLimit = ms
        }
    }
}

interface ConditionWatcherCallback {
    fun conditionUnmet()
}
