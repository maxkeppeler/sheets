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

package com.maxkeppeler.sheets.lottie

import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.maxkeppeler.sheets.core.AddOnComponent
import com.maxkeppeler.sheets.core.Sheet
import com.maxkeppeler.sheets.core.TopStyle
import com.maxkeppeler.sheets.core.utils.clipTopCorners

private const val EXCEPTION_MESSAGE_NOT_SETUP = "AnimationView was not setup yet."

/** Set a cover lottie animation. */
fun Sheet.withCoverLottieAnimation(lottieAnimation: LottieAnimation) {
    val componentLottie: AddOnComponent = {
        useCover() /* Indicate to setup the top bar style. */
        addOnCreateViewListener { binding ->
            val coverContainer = binding.top.cover
            val coverImage = binding.top.coverImage
            val coverParams = coverImage.layoutParams as ViewGroup.LayoutParams
            val coverAnimation = LottieAnimationView(requireContext()).apply {
                setupCoverSource(lottieAnimation,  this)
                layoutParams = coverParams
                lottieAnimation.lottieAnimationViewBuilder.invoke(this)
                if (getTopStyle() != TopStyle.ABOVE_COVER) {
                    clipTopCorners(getActualCornerFamily(), getActualCornerRadius())
                }
            }
            coverContainer.removeView(coverImage)
            coverContainer.addView(coverAnimation)
            setCoverAnimationView(coverAnimation)
            coverAnimation.playAnimation()
        }
    }
    componentLottie()
    addAddOnComponent(componentLottie)
}

/**
 * Play the cover animation.
 * @throws IllegalStateException If animation view was not setup yet.
 */
fun Sheet.playCoverAnimation() {
    getCoverLottieAnimationView().playAnimation()
}

/**
 * Resume the cover animation.
 * @throws IllegalStateException If animation view was not setup yet.
 */
fun Sheet.resumeCoverAnimation() {
    getCoverLottieAnimationView().playAnimation()
}

/**
 * Pause the cover animation.
 * @throws IllegalStateException If animation view was not setup yet.
 */
fun Sheet.pauseCoverAnimation() {
    getCoverLottieAnimationView().pauseAnimation()
}

/**
 * Cancel the cover animation.
 * @throws IllegalStateException If animation view was not setup yet.
 */
fun Sheet.cancelCoverAnimation() {
    getCoverLottieAnimationView().cancelAnimation()
}

private fun Sheet.getCoverLottieAnimationView(): LottieAnimationView {
   return getCoverAnimationView<LottieAnimationView>()
        ?: throw IllegalStateException(EXCEPTION_MESSAGE_NOT_SETUP)
}