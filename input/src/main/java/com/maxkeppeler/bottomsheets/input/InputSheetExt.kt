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

package com.maxkeppeler.bottomsheets.input

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RestrictTo
import androidx.appcompat.widget.AppCompatEditText

/** Add a [TextWatcher] to the [AppCompatEditText]. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun AppCompatEditText.addTextWatcher(textChanged: ((String) -> Unit)? = null): TextWatcher {
    var timer: CountDownTimer? = null
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(editable: Editable?) {
            timer?.cancel()
            timer = object : CountDownTimer(300, 100) {
                override fun onTick(millisUntilFinished: Long) = Unit
                override fun onFinish() {
                    textChanged?.invoke(editable.toString())
                }
            }.start()
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}