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

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.sheets.core.utils.*
import com.maxkeppeler.sheets.core.views.SheetContent
import com.maxkeppeler.sheets.storage.databinding.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileFilter
import java.util.*

internal class StorageAdapter(
    private val ctx: Context,
    private val selectionMode: StorageSelectionMode,
    private val homeLocation: File,
    currentLocation: File?,
    private val filter: FileFilter? = null,
    private val fileDisplayMode: FileDisplayMode,
    private val fileColumns: Int,
    private val allowFolderCreation: Boolean,
    private val multipleChoice: Boolean,
    private val listener: StorageAdapterListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val NAVIGATION_ITEM_INDEX = 0
        private const val SELECTOR_STATE_SELECTED_INDEX = 1
        private const val HOLDER_NAVIGATION_ITEM = 0
        private const val HOLDER_FILE_VERTICAL_ITEM = 2
        private const val HOLDER_FILE_HORIZONTAL_ITEM = 3
        private const val HOLDER_EMPTY_ITEM = 4
        private const val INDENTATION_FILES = 16
        private const val INDENTATION_DEFAULT = 0
    }

    private var currentFile = currentLocation ?: homeLocation
    private var files: MutableList<File> = mutableListOf()
    private var searchFilesJob: Job? = null
    private val selectedFiles = mutableMapOf<File, Pair<ImageView, SheetContent>>()
    private val iconsColor = getIconColor(ctx)
    private val textColor = getTextColor(ctx)
    private val highlightColor = getHighlightColor(ctx)

    fun getCurrentFile(): File = currentFile

    fun getCurrentFiles(): MutableList<File> = files

    init {
        openDirectory(currentFile)
    }

    private val selectedTextColor =
        colorOfAttr(ctx, R.attr.sheetsOptionSelectedTextColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)

    private val selectedIconsColor =
        colorOfAttr(ctx, R.attr.sheetsOptionSelectedImageColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)

    override fun getItemViewType(position: Int): Int = when {
        currentFile.hasParent(homeLocation,
            filter) && position == NAVIGATION_ITEM_INDEX -> HOLDER_NAVIGATION_ITEM
        files.isEmpty() -> HOLDER_EMPTY_ITEM
        fileDisplayMode == FileDisplayMode.VERTICAL -> HOLDER_FILE_VERTICAL_ITEM
        else -> HOLDER_FILE_HORIZONTAL_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HOLDER_NAVIGATION_ITEM -> NavigationItem(
                SheetsStorgeNavigationItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            HOLDER_FILE_VERTICAL_ITEM -> FileVerticalItem(
                SheetsStorageFileVerticalItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            HOLDER_FILE_HORIZONTAL_ITEM -> FileHorizontalItem(
                SheetsStorgeFileHorizontalItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> EmptyItem(
                SheetsStorageEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is NavigationItem -> holder.binding.buildNavigation()
            is FileVerticalItem -> holder.binding.buildHorizontalItem(i)
            is FileHorizontalItem -> holder.binding.buildVerticalItem(i)
            is EmptyItem -> holder.binding.buildEmpty()
        }
    }

    private fun SheetsStorageEmptyBinding.buildEmpty() {
        label.text = ctx.getString(R.string.sheets_folder_empty)
        icon.setImageResource(R.drawable.sheets_ic_folder_empty)
    }

    private fun SheetsStorgeNavigationItemBinding.buildNavigation() {

        val isRoot = currentFile == homeLocation
        val textRes = if (isRoot) R.string.sheets_navigation_home else R.string.sheets_navigation_back
        val iconRes = if (isRoot) R.drawable.sheets_ic_nav_home else R.drawable.sheets_ic_nav_back
        label.text = ctx.getString(textRes)
        path.text = currentFile.absolutePath
        icon.setImageResource(iconRes)
        container.changeRippleAndStateColor()

        val browse = { browse(NAVIGATION_ITEM_INDEX) }
        icon.setOnClickListener { browse() }
        container.setOnClickListener { browse() }

        val displayFolderCreationButton = allowFolderCreation && currentFile.canWrite()
        val folderVisibility = if (displayFolderCreationButton) View.VISIBLE else View.GONE
        val folderIcon = ContextCompat.getDrawable(ctx, R.drawable.sheets_ic_folder_add)
        createFolderBtn.visibility = folderVisibility
        createFolderBtn.setImageDrawable(folderIcon)
        createFolderBtn.setOnClickListener { listener.createFolder(currentFile) }
    }

    private fun SheetsStorgeFileHorizontalItemBinding.buildVerticalItem(i: Int) {
        val actualIndex = actualIndex(i)
        val file = files[actualIndex]
        buildItem(i, file, root, container, icon, label)
    }

    private fun SheetsStorageFileVerticalItemBinding.buildHorizontalItem(i: Int) {
        val actualIndex = actualIndex(i)
        val file = files[actualIndex]
        buildItem(i, file, root, container, icon, label)
    }

    private fun buildItem(
        index: Int,
        file: File,
        root: View,
        container: View,
        icon: ImageView,
        label: SheetContent,
    ) {

        (root.layoutParams as RecyclerView.LayoutParams).apply {
            marginStart = if (fileColumns == 1) INDENTATION_FILES.toDp() else INDENTATION_DEFAULT
        }

        label.text = file.name
        container.changeRippleAndStateColor()
        icon.setImageResource(file.getExtensionDrawable())

        val selected = listener.isSelected(file)
        if (selected) showSelected(label, icon, container)
        else showDeselected(label, icon, container)

        icon.setOnClickListener {
            if (file.isFile && selectionMode == StorageSelectionMode.FILE) {
                container.callOnClick()
                return@setOnClickListener
            }
            if (file.isDirectory && selectionMode == StorageSelectionMode.FOLDER && multipleChoice) {
                selectOption(file, label, icon, container)
            } else browse(index)
        }
        container.setOnClickListener {
            if (file.isFile && selectionMode == StorageSelectionMode.FILE) {
                selectOption(file, label, icon, container)
            } else browse(index)
        }
    }

    private fun showSelected(label: SheetContent, icon: ImageView, root: View) {
        label.setTextColor(selectedTextColor)
        icon.setColorFilter(selectedIconsColor)
        if (multipleChoice) root.isSelected = true
    }

    private fun showDeselected(label: SheetContent, icon: ImageView, root: View) {
        label.setTextColor(textColor)
        icon.setColorFilter(iconsColor)
        if (multipleChoice) root.isSelected = false
    }

    private fun View.changeRippleAndStateColor(
        rippleColor: Int = highlightColor,
        stateIndex: Int = SELECTOR_STATE_SELECTED_INDEX,
        stateBackgroundColor: Int = highlightColor,
    ) {
        // Ripple drawable
        (background as RippleDrawable).apply {
            setColor(ColorStateList.valueOf(rippleColor))
            // Selector drawable
            (getDrawable(1) as StateListDrawable).apply {
                // Selected state drawable
                (getStateDrawable(stateIndex) as GradientDrawable).apply {
                    color = ColorStateList.valueOf(stateBackgroundColor)
                }
            }
        }
    }

    private fun selectOption(file: File, label: SheetContent, icon: ImageView, root: View) {
        if (multipleChoice) {
            if (!listener.isMultipleChoiceSelectionAllowed(file)) return
            if (selectedFiles.keys.contains(file)) {
                listener.deselectMultipleChoice(file)
                selectedFiles[file]?.let { showDeselected(it.second, it.first, root) }
                selectedFiles.remove(file)
            } else {
                listener.selectMultipleChoice(file)
                selectedFiles[file] = Pair(icon, label)
                showSelected(label, icon, root)
            }
        } else {
            selectedFiles.forEach { showDeselected(it.value.second, it.value.first, root) }
            selectedFiles.clear()
            selectedFiles[file] = Pair(icon, label)
            showSelected(label, icon, root)
            listener.select(file)
        }
    }

    /** Creates a folder with the specified name at the current location. */
    fun createFolder(name: String) {
        if (currentFile.canWrite()) {
            val newFile = File(currentFile.absolutePath.plus("/$name"))
            if (!newFile.mkdirs()) newFile.mkdirs()
            openDirectory(newFile)
        }
    }

    private fun browse(index: Int) {
        val parent = currentFile.getRealParent(homeLocation, filter)
        if (parent != null && index == HOLDER_NAVIGATION_ITEM) {
            openDirectory(parent)
            return
        }

        val actualIndex = actualIndex(index)
        val selected = files[actualIndex]
        if (selected.isDirectory) {
            openDirectory(selected)
        }
    }

    private fun openDirectory(directory: File) {
        searchFilesJob?.cancel()
        searchFilesJob = GlobalScope.launch(Dispatchers.Main) {
            currentFile = directory
            val result = withContext(Dispatchers.IO) {
                val listedFiles = directory.listFiles() ?: arrayOf()
                when (selectionMode) {
                    StorageSelectionMode.FOLDER -> {
                        listedFiles
                            .filter { it.isDirectory && (filter == null || filter.accept(it)) }
                            .sortedBy { it.name.toLowerCase(Locale.getDefault()) }
                    }
                    else -> {
                        listedFiles
                            .sortedWith(compareBy({
                                !it.isDirectory && (filter == null || filter.accept(it))
                            }, {
                                it.name.toLowerCase(Locale.getDefault())
                            }))
                    }
                }
            }
            files = result.toMutableList()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        var count = files.size
        if (currentFile.hasParent(homeLocation, filter)) count += 1
        if (files.isEmpty()) count += 1
        return count
    }

    private fun actualIndex(position: Int): Int {
        var index = position
        if (currentFile.hasParent(homeLocation, filter)) index -= 1
        if (files.isEmpty()) index -= 1
        return index
    }

    inner class NavigationItem(val binding: SheetsStorgeNavigationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FileVerticalItem(val binding: SheetsStorageFileVerticalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FileHorizontalItem(val binding: SheetsStorgeFileHorizontalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class EmptyItem(val binding: SheetsStorageEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)
}