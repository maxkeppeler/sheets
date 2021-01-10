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
 * WITHOUT WARRANTIES OR CONDITIONS O@F ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

@file:Suppress("unused")

package com.maxkeppeler.sheets.input

/**
 * Static helper methods for validations.
 */
object Validation {

    /**
     * Report an valid text.
     */
    fun success(): ValidationResult = ValidationResult(true)

    /**
     * Report an invalid text with an error text.
     */
    fun failed(errorText: String): ValidationResult = ValidationResult(false, errorText)
}
