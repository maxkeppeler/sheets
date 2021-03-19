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
import android.net.Uri
import androidx.annotation.DrawableRes
import okhttp3.HttpUrl
import java.io.File

/** Convenience alias for this class. */
private typealias ImageBuilder = Image.() -> Unit

/** Convenience alias for coil's image request builder. */
private typealias CoiImageRequestBuilder = coil.request.ImageRequest.Builder.() -> Unit

/** Used to apply image request settings. */
typealias ImageRequestBuilder = ImageRequest.() -> Unit

/**
 * A class that holds the image data and some preferences for the image view and image loading.
 */
class Image private constructor() : ImageSource() {

    internal lateinit var any: Any
        private set

    private var imageRequestBuilder: ImageRequestBuilder = {
        // Use by default always crossfade
        crossfade(true)
    }

    internal val coilRequestBuilder: CoiImageRequestBuilder
        get() = {
            val request = ImageRequest().apply { imageRequestBuilder.invoke(this) }
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

    constructor(uri: String, builder: ImageBuilder? = null) : this() {
        this.any = uri
        builder?.invoke(this)
    }

    constructor(url: HttpUrl, builder: ImageBuilder? = null) : this() {
        this.any = url
        builder?.invoke(this)
    }

    constructor(uri: Uri, builder: ImageBuilder? = null) : this() {
        this.any = uri
        builder?.invoke(this)
    }

    constructor(file: File, builder: ImageBuilder? = null) : this() {
        this.any = file
        builder?.invoke(this)
    }

    constructor(@DrawableRes drawableRes: Int, builder: ImageBuilder? = null) : this() {
        this.any = drawableRes
        builder?.invoke(this)
    }

    constructor(drawable: Drawable, builder: ImageBuilder? = null) : this() {
        this.any = drawable
        builder?.invoke(this)
    }

    constructor(bitmap: Bitmap, builder: ImageBuilder? = null) : this() {
        this.any = bitmap
        builder?.invoke(this)
    }

    /** Setup the image loading request. */
    fun setupRequest(builder: ImageRequestBuilder) {
        this.imageRequestBuilder = builder
    }

}