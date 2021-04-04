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

import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.annotation.DrawableRes
import java.io.File
import java.io.FileFilter
import java.util.*

/** Check if file has a parent. */
internal fun File.hasParent(fileFilter: FileFilter?): Boolean = getRealParent(fileFilter) != null

/** Get real parent file. */
internal fun File.getRealParent(fileFilter: FileFilter?): File? {

    // External storage directory's is not readable, prevent to jump into parent folder
    if (absolutePath == Environment.getExternalStorageDirectory().path) return this

    parentFile?.let { file ->
        return if (file.canRead() && file.hasFiles(fileFilter)) {
            file
        } else null

    } ?: return null
}

internal fun File.hasFiles(fileFilter: FileFilter?): Boolean {
    val files = listFiles()?.filter { fileFilter == null || fileFilter.accept(it) } ?: arrayListOf()
    return files.isNotEmpty()
}

/** Get a suitable drawable depending on the file's extension. */
@DrawableRes
internal fun File.getExtensionDrawable(): Int {
    val ext = MimeTypeMap.getFileExtensionFromUrl(absolutePath).toLowerCase(Locale.ROOT)
    return when {
        EXTENSION_IMAGE.contains(ext) -> R.drawable.sheets_ic_file_image
        EXTENSION_VIDEO.contains(ext) -> R.drawable.sheets_ic_file_video
        EXTENSION_AUDIO.contains(ext) -> R.drawable.sheets_ic_file_music
        EXTENSION_TEXT.contains(ext) -> R.drawable.sheets_ic_file_text
        EXTENSION_COMPRESSED.contains(ext) -> R.drawable.sheets_ic_folder_zip
        isDirectory -> R.drawable.sheets_ic_folder
        isFile -> R.drawable.sheets_ic_file
        else -> R.drawable.sheets_ic_file
    }
}
