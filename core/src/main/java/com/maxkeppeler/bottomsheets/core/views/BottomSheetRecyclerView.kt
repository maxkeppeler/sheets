package com.maxkeppeler.bottomsheets.core.views

import android.content.Context
import android.util.AttributeSet
import android.widget.EdgeEffect
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.bottomsheets.R
import com.maxkeppeler.bottomsheets.core.utils.colorOfAttrs

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


@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BottomSheetRecyclerView
@JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : RecyclerView(ctx, attrs) {

init {

    edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
            val edgeColor = colorOfAttrs(ctx, R.attr.bottomSheetHighlightColor, R.attr.colorControlHighlight)
            return EdgeEffect(view.context).apply { color = edgeColor }
        }
    }
}

}