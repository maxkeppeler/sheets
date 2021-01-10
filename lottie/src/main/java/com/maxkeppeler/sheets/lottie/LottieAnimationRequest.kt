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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.maxkeppeler.sheets.lottie

import android.animation.ValueAnimator
import android.view.animation.Animation.RESTART
import android.view.animation.Animation.REVERSE
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.RawRes
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.Serializable

/**
 * Wrapper class of the LottieAnimationView class methods to offer basic functionality.
 * @see <a href="https://github.com/airbnb/lottie-android">Lottie</a>
 */
class LottieAnimationRequest internal constructor() : Serializable {

    companion object {

        /**
         * When the animation reaches the end and `repeatCount` is INFINITE
         * or a positive value, the animation restarts from the beginning.
         */
        const val RESTART = ValueAnimator.RESTART

        /**
         * When the animation reaches the end and `repeatCount` is INFINITE
         * or a positive value, the animation reverses direction on every iteration.
         */
        const val REVERSE = ValueAnimator.REVERSE

        /**
         * This value is used with the [.setRepeatCount] property to repeat
         * the animation indefinitely.
         */
        const val INFINITE = ValueAnimator.INFINITE
    }

    @IntDef(RESTART, REVERSE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RepeatMode

    @RawRes
    internal var animationResId: Int? = null

    @DrawableRes
    internal var fallbackDrawableRes: Int? = null
    internal var animationName: String? = null
    internal var animationUrl: String? = null
    internal var animationUrlWithCacheKey: Pair<String?, String?>? = null
    internal var animation: Pair<InputStream?, String?>? = null
    internal var startFrame: Int? = null
    internal var endFrame: Int? = null
    internal var startFrameName: String? = null
    internal var endFrameName: String? = null
    internal var startProgress: Float? = null
    internal var endProgress: Float? = null
    internal var minMaxFrameName: String? = null
    internal var minMaxFrame: Pair<Int, Int>? = null
    internal var speed: Float? = null

    internal var repeatMode: Int? = null
    internal var repeatCount: Int? = null

    /**
     * Sets the animation from a file in the raw directory.
     * This will load and deserialize the file asynchronously.
     */
    fun setAnimation(@RawRes rawRes: Int) {
        this.animationResId = rawRes
        this.animationName = null
    }

    /**
     * Sets the animation from the name of a file in the raw directory.
     * This will load and deserialize the file asynchronously.
     */
    fun setAnimation(assetName: String) {
        this.animationName = assetName
        this.animationResId = null
    }

    /**
     * Sets the animation from json string. This is the ideal API to use when loading an animation
     * over the network because you can use the raw response body here and a conversion to a
     * JSONObject never has to be done.
     */
    fun setAnimationFromJson(jsonString: String, cacheKey: String?) {
        setAnimation(ByteArrayInputStream(jsonString.toByteArray()), cacheKey)
    }

    /**
     * Sets the animation from an arbitrary InputStream.
     * This will load and deserialize the file asynchronously.
     *
     *
     * This is particularly useful for animations loaded from the network. You can fetch the
     * bodymovin json from the network and pass it directly here.
     */
    fun setAnimation(stream: InputStream?, cacheKey: String?) {
        animation = Pair(stream, cacheKey)
    }

    /**
     * Load a lottie animation from a url. The url can be a json file or a zip file. Use a zip file if you have images. Simply zip them together and lottie
     * will unzip and link the images automatically.
     *
     * Under the hood, Lottie uses Java HttpURLConnection because it doesn't require any transitive networking dependencies. It will download the file
     * to the application cache under a temporary name. If the file successfully parses to a composition, it will rename the temporary file to one that
     * can be accessed immediately for subsequent requests. If the file does not parse to a composition, the temporary file will be deleted.
     */
    fun setAnimationFromUrl(url: String?) {
        this.animationUrl = url
    }

    /**
     * Load a lottie animation from a url. The url can be a json file or a zip file. Use a zip file if you have images. Simply zip them together and lottie
     * will unzip and link the images automatically.
     *
     * Under the hood, Lottie uses Java HttpURLConnection because it doesn't require any transitive networking dependencies. It will download the file
     * to the application cache under a temporary name. If the file successfully parses to a composition, it will rename the temporary file to one that
     * can be accessed immediately for subsequent requests. If the file does not parse to a composition, the temporary file will be deleted.
     */
    fun setAnimationFromUrl(url: String?, cacheKey: String?) {
        this.animationUrlWithCacheKey = Pair(url, cacheKey)
    }

    /**
     * Set a drawable that will be rendered if the LottieComposition fails to load for any reason.
     * Unless you are using {@link #setAnimationFromUrl(String)}, this is an unexpected error and
     * you should handle it with {@link #setFailureListener(LottieListener)}.
     *
     * If this is a network animation, you may use this to show an error to the user or
     * you can use a failure listener to retry the download.
     */
    fun setFallbackResource(@DrawableRes fallbackResource: Int) {
        this.fallbackDrawableRes = fallbackResource
    }

    /**
     * Sets the minimum frame that the animation will start from when playing or looping.
     */
    fun setMinFrame(startFrame: Int) {
        this.startFrame = startFrame
    }

    /**
     * Sets the minimum progress that the animation will start from when playing or looping.
     */
    fun setMinProgress(startProgress: Float) {
        this.startProgress = startProgress
    }

    /**
     * Sets the maximum frame that the animation will end at when playing or looping.
     *
     * The value will be clamped to the composition bounds. For example, setting Integer.MAX_VALUE would result in the same
     * thing as composition.endFrame.
     */
    fun setMaxFrame(endFrame: Int) {
        this.endFrame = endFrame
    }

    /**
     * Sets the maximum progress that the animation will end at when playing or looping.
     */
    fun setMaxProgress(@FloatRange(from = 0.0, to = 1.0) endProgress: Float) {
        this.endProgress = endProgress
    }

    /**
     * Sets the minimum frame to the start time of the specified marker.
     */
    fun setMinFrame(markerName: String?) {
        this.startFrameName = markerName
    }

    /**
     * Sets the maximum frame to the start time + duration of the specified marker.
     */
    fun setMaxFrame(markerName: String?) {
        this.endFrameName = markerName
    }

    /**
     * Sets the minimum and maximum frame to the start time and start time + duration
     * of the specified marker.
     */
    fun setMinAndMaxFrame(markerName: String?) {
        this.minMaxFrameName = markerName
    }

    /**
     * @see .setMinFrame
     * @see .setMaxFrame
     */
    fun setMinAndMaxFrame(minFrame: Int, maxFrame: Int) {
        this.minMaxFrame = Pair(minFrame, maxFrame)
    }

    /* Sets the playback speed. If speed &lt; 0, the animation will play backwards.
    */
    fun setSpeed(speed: Float) {
        this.speed = speed
    }

    /**
     * Defines what this animation should do when it reaches the end. This
     * setting is applied only when the repeat count is either greater than
     * 0 or [INFINITE]. Defaults to [RESTART].
     *
     * @param mode [RESTART] or [REVERSE]
     */
    fun setRepeatMode(@RepeatMode mode: Int) {
        this.repeatMode = mode
    }

    /**
     * Sets how many times the animation should be repeated. If the repeat
     * count is 0, the animation is never repeated. If the repeat count is
     * greater than 0 or [INFINITE], the repeat mode will be taken
     * into account. The repeat count is 0 by default.
     *
     * @param count the number of times the animation should be repeated
     */
    fun setRepeatCount(count: Int) {
        this.repeatCount = count
    }
}