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
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


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
    listener: (() -> Unit)? = null,
) {

    animate().alpha(alpha)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { listener?.invoke() }
        .start()
}

/** Fade out a view. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun View.fadeOut(
    alpha: Float = ANIM_ALPHA_MIN,
    duration: Long = ANIM_DURATION_300,
    listener: (() -> Unit)? = null,
) {

    animate().alpha(alpha)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { listener?.invoke() }
        .start()
}

/**
 * Lifecycle aware ValueAnimator wrapper.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ValueAnimationListener(
    private val lifecycle: Lifecycle,
    private val valueStart: Float = ANIM_ALPHA_MIN,
    private val valueEnd: Float = ANIM_ALPHA_MAX,
    private val duration: Long = ANIM_DURATION_300,
    private val interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    private val updateListener: (Float) -> Unit,
    private val endListener: () -> Unit,
) : LifecycleObserver {

    private var valueAnimator: ValueAnimator? = null

    private val animatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
            updateListener.invoke(animation.animatedValue as Float)
    }

    private val animatorEndListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                endListener.invoke()
        }
    }

    init {
        start()
    }

    private fun start() {
        valueAnimator = ValueAnimator.ofFloat(valueStart, valueEnd)
        valueAnimator?.addUpdateListener(animatorUpdateListener)
        valueAnimator?.addListener(animatorEndListener)
        valueAnimator?.interpolator = interpolator
        valueAnimator?.duration = duration
        valueAnimator?.start()
    }

    fun cancel() {
        valueAnimator?.removeListener(animatorEndListener)
        valueAnimator?.removeUpdateListener(animatorUpdateListener)
        valueAnimator?.cancel()
        valueAnimator = null
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() = Unit

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() = cancel()
}
