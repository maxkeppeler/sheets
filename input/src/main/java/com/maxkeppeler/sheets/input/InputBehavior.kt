package com.maxkeppeler.sheets.input

import android.os.Bundle

/**
 * Defines the behavior actions of an input.
 * This can be added to a [InputCustom]
 */
interface InputBehavior {

    /** Validate the data of the input. */
    fun onValidate(): Boolean

    /** Save the data in the bundle. */
    fun onSave(bundle: Bundle, index: Int)

    /** Notify about the result event. */
    fun onResult()
}
