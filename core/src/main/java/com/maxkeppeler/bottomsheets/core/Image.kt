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

package com.maxkeppeler.bottomsheets.core

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import okhttp3.HttpUrl
import java.io.File
import java.io.Serializable

/** Used to apply image request settings. */
typealias ImageBuilder = ImageRequest.() -> Unit

/** Convenience alias for coil's image request builder. */
private typealias CoiImageRequestBuilder = coil.request.ImageRequest.Builder.() -> Unit

/**
 * A class that holds the image data and some preferences for the image view and image loading.
 */
class Image private constructor() : Serializable {

    internal lateinit var any: Any
        private set

    internal var scaleType: ImageView.ScaleType? = null
        private set

    internal var ratio: Ratio? = null
        private set

    private var imageBuilder: ImageBuilder = {
        // Use by default always crossfade
        crossfade(true)
    }

    internal val coilRequestBuilder: CoiImageRequestBuilder
        get() = {
            val request = ImageRequest().apply { imageBuilder?.invoke(this) }
            request.scale?.let { scale(it) }
            request.size?.let { size(it) }
            request.placeholderResId?.let { placeholder(it) }
            request.placeholderDrawable?.let { placeholder(it) }
            request.errorResId?.let { error(it) }
            request.errorDrawable?.let { error(it) }
            request.fallbackResId?.let { fallback(it) }
            request.fallbackDrawable?.let { fallback(it) }
            request.transition?.let { transition(it) }
            request.bitmapConfig?.let { bitmapConfig(it) }
        }

    constructor(
        uri: String,
        ratio: Ratio? = null,
        scaleType: ImageView.ScaleType? = null,
        builder: ImageBuilder? = null
    ) : this() {
        this.any = uri
        this.ratio = ratio
        this.scaleType = scaleType
        builder?.let { this.imageBuilder = it }
    }

    constructor(
        url: HttpUrl,
        ratio: Ratio? = null,
        scaleType: ImageView.ScaleType? = null,
        builder: ImageBuilder? = null
    ) : this() {
        this.any = url
        this.ratio = ratio
        this.scaleType = scaleType
        builder?.let { this.imageBuilder = it }
    }

    constructor(
        uri: Uri,
        ratio: Ratio? = null,
        scaleType: ImageView.ScaleType? = null,
        builder: ImageBuilder? = null
    ) : this() {
        this.any = uri
        this.ratio = ratio
        this.scaleType = scaleType
        builder?.let { this.imageBuilder = it }
    }

    constructor(
        file: File,
        ratio: Ratio? = null,
        scaleType: ImageView.ScaleType? = null,
        builder: ImageBuilder? = null
    ) : this() {
        this.any = file
        this.ratio = ratio
        this.scaleType = scaleType
        builder?.let { this.imageBuilder = it }
    }

    constructor(
        @DrawableRes drawableResId: Int,
        builder: ImageBuilder? = null
    ) : this() {
        this.any = drawableResId
        builder?.let { this.imageBuilder = it }
    }

    constructor(
        drawable: Drawable,
        ratio: Ratio? = null,
        scaleType: ImageView.ScaleType? = null,
        builder: ImageBuilder? = null
    ) : this() {
        this.any = drawable
        this.ratio = ratio
        this.scaleType = scaleType
        builder?.let { this.imageBuilder = it }
    }

    constructor(
        bitmap: Bitmap,
        ratio: Ratio? = null,
        scaleType: ImageView.ScaleType? = null,
        builder: ImageBuilder? = null
    ) : this() {
        this.any = bitmap
        this.ratio = ratio
        this.scaleType = scaleType
        builder?.let { this.imageBuilder = it }
    }

    /**
     * A class used to receive an aspect ratio.
     */
    data class Ratio(val width: Int, val height: Int) : Serializable {
        val dimensionRatio: String = "$width:$height"
    }
}