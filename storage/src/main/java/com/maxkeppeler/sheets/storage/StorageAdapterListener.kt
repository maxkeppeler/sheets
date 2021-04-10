/*
 *  Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.maxkeppeler.sheets.storage

import java.io.File

/**
 * Defines interactions between the Sheet or user and the adapter.
 */
internal interface StorageAdapterListener {

    /** Select file. */
    fun select(file: File)

    /** Select file in multiple choice mode. */
    fun selectMultipleChoice(file: File)

    /** Deselect file in multiple choice mode. */
    fun deselectMultipleChoice(file: File)

    /** Check if file is selected. */
    fun isSelected(file: File): Boolean

    /** Check if multiple choice is allowed. */
    fun isMultipleChoiceSelectionAllowed(file: File): Boolean

    /** Create a folder based within the file's path. */
    fun createFolder(file: File)
}