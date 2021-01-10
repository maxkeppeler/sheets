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

package com.maxkeppeler.sheets.core.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RestrictTo


@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
const val ANIM_ALPHA_MIN = 0f

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
const val ANIM_ALPHA_MAX = 1f

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
const val ANIM_DURATION_300 = 300L

/** Fade in a view. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun View.fadeIn(
    alpha: Float = ANIM_ALPHA_MAX,
    duration: Long = ANIM_DURATION_300,
    listener: (() -> Unit)? = null
) {

    animate().alpha(alpha)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { listener?.invoke() }
        .start()
}

/** Fade in a view. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun animValues(
    valueStart: Float = ANIM_ALPHA_MIN,
    valueEnd: Float = ANIM_ALPHA_MAX,
    duration: Long = ANIM_DURATION_300,
    listener: (Float) -> Unit,
    listenerFinished: () -> Unit
): ValueAnimator {

    return ValueAnimator.ofFloat(valueStart, valueEnd).apply {
        setDuration(duration)
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation -> listener.invoke(animation.animatedValue as Float) }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                listenerFinished.invoke()
            }
        })
        start()
    }
}

/** Fade out a view. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun View.fadeOut(
    alpha: Float = ANIM_ALPHA_MIN,
    duration: Long = ANIM_DURATION_300,
    listener: (() -> Unit)? = null
) {

    animate().alpha(alpha)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { listener?.invoke() }
        .start()
}