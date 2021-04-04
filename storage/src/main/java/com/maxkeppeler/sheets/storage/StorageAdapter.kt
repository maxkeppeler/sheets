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
import androidx.recyclerview.widget.RecyclerView
import com.maxkeppeler.sheets.core.utils.*
import com.maxkeppeler.sheets.core.views.SheetContent
import com.maxkeppeler.sheets.storage.databinding.SheetsStorageFileGridItemBinding
import com.maxkeppeler.sheets.storage.databinding.SheetsStorgeFileListItemBinding
import com.maxkeppeler.sheets.storage.databinding.SheetsStorgeNavigationItemBinding
import kotlinx.coroutines.*
import java.io.File
import java.io.FileFilter
import java.util.*

internal class StorageAdapter(
    private val ctx: Context,
    private val selectionMode: StorageSelectionMode,
    private val selectedInitialFile: File,
    private val filter: FileFilter? = null,
    private val fileDisplayMode: FileDisplayMode,
    private val fileColumns: Int,
    private val allowFolderCreation: Boolean,
    private val multipleChoice: Boolean,
    private val listener: StorageAdapterListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val SELECTOR_STATE_SELECTED_INDEX = 1
        private const val HOLDER_NAVIGATION_ITEM = 0
        private const val HOLDER_FILE_GRID_ITEM = 2
        private const val HOLDER_FILE_LIST_ITEM = 3
    }

    private var currentFile = selectedInitialFile
    private var files: MutableList<File> = mutableListOf()
    private var searchFilesJob: Job? = null
    private val selectedFiles = mutableMapOf<File, Pair<ImageView, SheetContent>>()

    private val iconsColor = getIconColor(ctx)
    private val textColor = getTextColor(ctx)
    private val highlightColor = getHighlightColor(ctx)

    init {
        openDirectory(currentFile)
    }

    private val selectedTextColor =
        colorOfAttr(ctx, R.attr.sheetOptionSelectedTextColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)

    private val selectedIconsColor =
        colorOfAttr(ctx, R.attr.sheetOptionSelectedImageColor).takeUnlessNotResolved()
            ?: getPrimaryColor(ctx)

    override fun getItemViewType(position: Int): Int = when {
        currentFile.hasParent(filter) && position == 0 -> HOLDER_NAVIGATION_ITEM
        fileDisplayMode == FileDisplayMode.GRID -> HOLDER_FILE_GRID_ITEM
        else -> HOLDER_FILE_LIST_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HOLDER_NAVIGATION_ITEM -> {
                NavigationItem(
                    SheetsStorgeNavigationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            HOLDER_FILE_GRID_ITEM -> {
                FileGridItem(
                    SheetsStorageFileGridItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                FileListItem(
                    SheetsStorgeFileListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is NavigationItem -> holder.binding.buildNavigation()
            is FileGridItem -> holder.binding.buildGridItem(i)
            is FileListItem -> holder.binding.buildListItem(i)
        }
    }

    private fun SheetsStorgeNavigationItemBinding.buildNavigation() {
        val isRoot = currentFile == selectedInitialFile
        label.text = if (isRoot) "Home" else "Go back"
        container.changeRippleAndStateColor()
        icon.setImageResource(if (isRoot) R.drawable.sheets_ic_nav_home else R.drawable.sheets_ic_nav_back)
        container.setOnClickListener { browse(0) }

        if (allowFolderCreation && currentFile.canWrite()) {
            // TODO: Display button to create folder
        }
    }

    private fun SheetsStorgeFileListItemBinding.buildListItem(i: Int) {
        val actualIndex = actualIndex(i)
        val file = files[actualIndex]
        buildItem(i, file, root, container, icon, label)
    }

    private fun SheetsStorageFileGridItemBinding.buildGridItem(i: Int) {
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
            marginStart = if (fileColumns == 1) 16.toDp() else 0
        }

        label.text = file.name

        container.changeRippleAndStateColor()
        icon.setImageResource(file.getExtensionDrawable())

        val selected = listener.isSelected(file)

        if (selected) showSelected(label, icon, container)
        else showDeselected(label, icon, container)

        icon.setOnClickListener {
            selectOption(file, label, icon, container)
        }
        container.setOnClickListener {
            browse(index)
            if (file.isDirectory && selectionMode == StorageSelectionMode.FOLDER
                || file.isFile && selectionMode == StorageSelectionMode.FILE
            ) selectOption(file, label, icon, container)
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

    fun createFolder(name: String) {
        if (currentFile.canWrite()) {
            val newFile = File(currentFile.absolutePath.plus("/$name"))
            if (!newFile.mkdirs()) newFile.mkdirs()
            openDirectory(newFile)
        }
    }

    private fun browse(index: Int) {

        val parent = currentFile.getRealParent(filter)

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
                val rawContents = directory.listFiles() ?: arrayOf()
                when (selectionMode) {
                    StorageSelectionMode.FOLDER -> {
                        rawContents
                            .filter { it.isDirectory && (filter == null || filter.accept(it)) }
                            .sortedBy { it.name.toLowerCase(Locale.getDefault()) }
                    }
                    else -> {
                        rawContents
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
        val count = files.size
        return count.takeUnless { currentFile.hasParent(filter) } ?: count.plus(1)
    }

    private fun actualIndex(position: Int): Int =
        position.takeUnless { currentFile.hasParent(filter) } ?: position.minus(1)

    inner class NavigationItem(val binding: SheetsStorgeNavigationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FileGridItem(val binding: SheetsStorageFileGridItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FileListItem(val binding: SheetsStorgeFileListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}