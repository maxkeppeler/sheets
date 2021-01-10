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

@file:Suppress("unused", "EXPERIMENTAL_API_USAGE")

package com.maxkeppeler.sheets.core

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import coil.ImageLoader
import coil.drawable.CrossfadeDrawable
import coil.size.PixelSize
import coil.size.Scale
import coil.size.Size
import coil.size.SizeResolver
import coil.transition.CrossfadeTransition
import coil.transition.Transition
import java.io.Serializable

/**
 * Wrapper class of the ImageRequest class of coil to offer basic functionality to the image loading.
 */
class ImageRequest internal constructor() : Serializable {

    internal var scale: Scale? = null
    internal var size: SizeResolver? = null

    internal var placeholderResId: Int? = null
    internal var placeholderDrawable: Drawable? = null
    internal var errorResId: Int? = null
    internal var errorDrawable: Drawable? = null
    internal var fallbackResId: Int? = null
    internal var fallbackDrawable: Drawable? = null
    internal var transition: Transition? = null
    internal var bitmapConfig: Bitmap.Config? = null

    /** Set the placeholder drawable to use when the request starts. */
    fun placeholder(@DrawableRes drawableResId: Int) = apply {
        this.placeholderResId = drawableResId
        this.placeholderDrawable = null
    }

    /** Set the placeholder drawable to use when the request starts. */
    fun placeholder(drawable: Drawable?) = apply {
        this.placeholderDrawable = drawable
        this.placeholderResId = 0
    }

    /** Set the error drawable to use if the request fails. */
    fun error(@DrawableRes drawableResId: Int) = apply {
        this.errorResId = drawableResId
        this.errorDrawable = null
    }

    /** Set the error drawable to use if the request fails. */
    fun error(drawable: Drawable?) = apply {
        this.errorDrawable = drawable
        this.errorResId = 0
    }

    /** Set the fallback drawable to use if image is null. */
    fun fallback(@DrawableRes drawableResId: Int) = apply {
        this.fallbackResId = drawableResId
        this.fallbackDrawable = null
    }

    /** Set the fallback drawable to use if image is null. */
    fun fallback(drawable: Drawable?) = apply {
        this.fallbackDrawable = drawable
        this.fallbackResId = 0
    }

    /**
     * @see ImageLoader.Builder.crossfade
     */
    fun crossfade(enable: Boolean) =
        crossfade(if (enable) CrossfadeDrawable.DEFAULT_DURATION else 0)

    /**
     * @see ImageLoader.Builder.crossfade
     */
    fun crossfade(durationMillis: Int) =
        transition(if (durationMillis > 0) CrossfadeTransition(durationMillis) else Transition.NONE)


    /**
     * Set the requested width/height.
     */
    fun size(@Px size: Int) = size(size, size)

    /**
     * Set the requested width/height.
     */
    fun size(@Px width: Int, @Px height: Int) = size(PixelSize(width, height))

    /**
     * Set the requested width/height.
     */
    private fun size(size: Size) {
        this.size = SizeResolver(size)
    }

    /**
     * Set the scaling algorithm that will be used to fit/fill the image into the size provided by [sizeResolver].
     *
     * NOTE: If [scale] is not set, it is automatically computed for [ImageView] targets.
     */
    private fun scale(scale: Scale) = apply {
        this.scale = scale
    }

    /**
     * @see ImageLoader.Builder.bitmapConfig
     */
    fun bitmapConfig(config: Bitmap.Config) = apply {
        this.bitmapConfig = config
    }

    private fun transition(transition: Transition) = apply {
        this.transition = transition
    }
}