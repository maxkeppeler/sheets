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

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.maxkeppeler.bottomsheets.calendar.CalendarMode
import com.maxkeppeler.bottomsheets.calendar.CalendarSheet
import com.maxkeppeler.bottomsheets.calendar.SelectionMode
import com.maxkeppeler.bottomsheets.calendar.TimeLine
import com.maxkeppeler.bottomsheets.color.ColorSheet
import com.maxkeppeler.bottomsheets.color.ColorView
import com.maxkeppeler.bottomsheets.core.utils.splitTime
import com.maxkeppeler.bottomsheets.core.utils.toFormattedDate
import com.maxkeppeler.bottomsheets.core.utils.toFormattedTimeHHMMSS
import com.maxkeppeler.bottomsheets.info.InfoSheet
import com.maxkeppeler.bottomsheets.input.InputSheet
import com.maxkeppeler.bottomsheets.input.type.InputCheckBox
import com.maxkeppeler.bottomsheets.input.type.InputEditText
import com.maxkeppeler.bottomsheets.input.type.InputRadioButtons
import com.maxkeppeler.bottomsheets.input.type.InputSpinner
import com.maxkeppeler.bottomsheets.options.DisplayMode
import com.maxkeppeler.bottomsheets.options.Option
import com.maxkeppeler.bottomsheets.options.OptionsSheet
import com.maxkeppeler.bottomsheets.time_clock.ClockTimeSheet
import com.maxkeppeler.bottomsheets.time_clock.TimeFormat
import com.maxkeppeler.bottomsheets.time_clock.TimeSheet
import com.maxkeppeler.sample.databinding.MainActBinding
import java.util.*
import java.util.concurrent.TimeUnit
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

    override fun onCreate(saved: Bundle?) {
//        val newTheme = R.style.BottomSheetSignNightTheme
//        theme.applyStyle(newTheme, true)
        super.onCreate(saved)
        binding = MainActBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setup()
    }

    private fun setup() {

        binding.optionsListSheet.setOnClickListener { showOptionsSheetList() }

        binding.optionsGridShortSheet.setOnClickListener { showOptionsSheetGridHorizontal() }

        binding.optionsGridLongSheet.setOnClickListener { showOptionsSheetGridVertical() }

        binding.colorSheet.setOnClickListener { showColorSheet() }

        binding.clockTimeSheet.setOnClickListener { showClockTimeSheet() }

        binding.timeSheet.setOnClickListener { showTimeSheet() }

        binding.calendarSheet.setOnClickListener { showCalendarSheet() }

        binding.infoSheet.setOnClickListener { showInfoSheet() }

        binding.inputSheet.setOnClickListener { showInputSheet() }
    }

    private fun showCalendarSheet() {

        CalendarSheet().show(this) { // Build and show
            title("When do you want to take holidays?") // Set the title of the bottom sheet
            rangeYears(50)
            selectionMode(SelectionMode.RANGE)
            calendarMode(CalendarMode.MONTH)
//            hideToolbar()
            disableTimeline(TimeLine.PAST)
            showButtons(true)
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

    private fun showOptionsSheetList() {

        val sheet = OptionsSheet().build(this) { // Build and show
            displayMode(DisplayMode.LIST)
            title("Note from 27th dec") // Set the title of the bottom sheet
            if (Random.nextBoolean()) {
                showButtons() // For single choice, no buttons are displayed, except you enforce to display them
            }
            with( // Add options
                Option(R.drawable.ic_send, "Send"),
                Option(R.drawable.ic_invite, "Invite"), // String or StringRes
                Option(R.drawable.ic_archive, "Archive").disabled(false)
            )
            onPositive { index, option ->
                // Selected index / option
            }
        }

        sheet.show() // Show later
    }

    private fun showOptionsSheetGridHorizontal() {

        val sheet = OptionsSheet().build(this) { // Build only
            displayMode(DisplayMode.GRID_HORIZONTAL) // Display mode for list/grid + scroll into height or width
            if (Random.nextBoolean()) { // Simulating design of longer options grid, which scrolls horizontal and multiple choices
                title("What would you like to eat daily?")
                multipleChoices() // Apply to make it multiple choices
                minChoices(3) // Set minimum choices
                maxChoices(4) // Set maximum choices
                showMultipleChoicesInfo() // Show info view for selection
                showButtons()
                with(
                    Option(R.drawable.ic_apple, "Apple"),
                    Option(R.drawable.ic_fruit_cherries, "Cherries").disable(), // An option can be disabled
                    Option(R.drawable.ic_food_pasta, "Pasta"),
                    Option(R.drawable.ic_fruit_watermelon, "Watermelon"),
                    Option(R.drawable.ic_fruit_grapes, "Grapes").select(), // An option can be preselected
                    Option(R.drawable.ic_food_burger, "Burger"),
                    Option(R.drawable.ic_fruit_pineapple, "Pineapple"),
                    Option(R.drawable.ic_food_croissant, "Croissant")
                )
            } else {
                title("What's your favorite fruit?")
                displayMode(DisplayMode.GRID_HORIZONTAL) // Display mode for list/grid + scroll into height or width
                with( // Add options
                    Option(R.drawable.ic_fruit_cherries, "Cherries"),
                    Option(R.drawable.ic_fruit_watermelon, "Watermelon"),
                    Option(R.drawable.ic_fruit_grapes, "Grapes")
                )
            }
            onPositiveMultiple { selectedIndices: MutableList<Int>, selectedOptions: MutableList<Option> ->
                // All selected indices / options
            }
        }

        sheet.show() // Show later
    }

    private fun showOptionsSheetGridVertical() {

        OptionsSheet().show(this) { // Build and show
            title("Alarm at 5 am")
            displayMode(DisplayMode.GRID_VERTICAL) // Display mode for list/grid + scroll into height or width
            if (Random.nextBoolean()) {
                showButtons() // For single choice, no buttons are displayed, except you enforce to display them
            }
            with( // Add options
                Option(
                    R.drawable.bs_ic_color_picker,
                    "Edit"
                ).disable(), // An option can be disabled
                Option(
                    R.drawable.bs_ic_color_picker,
                    "Cancel"
                ), // An option can be preselected
                Option(R.drawable.bs_ic_color_picker, "Duplicate")
            )
            onDismiss {
            }
        }

    }


    private fun showColorSheet() {
        ColorSheet().show(this) { // Build and show
            title("Background color")
            defaultView(ColorView.TEMPLATE) // Set the default view when the bottom sheet is visible
            // disableSwitchColorView() Disable switching between template and custom color view
            onPositive { color ->
                // Use Color
            }
        }
    }

    private fun showClockTimeSheet() {

        ClockTimeSheet().show(this) {
            title("Wake-up time")
            format24Hours(Random.nextBoolean()) // By default 24-hours format is enabled
            currentTime(TimeUnit.HOURS.toMillis(5)) // Set current time
            onPositive { clockTimeInMillis ->
                // Use selected clock time in millis
                showToastLong("Clock time", clockTimeInMillis.toFormattedTimeHHMMSS())
            }
        }
    }

    private fun showTimeSheet() {

        TimeSheet().show(this) {
            title("Snooze time")
            format(TimeFormat.HH_MM_SS)
            currentTime(90) // Set current time in seconds
            minTime(60) // Set minimum time in seconds
            maxTime(600) // Set maximum time in seconds
            onPositive { timeInSec ->
                // Use selected time in millis
                Log.d(TAG, "timeInSec: $timeInSec")
                showToastLong("Time", splitTime(timeInSec).toString())
            }
        }
    }

    private fun showInfoSheet() {

        InfoSheet().show(this) {
            title("Did you read the README on GitHub?")
            content("It will help you to setup beautiful bottom sheets in your project.")
            onNegative("Not yet") { /* Set listener when negative button is clicked. */ }
            onPositive("Yes")
        }
    }

    private fun showInputSheet() {

        InputSheet().show(this) {
            title("Short survey")
            content("We would like to ask you some questions reading your streaming platform usage.")
            with(InputEditText {
                required()
                drawable(R.drawable.ic_mail)
                label("Your favorite TV-Show")
                hint("The Mandalorian, ...")
                changeListener { value -> showToast("Text change", value) }
                resultListener { value -> showToast("Text result", value) }
            })
            with(InputCheckBox("binge_watching") { // Read value later by index or custom key from bundle
                label("Binge Watching")
                text("I'm regularly binge watching shows on Netflix.")
                default(false)
                changeListener { value -> showToast("CheckBox change", value.toString()) }
                resultListener { value -> showToast("CheckBox result", value.toString()) }
            })

            with(InputSpinner {
                required()
                drawable(R.drawable.ic_telegram)
                label("Favorite show in the list")
                text("Select Show")
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
                //  preselectedIndex(0)
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
}