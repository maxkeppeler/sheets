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

import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.maxkeppeler.bottomsheets.core.AddOnComponent
import com.maxkeppeler.bottomsheets.core.BottomSheet

private const val EXCEPTION_MESSAGE_NOT_SETUP = "AnimationView was not setup yet."

/** Set a cover lottie animation. */
fun BottomSheet.withCoverLottieAnimation(lottieAnimation: LottieAnimation) {
    val componentLottie: AddOnComponent = {
        useCover() /* Indicate to setup the top bar style. */
        addOnCreateViewListener { binding ->
            setupCoverSource(lottieAnimation)
            val topView = binding.top.root
            val coverImage = binding.top.cover
            val coverParams = coverImage.layoutParams as ConstraintLayout.LayoutParams
            val coverAnimation = LottieAnimationView(requireContext()).apply {
                layoutParams =
                    coverParams /* Apply cover image params, to keep constraints working. */
                lottieAnimation.lottieAnimationViewBuilder.invoke(this)
            }
            topView.removeView(coverImage)
            topView.addView(coverAnimation)
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
fun BottomSheet.playCoverAnimation() {
    getCoverLottieAnimationView().playAnimation()
}

/**
 * Resume the cover animation.
 * @throws IllegalStateException If animation view was not setup yet.
 */
fun BottomSheet.resumeCoverAnimation() {
    getCoverLottieAnimationView().playAnimation()
}

/**
 * Pause the cover animation.
 * @throws IllegalStateException If animation view was not setup yet.
 */
fun BottomSheet.pauseCoverAnimation() {
    getCoverLottieAnimationView().pauseAnimation()
}

/**
 * Cancel the cover animation.
 * @throws IllegalStateException If animation view was not setup yet.
 */
fun BottomSheet.cancelCoverAnimation() {
    getCoverLottieAnimationView().cancelAnimation()
}

private fun BottomSheet.getCoverLottieAnimationView(): LottieAnimationView {
    return (getCoverView() as? LottieAnimationView)
        ?: throw IllegalStateException(EXCEPTION_MESSAGE_NOT_SETUP)
}