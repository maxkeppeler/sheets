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

package com.maxkeppeler.sheets.core

import androidx.appcompat.widget.AppCompatImageView
import java.io.Serializable

typealias ImageViewBuilder = AppCompatImageView.() -> Unit

/**
 * A class that holds general image view settings.
 */
abstract class ImageSource : Serializable {

    internal var imageViewBuilder: ImageViewBuilder? = null
    internal var ratio: Ratio? = null

    /** Setup ImageView. */
    fun setupImageView(imageViewBuilder: ImageViewBuilder) {
        this.imageViewBuilder = imageViewBuilder
    }

    /** Set the ratio of the container view that contains the ImageView. */
    fun ratio(ratio: Ratio) {
        this.ratio = ratio
    }
}