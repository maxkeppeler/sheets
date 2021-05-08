/*
 *  Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

@file:Suppress("unused")

package com.maxkeppeler.sheets.input.type

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.maxkeppeler.sheets.input.InputBehavior

/** Listener that is invoked when the view is ready. */
typealias InputCustomViewListener = (view: View) -> Unit

/** Listener that is invoked when the data of the input changed. */
typealias InputCustomDataChangeListener = () -> Unit

/**
 * Input of a unknown type displayed in a custom view.
 */
class InputCustom(key: String? = null, func: InputCustom.() -> Unit) : Input(key) {

    init {
        func()
    }

    internal var viewListener: InputCustomViewListener? = null
    internal var dataChangeListener: InputCustomDataChangeListener? = null
    internal var viewRes: Int? = null
    internal var view: View? = null
    private var behavior: InputBehavior? = null
    internal var fullView: Boolean = false

    /** Set the behavior. */
    fun behavior(behavior: InputBehavior) {
        this.behavior = behavior
    }

    /** Set the behavior. */
    inline fun behavior(
        crossinline onResult: () -> Unit = { },
        crossinline onValidate: () -> Boolean = { true },
        crossinline onSave: (bundle: Bundle, index: Int) -> Unit = { _, _ -> },
    ) = behavior(object : InputBehavior {
        override fun onResult() = onResult.invoke()
        override fun onValidate(): Boolean = onValidate.invoke()
        override fun onSave(bundle: Bundle, index: Int) = onSave.invoke(bundle, index)
    })

    /** Set the view. */
    fun view(@LayoutRes layout: Int, listener: InputCustomViewListener? = null) {
        this.viewRes = layout
        this.viewListener = listener
        this.view = null
    }

    /** Set the view. */
    fun view(view: View, listener: InputCustomViewListener? = null) {
        this.view = view
        this.viewListener = listener
        this.viewRes = null
    }

    /** The custom view will be take up the full space of the input and hide the label, content and icon. */
    fun fullView(fullView: Boolean) {
        this.fullView = fullView
    }

    /** Notify the validation process about the changed input data. */
    fun notifyInputDataChange() {
        this.dataChangeListener?.invoke()
    }

    override fun invokeResultListener() {
        behavior?.onResult()
    }

    override fun valid(): Boolean =
        behavior?.onValidate() ?: true

    override fun putValue(bundle: Bundle, index: Int) {
        behavior?.onSave(bundle, index)
    }
}