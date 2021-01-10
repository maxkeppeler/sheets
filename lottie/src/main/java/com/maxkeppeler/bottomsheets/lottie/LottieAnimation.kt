/*
 * Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

@file:Suppress("unused")

package com.maxkeppeler.bottomsheets.lottie

import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.maxkeppeler.bottomsheets.core.ImageSource
import java.io.Serializable

/** Used to apply image request settings. */
typealias AnimationBuilder = LottieAnimationRequest.() -> Unit

/** Convenience alias for lottie's animation view config builder. */
private typealias LottieAnimationViewBuilder = LottieAnimationView.() -> Unit

/**
 * A class that holds the animation configurations.
 */
class LottieAnimation private constructor() : ImageSource(), Serializable {

    private var builder: AnimationBuilder? = null

    internal val lottieAnimationViewBuilder: LottieAnimationViewBuilder
        get() = {
            // By default restart animation infinite times
            repeatMode = LottieDrawable.RESTART
            repeatCount = LottieDrawable.INFINITE

            val request = LottieAnimationRequest().apply { builder?.invoke(this) }
            request.animationResId?.let { setAnimation(it) }
            request.animationName?.let { setAnimation(it) }
            request.animationUrl?.let { setAnimationFromUrl(it) }
            request.animationUrlWithCacheKey?.let { setAnimationFromUrl(it.first, it.second) }
            request.animation?.let { setAnimation(it.first, it.second) }
            request.startFrame?.let { setMinFrame(it) }
            request.endFrame?.let { setMaxFrame(it) }
            request.startFrameName?.let { setMinFrame(it) }
            request.endFrameName?.let { setMaxFrame(it) }
            request.startProgress?.let { setMinProgress(it) }
            request.endProgress?.let { setMaxProgress(it) }
            request.minMaxFrameName?.let { setMinAndMaxFrame(it) }
            request.minMaxFrame?.let { setMinAndMaxFrame(it.first, it.second) }
            request.speed?.let { speed = it }
            request.repeatMode?.let { repeatMode = it }
            request.repeatCount?.let { repeatCount = it }
        }

    constructor(
        ratio: Ratio? = null,
        scaleType: ImageView.ScaleType? = null,
        builder: AnimationBuilder,
    ) : this() {
        this.ratio = ratio
        this.scaleType = scaleType
        this.builder = builder
    }

    constructor(builder: AnimationBuilder) : this() {
        this.builder = builder
    }

}