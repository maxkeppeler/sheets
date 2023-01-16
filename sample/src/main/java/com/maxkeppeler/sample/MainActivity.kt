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

package com.maxkeppeler.sample

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputLayout
import com.maxkeppeler.sample.custom_sheets_example.CustomSheet
import com.maxkeppeler.sample.databinding.MainActBinding
import com.maxkeppeler.sample.utils.SheetExample
import com.maxkeppeler.sample.utils.toFormattedDate
import com.maxkeppeler.sheets.calendar.CalendarMode
import com.maxkeppeler.sheets.calendar.CalendarSheet
import com.maxkeppeler.sheets.calendar.SelectionMode
import com.maxkeppeler.sheets.calendar.TimeLine
import com.maxkeppeler.sheets.color.ColorSheet
import com.maxkeppeler.sheets.color.ColorView
import com.maxkeppeler.sheets.core.*
import com.maxkeppeler.sheets.core.layoutmanagers.CustomStaggeredGridLayoutManager
import com.maxkeppeler.sheets.info.InfoSheet
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.Validation
import com.maxkeppeler.sheets.input.type.InputCheckBox
import com.maxkeppeler.sheets.input.type.InputEditText
import com.maxkeppeler.sheets.input.type.InputRadioButtons
import com.maxkeppeler.sheets.input.type.spinner.InputSpinner
import com.maxkeppeler.sheets.input.type.spinner.SpinnerOption
import com.maxkeppeler.sheets.lottie.LottieAnimation
import com.maxkeppeler.sheets.lottie.cancelCoverAnimation
import com.maxkeppeler.sheets.lottie.withCoverLottieAnimation
import com.maxkeppeler.sheets.option.DisplayMode
import com.maxkeppeler.sheets.option.Option
import com.maxkeppeler.sheets.option.OptionSheet
import com.maxkeppeler.sheets.storage.FileDisplayMode
import com.maxkeppeler.sheets.storage.StorageSelectionMode
import com.maxkeppeler.sheets.storage.StorageSheet
import com.maxkeppeler.sheets.duration.DurationTimeFormat
import com.maxkeppeler.sheets.duration.DurationSheet
import java.io.File
import java.io.FileFilter
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.random.Random

/**
 * @author Maximilian Keppeler
 */
class MainActivity : AppCompatActivity() {

    companion object {
        @Suppress("unused")
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: MainActBinding
    private var sheetStyle: SheetStyle? = null
    private var themeMode: ThemeMode? = null

    private fun getSheetStyle(): SheetStyle = sheetStyle ?: SheetStyle.values().random()

    private fun showBottomSheet(example: SheetExample) {

        when (example) {
            SheetExample.OPTIONS_LIST -> showOptionsSheetList()
            SheetExample.OPTIONS_HORIZONTAL_SMALL -> showOptionsSheetGridSmall(DisplayMode.GRID_HORIZONTAL)
            SheetExample.OPTIONS_HORIZONTAL_MIDDLE -> showOptionsSheetGridMiddle(DisplayMode.GRID_HORIZONTAL)
            SheetExample.OPTIONS_HORIZONTAL_LARGE -> showOptionsSheetGridLarge(DisplayMode.GRID_HORIZONTAL)
            SheetExample.OPTIONS_VERTICAL_SMALL -> showOptionsSheetGridSmall(DisplayMode.GRID_VERTICAL)
            SheetExample.OPTIONS_VERTICAL_MIDDLE -> showOptionsSheetGridMiddle(DisplayMode.GRID_VERTICAL)
            SheetExample.OPTIONS_VERTICAL_LARGE -> showOptionsSheetGridLarge(DisplayMode.GRID_VERTICAL)
            SheetExample.COLOR -> showColorSheet()
            SheetExample.COLOR_TEMPLATE -> showColorSheetTemplate()
            SheetExample.COLOR_CUSTOM -> showColorSheetCustom()
            SheetExample.CLOCK_TIME -> showClockTimeSheet()
            SheetExample.TIME_HH_MM_SS -> showTimeSheet(DurationTimeFormat.HH_MM_SS)
            SheetExample.TIME_HH_MM -> showTimeSheet(DurationTimeFormat.HH_MM)
            SheetExample.TIME_MM_SS -> showTimeSheet(DurationTimeFormat.MM_SS)
            SheetExample.TIME_M_SS -> showTimeSheet(DurationTimeFormat.M_SS)
            SheetExample.TIME_SS -> showTimeSheet(DurationTimeFormat.SS)
            SheetExample.TIME_MM -> showTimeSheet(DurationTimeFormat.MM)
            SheetExample.TIME_HH -> showTimeSheet(DurationTimeFormat.HH)
            SheetExample.CALENDAR_MULTIPLE_DATES -> showMultipleCalendarView()
            SheetExample.CALENDAR_RANGE_MONTH -> showCalendarSheet()
            SheetExample.CALENDAR_WEEK1 -> showCalendarSheetWeek1()
            SheetExample.CALENDAR_RANGE_WEEK2 -> showCalendarSheetWeek2()
            SheetExample.CALENDAR_RANGE_WEEK3 -> showCalendarSheetWeek3()
            SheetExample.INFO -> showInfoSheet()
            SheetExample.INFO_COVER_IMAGE_1 -> showInfoSheetTopStyleTop()
            SheetExample.INFO_COVER_IMAGE_2 -> showInfoSheetTopStyleBottom()
            SheetExample.INFO_COVER_IMAGE_3 -> showInfoSheetTopStyleMixed()
            SheetExample.INFO_LOTTIE -> showInfoSheetLottie()
            SheetExample.INPUT_SHORT -> showInputSheetShort()
            SheetExample.INPUT_LONG -> showInputSheetLong()
            SheetExample.INPUT_SPINNER_ICON -> showInputSheetSpinnerIcons()
            SheetExample.INPUT_PASSWORD -> showInputSheetPassword()
            SheetExample.CUSTOM1 -> showCustomSheet()
            SheetExample.STORAGE_LIST -> withStoragePermissions { showStorageSheetList() }
            SheetExample.STORAGE_LIST_FILTER -> withStoragePermissions { showStorageSheetListFilter() }
            SheetExample.STORAGE_LIST_COLUMNS -> withStoragePermissions { showStorageSheetListColumns() }
            SheetExample.STORAGE_GRID -> withStoragePermissions { showStorageSheetGridColumns() }
            SheetExample.STORAGE_LIST_FOLDER -> withStoragePermissions { showStorageSheetListFolder() }
            SheetExample.STORAGE_LIST_FOLDER_MULTI -> withStoragePermissions { showStorageSheetListFolders() }
        }
    }

    private fun showCalendarSheetWeek1() {

        CalendarSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("When do you want to take holidays?") // Set the title of the sheet
            rangeYears(50)
            selectionMode(SelectionMode.DATE)
            calendarMode(CalendarMode.WEEK_1)
            val date = Calendar.getInstance().apply {
                set(Calendar.YEAR, 1997)
                set(Calendar.MONTH, Calendar.DECEMBER)
                set(Calendar.DAY_OF_MONTH, 22)
            }
            setSelectedDate(date)
            onPositive { dateStart, dateEnd -> // dateEnd is only not null if the selection is a range
                dateEnd?.let {
                    showToastLong(
                        "CalendarSheet result range",
                        "${dateStart.timeInMillis.toFormattedDate()} - ${it.timeInMillis.toFormattedDate()}"
                    )
                } ?: kotlin.run {
                    showToastLong(
                        "CalendarSheet result date",
                        dateStart.timeInMillis.toFormattedDate()
                    )
                }
            }
        }
    }

    private fun showCalendarSheetWeek2() {

        CalendarSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("When do you want to take holidays?") // Set the title of the sheet
            selectionMode(SelectionMode.RANGE)
            calendarMode(CalendarMode.WEEK_2)
//            disableTimeline(TimeLine.FUTURE)
            onPositive { dateStart, dateEnd -> // dateEnd is only not null if the selection is a range
                dateEnd?.let {
                    showToastLong(
                        "CalendarSheet result range",
                        "${dateStart.timeInMillis.toFormattedDate()} - ${it.timeInMillis.toFormattedDate()}"
                    )
                } ?: kotlin.run {
                    showToastLong(
                        "CalendarSheet result date",
                        dateStart.timeInMillis.toFormattedDate()
                    )
                }
            }
        }
    }

    private fun showCalendarSheetWeek3() {

        CalendarSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("When do you want to take holidays?") // Set the title of the sheet
            selectionMode(SelectionMode.RANGE)
            calendarMode(CalendarMode.WEEK_3)
            disableTimeline(TimeLine.FUTURE)
            onPositive { dateStart, dateEnd -> // dateEnd is only not null if the selection is a range
                dateEnd?.let {
                    showToastLong(
                        "CalendarSheet result range",
                        "${dateStart.timeInMillis.toFormattedDate()} - ${it.timeInMillis.toFormattedDate()}"
                    )
                } ?: kotlin.run {
                    showToastLong(
                        "CalendarSheet result date",
                        dateStart.timeInMillis.toFormattedDate()
                    )
                }
            }
        }
    }

    private fun showCalendarSheet() {

        CalendarSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("When do you want to take holidays?") // Set the title of the sheet
            rangeYears(50)
            selectionMode(SelectionMode.RANGE)
            calendarMode(CalendarMode.MONTH)
//            hideToolbar()
//            disableTimeline(TimeLine.PAST)
            disable(
                Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    .apply { add(Calendar.DAY_OF_MONTH, 2) })
            disable(Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 10) })
            disable(Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 20) })
            repeat(5) {
                disable(Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 2 + it) })
            }
            onPositive { dateStart, dateEnd -> // dateEnd is only not null if the selection is a range
                dateEnd?.let {
                    showToastLong(
                        "CalendarSheet result range",
                        "${dateStart.timeInMillis.toFormattedDate()} - ${it.timeInMillis.toFormattedDate()}"
                    )
                } ?: kotlin.run {
                    showToastLong(
                        "CalendarSheet result date",
                        dateStart.timeInMillis.toFormattedDate()
                    )
                }
            }
        }
    }

    private fun showMultipleCalendarView() {
        CalendarSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("When do you want to take holidays?") // Set the title of the sheet
            rangeYears(50)
            selectionMode(SelectionMode.DATE_MULTIPLE)
            calendarMode(CalendarMode.MONTH)
            onMultiplePositive {
                showToastLong(
                    "CalendarSheet result range",
                    "Num Selected: ${it.size}"
                )
            }
        }
    }

    private fun showOptionsSheetList() {

        val sheet = OptionSheet().build(this) { // Build and show
            style(getSheetStyle())
            displayMode(DisplayMode.LIST)
            cornerFamily(CornerFamily.CUT)
            titleColor(Color.YELLOW)
            cornerRadius(16f)
            title("Note from 27th dec") // Set the title of the sheet
            if (Random.nextBoolean()) {
                displayButtons() // For single choice, no buttons are displayed, except you enforce to display them
            }
            with( // Add options
                Option(R.drawable.ic_send, "Send"),
                Option(R.drawable.ic_invite, "Invite"), // String or StringRes
                Option(R.drawable.ic_archive, "Archive").disabled(false)
            )
            onPositive { index, option ->
                // Selected index / option
            }
            onDismiss {
                binding.exampleRecyclerView.visibility = View.VISIBLE
            }
        }

        sheet.show() // Show later
    }

    private fun showOptionsSheetGridSmall(displayMode: DisplayMode) {

        val sheet = OptionSheet().build(this) { // Build only
            style(getSheetStyle())
            displayMode(displayMode) // Display mode for list/grid + scroll into height or width
            title("What's your favorite fruit?")
            displayMode(DisplayMode.GRID_HORIZONTAL) // Display mode for list/grid + scroll into height or width
            with( // Add options
                Option(R.drawable.ic_fruit_cherries, "Cherries"),
                Option(R.drawable.ic_fruit_watermelon, "Watermelon"),
                Option(R.drawable.ic_fruit_grapes, "Grapes")
            )
            onPositive { index, option ->
                // All selected indices / options
            }
        }

        sheet.show() // Show later
    }

    private fun showOptionsSheetGridMiddle(displayMode: DisplayMode) {

        OptionSheet().show(this) { // Build and show
            style(getSheetStyle())
            displayMode(displayMode) // Display mode for list/grid + scroll into height or width
            title("What would you like to eat daily?")
            multipleChoices() // Apply to make it multiple choices
            minChoices(3) // Set minimum choices
            maxChoices(4) // Set maximum choices
            displayMultipleChoicesInfo() // Show info view for selection
            displayButtons()
            with(
                Option(R.drawable.ic_apple, "Apple"),
                Option(
                    R.drawable.ic_fruit_cherries,
                    "Cherries"
                ).disable(), // An option can be disabled
                Option(R.drawable.ic_food_pasta, "Pasta"),
                Option(R.drawable.ic_fruit_watermelon, "Watermelon"),
                Option(
                    R.drawable.ic_fruit_grapes,
                    "Grapes"
                ).select(), // An option can be preselected
                Option(R.drawable.ic_food_burger, "Burger"),
                Option(R.drawable.ic_fruit_pineapple, "Pineapple"),
                Option(R.drawable.ic_food_croissant, "Croissant")
            )
            onPositiveMultiple { selectedIndices: MutableList<Int>, selectedOptions: MutableList<Option> ->
                // All selected indices / options
            }
        }
    }

    private fun showOptionsSheetGridLarge(displayMode: DisplayMode) {

        OptionSheet().show(this) { // Build and show
            style(getSheetStyle())
            displayMode(displayMode) // Display mode for list/grid + scroll into height or width
            title("What would you like to eat daily?")
            with(
                Option(R.drawable.ic_food_burger, "Burger"),
                Option(R.drawable.ic_fruit_pineapple, "Pineapple"),
                Option(R.drawable.ic_food_croissant, "Croissant"),
                Option(R.drawable.ic_apple, "Apple"),
                Option(
                    R.drawable.ic_fruit_cherries,
                    "Cherries"
                ).disable(), // An option can be disabled
                Option(R.drawable.ic_food_pasta, "Pasta"),
                Option(R.drawable.ic_fruit_watermelon, "Watermelon"),
                Option(
                    R.drawable.ic_fruit_grapes,
                    "Grapes"
                ), // An option can be preselected
                Option(R.drawable.ic_food_burger, "Burger"),
                Option(R.drawable.ic_fruit_pineapple, "Pineapple"),
                Option(R.drawable.ic_food_croissant, "Croissant")
            )
            onPositive { index, option ->
                // All selected indices / options
            }
        }
    }

    private fun showOptionsSheetGridVertical() {

        OptionSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("Alarm at 5 am")
            displayMode(DisplayMode.GRID_VERTICAL) // Display mode for list/grid + scroll into height or width
            if (Random.nextBoolean()) {
                displayButtons() // For single choice, no buttons are displayed, except you enforce to display them
            }
            with( // Add options
                Option(
                    R.drawable.sheets_ic_color_picker,
                    "Edit"
                ).disable(), // An option can be disabled
                Option(
                    R.drawable.sheets_ic_color_picker,
                    "Cancel"
                ), // An option can be preselected
                Option(R.drawable.sheets_ic_color_picker, "Duplicate")
            )
            onDismiss {
            }
        }

    }


    private fun showColorSheet() {
        ColorSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("Background color")
            defaultView(ColorView.TEMPLATE) // Set the default view when the sheet is visible
            // disableSwitchColorView() Disable switching between template and custom color view
            onPositive { color ->
                // Use Color
            }
        }
    }


    private fun showColorSheetTemplate() {
        ColorSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("Background color")
            defaultView(ColorView.TEMPLATE) // Set the default view when the sheet is visible
            disableSwitchColorView()
            onPositive { color ->
                // Use Color
            }
        }
    }

    private fun showColorSheetCustom() {
        ColorSheet().show(this) { // Build and show
            style(getSheetStyle())
            title("Background color")
            defaultView(ColorView.CUSTOM) // Set the default view when the sheet is visible
            disableSwitchColorView()
            onPositive { color ->
                // Use Color
            }
        }
    }

    private fun showClockTimeSheet() {

//        ClockTimeSheet().show(this) {
//            style(getSheetStyle())
//            title("Wake-up time")
//            format24Hours(Random.nextBoolean()) // By default 24-hours format is enabled
//            currentTime(
//                TimeUnit.HOURS.toMillis(5).plus(TimeUnit.MINUTES.toMillis(30))
//            ) // Set current time
//            onPositive { milliseconds, hours, minutes ->
//                // Use selected clock time in millis
//                showToastLong("Clock time", "$hours - $minutes ($milliseconds)")
//            }
//        }
    }

    private fun showTimeSheet(timeFormat: DurationTimeFormat) {

        DurationSheet().show(this) {
            style(getSheetStyle())
            title("Snooze time")
            format(timeFormat)
            currentTime(
                TimeUnit.HOURS.toSeconds(0)
                    .plus(TimeUnit.MINUTES.toSeconds(50).plus(TimeUnit.SECONDS.toSeconds(12)))
            )
//            currentTime(0) // Set current time in seconds
//            minTime(1) // Set minimum time in seconds
//            maxTime(600) // Set maximum time in seconds
            onPositive { timeInSec ->
                // Use selected time in millis
                Log.d(TAG, "timeInSec: $timeInSec")
                showToastLong("Time", "Seconds: $timeInSec")
            }
        }
    }

    private fun showInputSheetShort() {

        InputSheet().show(this) {
            style(getSheetStyle())
            title("Survey about this library.")
            with(InputRadioButtons("") {
                required()
                drawable(R.drawable.ic_telegram)
                label("How did you find this library?")
                options(mutableListOf("Google", "GitHub", "Twitter"))
                selected(1)
                changeListener { value -> showToast("RadioButton change", value.toString()) }
                resultListener { value -> showToast("RadioButton result", value.toString()) }
            })
            with(InputCheckBox("use") { // Read value later by index or custom key from bundle
                label("Usage")
                text("I use this library in my awesome app.")
                changeListener { value -> showToast("CheckBox change", value.toString()) }
                resultListener { value -> showToast("CheckBox result", value.toString()) }
            })

            onNegative { showToast("InputSheet cancelled", "No result") }
            onPositive { result ->
                showToastLong("InputSheet result", result.toString())
                val text = result.getString("0") // Read value of inputs by index
                val check = result.getBoolean("use") // Read value by passed key
            }
        }
    }


    private fun showInputSheetLong() {

        InputSheet().show(this) {
            style(getSheetStyle())
            title("Short survey")
            with(InputEditText {
                required()
                defaultValue(buildSpannedString {
                    append("The ")
                    bold{
                        append("Office")
                    }
                })
                startIconDrawable(R.drawable.ic_mail)
                label("Your favorite TV-Show")
                hint("The Mandalorian, ...")
                inputType(InputType.TYPE_CLASS_TEXT)
                changeListener { value -> showToast("Text change", value.toString()) }
                resultListener { value -> showToast("Text result", value.toString()) }
            })
            with(InputCheckBox("binge_watching") { // Read value later by index or custom key from bundle
                label("Binge Watching")
                text("I'm regularly binge watching shows on Netflix.")
                defaultValue(false)
                changeListener { value -> showToast("CheckBox change", value.toString()) }
                resultListener { value -> showToast("CheckBox result", value.toString()) }
            })

            with(InputSpinner {
                required()
                drawable(R.drawable.ic_telegram)
                label("Favorite show in the list")
                noSelectionText("Select Show")
                options(
                    mutableListOf(
                        "Westworld",
                        "Fringe",
                        "The Expanse",
                        "Rick and Morty",
                        "Attack on Titan",
                        "Death Note",
                        "Parasite",
                        "Jujutsu Kaisen"
                    )
                )
                changeListener { value -> showToast("Spinner change", value.toString()) }
                resultListener { value -> showToast("Spinner result", value.toString()) }
            })

            with(InputRadioButtons("") {
                required()
                drawable(R.drawable.ic_telegram)
                label("Streaming service of your choice")
                options(mutableListOf("Netflix", "Amazon", "Other"))
                selected(0)
                changeListener { value -> showToast("RadioButton change", value.toString()) }
                resultListener { value -> showToast("RadioButton result", value.toString()) }
            })
            onNegative { showToast("InputSheet cancelled", "No result") }
            onPositive { result ->
                showToastLong("InputSheet result", result.toString())
                val text = result.getString("0") // Read value of inputs by index
                val check = result.getBoolean("binge_watching") // Read value by passed key
            }
        }
    }

    private fun showInputSheetSpinnerIcons() {

        InputSheet().show(this) {
            style(getSheetStyle())
            title("Spinner Icons")
            with(InputSpinner {
                required()
                label("Favorite Fruit")
                noSelectionText("Select Fruit")
                options(
                    mutableListOf(
                        SpinnerOption("Apple", R.drawable.ic_apple),
                        SpinnerOption("Cherries", R.drawable.ic_fruit_cherries),
                        SpinnerOption("Grapes", R.drawable.ic_fruit_grapes),
                        SpinnerOption("Pineapple", R.drawable.ic_fruit_pineapple),
                        SpinnerOption("Watermelon", R.drawable.ic_fruit_watermelon)
                    )
                )
                changeListener { value -> showToast("Spinner change", value.toString()) }
                resultListener { value -> showToast("Spinner result", value.toString()) }
            })

            onNegative { showToast("InputSheet cancelled", "No result") }
            onPositive { result ->
                showToastLong("InputSheet result", result.toString())
            }
        }
    }

    private fun showInputSheetPassword() {

        var password1: String? = "1"
        var password2: String?
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        val errorText =
            "Must contain at least one digit, lower case letter, upper case letter, special character, no whitespace and at least 8 characters."

        InputSheet().show(this) {
            style(getSheetStyle())
            title("Choose a password")
            withIconButton(IconButton(R.drawable.ic_help)) {
                showToast(
                    "IconButton",
                    "Help clicked..."
                )
            }
            with(InputEditText {
                required()
                hint("Password")
                startIconDrawable(R.drawable.ic_lock)
                endIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE)
                passwordVisible(false /* Don't display password in clear text. */)
                validationListener { value ->
                    password1 = value.toString()
                    val pattern = Pattern.compile(regex)
                    val matcher = pattern.matcher(value)
                    val valid = matcher.find()
                    if (valid) Validation.success()
                    else Validation.failed(errorText)
                }
                changeListener { value -> showToast("Text change", value.toString()) }
                resultListener { value -> showToast("Text result", value.toString()) }
            })
            with(InputEditText {
                required()
                startIconDrawable(R.drawable.ic_lock)
                hint("Repeat password")
                endIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE)
                passwordVisible(false)
                validationListener { value ->
                    password2 = value.toString()
                    if (password1 != password2) {
                        Validation.failed("Passwords don't match.")
                    } else Validation.success()
                }
                resultListener { value -> showToast("Text result", value.toString()) }
            })
            onNegative("cancel") { showToast("InputSheet cancelled", "No result") }
            onPositive("register") {
                showToastLong("InputSheet result", "Passwords matched.")
            }
        }
    }

    private fun showInfoSheet() {

        InfoSheet().show(
            this,
            width = null /* Use custom view width (e.g. for landscape mode or tablet). */
        ) {
            style(getSheetStyle())
            withIconButton(IconButton(R.drawable.ic_github)) { /* e. g. open website. */ }
            title("Did you read the README?")
            content("It will help you to setup beautiful sheets in your project.")
            onNegative(
                "Not yet",
                R.drawable.ic_github
            ) { /* Set listener when negative button is clicked. */
                showToast("onNegative", "")
            }
            onPositive("Yes") {
                showToast("onPositive", "")
            }
            positiveButtonStyle(ButtonStyle.NORMAL)
            negativeButtonStyle(ButtonStyle.OUTLINED)
            displayNegativeButton(true)
            displayPositiveButton(true)
            drawable(R.drawable.ic_github)
            drawableColor(R.color.sheets_md_red_500)
        }
    }

    private var storageSheet: StorageSheet? = null

    private fun showStorageSheetList() {

        storageSheet = StorageSheet().show(this) {
            style(getSheetStyle())
            fileDisplayMode(FileDisplayMode.HORIZONTAL)
            fileColumns(1)
            multipleChoices(true)
            maxChoices(8)
            minChoices(3)
            displayMultipleChoicesInfo(true)
            selectionMode(StorageSelectionMode.FILE)
            onCreateFolder { file, listener -> showInputStorageFolderNameSheet(file, listener) }
            onPositive { files -> showToastLong("onPositive", "Files: $files") }
        }
    }

    private fun showInputStorageFolderNameSheet(file: File, listener: (name: String) -> Unit) {

        storageSheet?.dismiss()

        InputSheet().show(this) {
            style(getSheetStyle())
            with(InputEditText {
            })
            onPositive { data ->
                val name = data.getString("0", "New Folder")
                listener.invoke(name)
            }
            onClose {
//                storageSheet?.currentLocation(file)
                storageSheet?.homeLocation(file)
                storageSheet?.show()
            }
        }
    }

    private fun showStorageSheetListFilter() {

        val fileFilter = FileFilter { file -> !file.path.endsWith(".jpg") }

        StorageSheet().show(this) {
            style(getSheetStyle())
            fileDisplayMode(FileDisplayMode.HORIZONTAL)
            fileColumns(1)
            filter(fileFilter)
            selectionMode(StorageSelectionMode.FILE)
            displayToolbar(false)
            onPositive { files -> showToastLong("onPositive", "Files: $files") }
        }
    }

    private fun showStorageSheetListColumns() {

        StorageSheet().show(this) {
            style(getSheetStyle())
            fileDisplayMode(FileDisplayMode.HORIZONTAL)
            fileColumns(2)
            selectionMode(StorageSelectionMode.FILE)
            displayToolbar(false)
            onPositive { files -> showToastLong("onPositive", "Files: $files") }
        }
    }

    private fun showStorageSheetGridColumns() {

        StorageSheet().show(this) {
            style(getSheetStyle())
            fileDisplayMode(FileDisplayMode.VERTICAL)
            fileColumns(3)
            selectionMode(StorageSelectionMode.FILE)
            displayToolbar(false)
            onPositive { files -> showToastLong("onPositive", "Files: $files") }
        }
    }

    private fun showStorageSheetListFolder() {

        StorageSheet().show(this) {
            style(getSheetStyle())
            fileDisplayMode(FileDisplayMode.HORIZONTAL)
            selectionMode(StorageSelectionMode.FOLDER)
            onPositive { files -> showToastLong("onPositive", "Folder: $files") }
        }
    }

    private fun showStorageSheetListFolders() {

        StorageSheet().show(this) {
            style(getSheetStyle())
            fileDisplayMode(FileDisplayMode.HORIZONTAL)
            selectionMode(StorageSelectionMode.FOLDER)
            multipleChoices(true)
            onPositive { files -> showToastLong("onPositive", "Folders: $files") }
        }
    }

    private fun showInfoSheetTopStyleTop() {

        InfoSheet().show(this) {
            style(getSheetStyle())
            cornerFamily(CornerFamily.CUT)
            topStyle(TopStyle.ABOVE_COVER)
            withCoverImage(Image("https://images.hdqwalls.com/download/interstellar-gargantua-u4-1440x900.jpg"))
            withIconButton(IconButton(R.drawable.ic_github)) { /* e. g. open website. */ }
            withIconButton(IconButton(R.drawable.ic_mail)) { /* Will not automatically dismiss the sheet. */ }
            title("Interstellar")
            content("“We used to look up at the sky and wonder at our place in the stars, now we just look down and worry about our place in the dirt.” — Cooper")
            onNegative("")
            onPositive("Ok")
        }
    }

    private fun showInfoSheetTopStyleBottom() {

        InfoSheet().show(this) {
            style(getSheetStyle())
            displayCloseButton(false)
            cornerFamily(CornerFamily.CUT)
            cornerRadius(24f)
            topStyle(TopStyle.BELOW_COVER)
            withCoverImage(Image("https://img4.goodfon.com/wallpaper/nbig/7/47/westworld-anthony-hopkins-robert-ford-actor-show-faces.jpg") {
                setupRequest {
                    // Image request / loading setup
                    // fallback(R.drawable.ic_help)
                }
                setupImageView {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    // Manipulate ImageView
                }
            })
            withIconButton(IconButton(R.drawable.ic_help)) { }
            title("Dr. Robert Ford")
            content("Dreams mean everything. They’re the stories we tell ourselves of what could be, who we could become.\"")
            onNegative("Disagree")
            onPositive("Agree")
        }
    }

    private fun showInfoSheetLottie() {

        InfoSheet().show(this) {
            style(getSheetStyle())
            displayCloseButton(false)
            cornerFamily(CornerFamily.CUT)
            cornerRadius(16f)
            topStyle(TopStyle.values().random())
            withCoverLottieAnimation(LottieAnimation {
                ratio(Ratio(3, 2))
                setupAnimation {
                    // Lottie animation setup
                    setAnimation(R.raw.anim_lottie_business_team)
                    // .. more settings
                }
                setupImageView {
                    // Manipulate ImageView
//                    setBackgroundColor(ContextCompat.getColor(context, R.color.sheetDividerColor))
                }
            })
            withIconButton(IconButton(R.drawable.ic_help)) { cancelCoverAnimation() }
            title("Team Collaboration")
            content("In the world of software projects, it is inevitable that we will find ourselves working in a team to deliver a project.")
            onNegative("Learn how") { }
            onPositive("Great")
        }
    }

    private fun showInfoSheetTopStyleMixed() {

        InfoSheet().show(this) {
            displayButtons(false)
            style(getSheetStyle())
            cornerFamily(CornerFamily.CUT)
            topStyle(TopStyle.MIXED)
            withCoverImage(Image("https://cdn.wallpapersafari.com/11/17/LjhbqX.jpg") {
                setupRequest {
                    // For placeholder, error, fallback drawable and other image loading configs
                    crossfade(300)
                }
                setupImageView {
                    // Setup ImageView
                }
            })
            withIconButton(IconButton(R.drawable.ic_help)) { /* Will not automatically dismiss the sheet. */ }
            title("Attack on Titan")
            content("It is set in a world where humanity lives inside cities surrounded by enormous walls that protect them from gigantic man-eating humanoids referred to as Titans; the story follows Eren Yeager, who vows to exterminate the Titans after a Titan brings about the destruction of his hometown and the death of his mother.")
        }
    }

    private fun showCustomSheet() {

        CustomSheet().show(this) {
            style(getSheetStyle())
            title("Custom Example")
            onPositive("Cool")
        }
    }

    private fun showToast(id: String, value: String?) {
        Toast.makeText(
            this@MainActivity,
            id.plus(": $value"),
            Toast.LENGTH_SHORT
        ).apply { show() }
    }

    private fun showToastLong(id: String, value: String?) {
        Toast.makeText(
            this@MainActivity,
            id.plus(": $value"),
            Toast.LENGTH_LONG
        ).apply { show() }
    }


    override fun onCreate(saved: Bundle?) {
        loadPreferences()
        applyThemeMode()
//        val newTheme = R.style.BottomSheetSignNightTheme
//        theme.applyStyle(newTheme, true)
        super.onCreate(saved)
        binding = MainActBinding.inflate(layoutInflater).also { setContentView(it.root) }
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setup()
    }

    private fun setup() {

        setSupportActionBar(binding.toolbar)

        binding.exampleRecyclerView.layoutManager =
            CustomStaggeredGridLayoutManager(2, RecyclerView.VERTICAL, false)
        binding.exampleRecyclerView.adapter = BottomSheetExampleAdapter(this, ::showBottomSheet)

        binding.sheetStyleButtonGroup.check(
            when (sheetStyle) {
                SheetStyle.BOTTOM_SHEET -> R.id.bottomSheet
                SheetStyle.DIALOG -> R.id.dialog
                null -> R.id.random
            }
        )

        binding.sheetStyleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                sheetStyle = when (checkedId) {
                    R.id.bottomSheet -> SheetStyle.BOTTOM_SHEET
                    R.id.dialog -> SheetStyle.DIALOG
                    else -> null
                }
                setPrefString(Preference.SHEET_STYLE, sheetStyle?.name)
            }
        }

        binding.themeButtonGroup.check(
            when (themeMode) {
                ThemeMode.NIGHT_MODE -> R.id.nightMode
                ThemeMode.DAY_MODE -> R.id.dayMode
                else -> R.id.auto
            }
        )

        binding.themeButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                themeMode = when (checkedId) {
                    R.id.dayMode -> ThemeMode.DAY_MODE
                    R.id.nightMode -> ThemeMode.NIGHT_MODE
                    else -> ThemeMode.AUTO
                }
                setPrefString(Preference.THEME_MODE, themeMode?.name)
                applyThemeMode()
            }
        }
    }

    private fun withStoragePermissions(withPermissionListener: () -> Unit) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 10
            )

        } else withPermissionListener.invoke()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.github -> open()
            R.id.share -> share()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun open() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/maxkeppeler/sheets"))
        startActivity(intent)
    }

    private fun share() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this library! https://github.com/maxkeppeler/sheets"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun loadPreferences() {
        val sheetStyleName = getPrefString(Preference.SHEET_STYLE)
        sheetStyle =
            sheetStyleName?.takeIf { SheetStyle.values().any { it.name == sheetStyleName } }
                ?.let { SheetStyle.valueOf(it) }

        val themeModeName = getPrefString(Preference.THEME_MODE)

        themeMode =
            themeModeName?.takeIf { ThemeMode.values().any { it.name == themeModeName } }
                ?.let { ThemeMode.valueOf(it) }
    }

    private fun applyThemeMode() {
        when (themeMode) {
            ThemeMode.NIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeMode.DAY_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private val prefs: SharedPreferences
        get() = getDefaultSharedPreferences(applicationContext)

    enum class ThemeMode {
        AUTO,
        NIGHT_MODE,
        DAY_MODE
    }

    enum class Preference(val key: String) {
        THEME_MODE("pref_theme_mode"),
        SHEET_STYLE("pref_sheet_style"),
    }

    private fun setPrefString(pref: Preference, value: String?) =
        prefs.edit().putString(pref.key, value).apply()

    private fun getPrefString(preference: Preference): String? =
        prefs.getString(preference.key, null)

}
