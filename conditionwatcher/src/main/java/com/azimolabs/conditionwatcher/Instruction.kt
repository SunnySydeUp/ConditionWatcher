package com.azimolabs.conditionwatcher

import android.os.Bundle

/**
 * Created by F1sherKK on 16/12/15.
 */
abstract class Instruction {

    var dataContainer = Bundle()
        private set

    abstract val description: String

    fun setData(dataContainer: Bundle) {
        this.dataContainer = dataContainer
    }

    abstract fun checkCondition(): Boolean
}
