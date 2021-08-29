# Sheets

<p>

  <img src="art/ic_library.png" width="96px" height="96px" alt="Sheets Library" align="left" style="margin-right: 24px; margin-bottom: 24px">

  <p>

Sleek dialogs and bottom-sheets for quick use in your app. Choose one of the available sheets or build custom sheets on top of the existing functionality.

   <a href="https://search.maven.org/search?q=g:%22com.maxkeppeler.sheets%22">
     <img style="margin-right: 4px; margin-bottom: 8px;" alt="Version of Sheets library" src="https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/core.svg?label=Maven%20Central">
   </a>

   <a href="https://github.com/maxkeppeler/sheets">
     <img style="margin-right: 4px; margin-bottom: 8px;" alt="Codacy code quality of Sheets library" src="https://img.shields.io/codacy/grade/9a3b68b152e149fd82f0873e2fed78d5?label=Code%20Quality">
   </a>

   <a href="https://www.apache.org/licenses/LICENSE-2.0">
     <img style="margin-right: 4px; margin-bottom: 8px;" alt="GitHub" src="https://img.shields.io/github/license/maxkeppeler/sheets?color=%23007EC6&label=">
   </a>

<a href="https://github.com/maxkeppeler/sheets">
  <img style="margin-right: 4px; margin-bottom: 8px" alt="Give this library a star" src="https://img.shields.io/github/stars/maxkeppeler/sheets?style=social">
</a>

<a href="https://github.com/maxkeppeler/sheets/fork">
  <img style="margin-right: 4px; margin-bottom: 8px" alt="Fork this library" src="https://img.shields.io/github/forks/maxkeppeler/sheets?style=social">
</a>

<a href="https://github.com/maxkeppeler/sheets">
  <img style="margin-right: 4px; margin-bottom: 8px" alt="Watch this library" src="https://img.shields.io/github/watchers/maxkeppeler/sheets.svg?style=social&amp;label=Watch">
</a>

<a href="https://github.com/maxkeppeler/">
  <img style="margin-right: 4px; margin-bottom: 8px" alt="Follow me on GitHub" src="https://img.shields.io/github/followers/maxkeppeler?style=social&label=Follow">
</a>

<a href="https://twitter.com/intent/tweet?text=Checkout%20this%20beautiful%20library!%20%23android%20%23androiddev%20%23library%20%40maxkeppeler%20%0A%0Ahttps%3A%2F%2Fgithub.com%2Fmaxkeppeler%2Fsheets">
  <img style="margin-right: 4px; margin-bottom: 8px" alt="Share this library on Twitter" src="https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Fgithub.com%2Fmaxkeppeler%2Fsheets&label=Share">
</a>

<a href="https://twitter.com/maxkeppeler">
  <img style="margin-right: 4px; margin-bottom: 8px" alt="Follow Maximilian Keppeler on Twitter" src="https://img.shields.io/twitter/follow/maxkeppeler?label=Follow&style=social">
</a>

  </p>
</p>

Read in [Deutsch](README.de_DE.md) or [简体中文](README.zh_CN.md).

<img src="art/showcase.png" alt="sheetss Library">

## Table of Contents

- [Get started](#get-started)
  - [Info Sheet](#info)
  - [Options Sheet](#options)
  - [Clock Time Sheet](#clock-time)
  - [Time Sheet](#time)
  - [Input Sheet](#input)
  - [Calendar Sheet](#calendar)
  - [Storage Sheet](#storage)
  - [Color Sheet](#color)
  - [Custom Sheet](#custom)
  - [Lottie](#lottie)
  - [Appearance](#appearance)
- [Misc](#misc)
  - [Support this project](#support-this-project)
  - [Contribute](#contribute)
  - [Donate](#donate)
  - [Showcase](#showcase)
  - [License](#license)

# Get started

A sheet can dynamically be displayed as either a dialog or as a bottom-sheet.
Check out the [sample](https://github.com/MaxKeppeler/sheets/blob/main/sample/sample.apk).

You have to use the `core` module as it is the foundation of any sheet.

In your top-level `build.gradle` file:

```gradle
repositories {
  ...
  mavenCentral()
}
```

In your app `build.gradle` file:

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/core.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/core)

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:core:<latest-version>'
}
```

**Base functions** <br/>
Following functions can be called from any type of sheet.

| Function              | Action                                                                           |
| --------------------- | -------------------------------------------------------------------------------- |
| style()               | Display as dialog or bottom-sheet.                                               |
| title()               | Set the title text.                                                              |
| titleColor()          | Set the title text color.                                                        |
| titleColorRes()       | Set the title text color by a resource.                                          |
| withCoverImage()      | Add a cover image.                                                               |
| topStyle()            | Specify the style of the cover image and top bar.                                |
| positiveButtonStyle() | Define the style of the positive button (Text, Filled, Outlined).                |
| negativeButtonStyle() | Define the style of the negative button (Text, Filled, Outlined).                |
| withIconButton()      | Add up to 3 icon buttons to the top bar.                                         |
| closeIconButton()     | Set a custom close icon button.                                                  |
| displayHandle()       | Display the handle.                                                              |
| displayCloseButton()  | Display close icon button.                                                       |
| displayToolbar()      | Display toolbar. (Close icon button, title, divider and icon buttons)            |
| peekHeight()          | Set the peek height. (Only bottom-sheet)                                         |
| cornerRadius()        | Set corner radius.                                                               |
| cornerFamily()        | Set corner family. (Cut or rounded)                                              |
| borderWidth()         | Set the border width.                                                            |
| borderColor()         | Set the border color.                                                            |
| cancelableOutside()   | Make sheet cancelable outside of the dialog view.                                |
| onNegative()          | Set the negative button text and listener.                                       |
| onPositive()          | Set the positive button text and listener.                                       |
| onDismiss()           | Set a listener that is invoked when the sheet is dismissed.                      |
| onCancel()            | Set a listener that is invoked when the sheet is cancelled (only if cancelable). |
| onClose()             | Set a listener that is invoked when the sheet is closed.                         |
| show()                | show the sheet.                                                                  |

Each sheet has an extension function called `build` and `show`.<br/>

Use `build` to build a sheet and display it later.

    val sheet = InfoSheet().build(context) {
      // build sheet
    }

    sheet.show() // Show sheet when ready

Use `show` if you want to build and then immediately display it.

    InfoSheet().show(context) {
      // build sheet
    } // Show sheet

## Info

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/info.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/core)

The `Info` Sheet lets you display information or warning.

<details open>
<br/>
<br/>
<summary>Showcase as Dialog</summary>

<img src="art/InfoSheet Dialog.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Top.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Bottom.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Mixed.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>

</details>
</br>

<details>
<summary>Showcase as BottomSheet</summary>

<br/>
<br/>
<img src="art/InfoSheet BottomSheet.png" width="80%" alt="Sheets InfoSheet"><br/>
<img src="art/InfoSheet BottomSheet Cover TopStyle Top.png" width="80%" alt="Sheets InfoSheet BottomSheet"><br/>
<img src="art/InfoSheet BottomSheet Cover TopStyle Bottom.png" width="80%" alt="Sheets InfoSheet BottomSheet"><br/>
<img src="art/InfoSheet BottomSheet Cover TopStyle Mixed.png" width="80%" alt="Sheets InfoSheet BottomSheet"><br/>
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:info:<latest-version>'
}
```

### Usage

For the default info sheet use it as following:

    InfoSheet().show(context) {
      title("Do you want to install Awake?")
      content("Awake is a beautiful alarm app with morning challenges, advanced alarm management and more.")
      onNegative("No") {
        // Handle event
      }
      onPositive("Install") {
        // Handle event
      }
    }

| Function        | Action              |
| --------------- | ------------------- |
| content()       | Set content text.   |
| drawable()      | Set drawable.       |
| drawableColor() | Set drawable color. |
| customView() | Use a custom view. |

## Options

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/options.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/options)

The `Options` Sheet lets you display a grid or list of options.

<details open>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/OptionsSheet Dialog Grid Middle.png" width="80%" alt="Sheets OptionsSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/OptionsSheet BottomSheet Grid Middle.png" width="80%" alt="Sheets OptionsSheet BottomSheet">
</details>

<br/>
<details>
<br/><br/>
<summary>Showcase some variants as Dialogs</summary>

<img src="art/OptionsSheet Dialog List.png" width="80%" alt="Sheets OptionsSheet Dialog"><br/>
<img src="art/OptionsSheet Dialog Grid Small.png" width="80%" alt="Sheets OptionsSheet" Dialog><br/>
<img src="art/OptionsSheet Dialog Grid Large Horizontal.png" width="80%" alt="Sheets OptionsSheet" Dialog><br/>

</details>
</br>

<details>
<br/><br/>
<summary>Showcase some variants as BottomSheets</summary>

<img src="art/OptionsSheet BottomSheet List.png" width="80%" alt="Sheets OptionsSheet BottomSheet"><br/>
<img src="art/OptionsSheet BottomSheet Grid Small.png" width="80%" alt="Sheets OptionsSheet" BottomSheet><br/>
<img src="art/OptionsSheet BottomSheet Grid Large Horizontal.png" width="80%" alt="Sheets OptionsSheet" BottomSheet><br/>

</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:info:<latest-version>'
}
```

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:options:<latest-version>'
}
```

### Usage

For the default options sheet use it as following:

    OptionsSheet().show(context) {
      title("Text message")
      with(
        Option(R.drawable.ic_copy, "Copy"),
        Option(R.drawable.ic_translate, "Translate"),
        Option(R.drawable.ic_paste, "Paste")
      )
      onPositive { index: Int, option: Option ->
        // Handle selected option
      }
    }

| Function                     | Action                                                                                 |
| ---------------------------- | -------------------------------------------------------------------------------------- |
| multipleChoices()            | Allow multiple choices content.                                                        |
| displayMultipleChoicesInfo() | Display info of the multiple choices.                                                  |
| maxChoicesStrictLimit()      | Specify that the max choices is strict and more choices can't be selected temporarily. |
| minChoices()                 | Set the minimum amount of choices.                                                     |
| maxChoices()                 | Set the maximum amount of choices.                                                     |
| onPositiveMultiple()         | Set listener for multiple choices.                                                     |
| displayButtons()             | Display buttons and require a positive button click for selection.                     |
| displayMode()                | Display options in a list or a vertical/ horizontal growing scrollable grid.           |
| preventIconTint()                | (Global) Prevents the lib to use a tint for the icons. Keeps the default colors of a drawable.           |

**Option**

| Function   | Action               |
| ---------- | -------------------- |
| selected() | Preselect an option. |
| disable()  | Disable an option.   |
| preventIconTint()                | (Local) Prevents the lib to use a tint for the icons. Keeps the default colors of a drawable.           |

**Note**: Preselected options automatically increase the current selection while disabled options decrease the maximum amount of choices.

## Clock Time

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/time-clock.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/time-clock)

The `Clock Time` Sheet lets you quickly pick a time.

<details>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/ClockTimeSheet Dialog.png" width="80%" alt="Sheets OptionsSheet Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/ClockTimeSheet BottomSheet.png" width="80%" alt="Sheets OptionsSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:time-clock:<latest-version>'
}
```

### Usage

For the default clock time sheet, in 24-hours format, use it as following:

    ClockTimeSheet().show(context) {
      title("Wake-up time")
      onPositive { clockTimeInMillis: Long ->
        // Handle selected time
      }
    }

| Function        | Action                                |
| --------------- | ------------------------------------- |
| format24Hours() | Use 24-hours or 12-hours format.      |
| currentTime()   | Set the current time in milliseconds. |

## Time

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/time.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/time)

The `Time` Sheet lets you pick a duration time in a specific format.

<details open>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/TimeSheet Dialog.png" width="80%" alt="Sheets TimeSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/TimeSheet BottomSheet.png" width="80%" alt="Sheets TimeSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:time:<latest-version>'
}
```

### Usage

For the default time sheet use it as following:

    TimeSheet().show(context) {
      title("Snooze time")
      onPositive { durationTimeInMillis: Long ->
        // Handle selected time
      }
    }

| Function      | Action                                         |
| ------------- | ---------------------------------------------- |
| format()      | Select the time format. (hh:mm:ss, mm:ss, ...) |
| currentTime() | Set the current time in seconds.               |
| minTime()     | Set the minimum time.                          |
| maxTime()`    | Set the maximum time.                          |

## Input

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/input.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/input)

The `Input` Sheet lets you display a form consisting of various inputs.

<details>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/InputSheet Dialog Short.png" width="80%" alt="Sheets InputSheet Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/InputSheet BottomSheet Short.png" width="80%" alt="Sheets InputSheet BottomSheet">
</details>

<br/>
<details>
<br/><br/>
<summary>Showcase some variants as Dialogs</summary>

<img src="art/InputSheet Dialog Long.png" width="80%" alt="Sheets InputSheet Dialog"><br/>

</details>
</br>

<details>
<br/><br/>
<summary>Showcase some variants as BottomSheets</summary>

<img src="art/InputSheet BottomSheet Long.png" width="80%" alt="Sheets InputSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:input:<latest-version>'
}
```

### Usage

For the default input sheet use it as following:

    InputSheet()).show(context) {
        title("Short survey")
      with(InputEditText {
        required())
        label("Your favorite TV-Show")
        hint("The Mandalorian, ...")
        validationListener { value -> } // Add custom validation logic
        changeListener { value -> } // Input value changed
        resultListener { value -> } // Input value changed when form finished
      })
      with(InputCheckBox("binge_watching") { // Read value later by index or custom key from bundle
        label("Binge Watching")
        text("I'm regularly binge watching shows on Netflix.")
        // ... more options
      })
      with(InputRadioButtons() {
        required()
        label("Streaming service of your choice")
        options(mutableListOf("Netflix", "Amazon", "Other"))
      })
      // ... more input options
      onNegative { showToast("InputSheet cancelled", "No result") }
      onPositive { result ->
          showToastLong("InputSheet result", result.toString())
          val text = result.getString("0") // Read value of inputs by index
          val check = result.getBoolean("binge_watching") // Read value by passed key
      }
    }

| Function       | Action                              |
| -------------- | ----------------------------------- |
| with()         | Add an input. (see input options)   |
| displayInput() | Set visibility of a specific input. |

**Input options:**

- `InputEditText`
- `InputCheckBox`
- `InputSwitch`
- `InputRadioButtons`
- `InputSpinner`
- `InputSeparator`

**Input**<br/>

| Function         | Action                                        |
| ---------------- | --------------------------------------------- |
| label()          | Set the label text.                           |
| content()        | Set content text. (e. g. to explain a survey) |
| drawable()       | Set the drawable.                             |
| required()       | Mark input as required.                       |
| visible()        | Set initial visibility.                       |
| changeListener() | Set listener to observe changes.              |
| resultListener() | Set listener for final value.                 |

**InputEditText**<br/>

| Function             | Action                                            |
| -------------------- | ------------------------------------------------- |
| hint()               | Set the hint text.                                |
| defaultValue()       | Set default text.                                 |
| inputType()          | Set the `android.text.InputType`'s.               |
| inputFilter()        | Set the `android.text.inputFilter`                |
| maxLines()           | Set the max amount of lines.                      |
| endIconMode()        | Set TextInputLayout.EndIconMode.                  |
| endIconActivated()   | Set the EndIcon activated.                        |
| passwordVisible()    | Make the password initially visible or invisible. |
| validationListener() | Validate the text input with your own logic.      |

**InputCheckBox** <br/>

| Function       | Action             |
| -------------- | ------------------ |
| text()         | Set the text.      |
| defaultValue() | Set default value. |

**InputSwitch** <br/>

| Function       | Action             |
| -------------- | ------------------ |
| text()         | Set the text.      |
| defaultValue() | Set default value. |

**InputRadioButtons** <br/>

| Function   | Action                             |
| ---------- | ---------------------------------- |
| options()  | Set a list of RadioButton options. |
| selected() | Set a selected index.              |

**InputSpinner** <br/>

| Function                              | Action                                                    |
| ------------------------------------- | --------------------------------------------------------- |
| noSelectionText()                     | Set the text that is displayed, when nothing is selected. |
| options(MutableList of String)        | Set a list of options.                                    |
| options(MutableList of SpinnerOption) | Set a list of options with optional icon for each option. |
| selected()                            | Set a selected index.                                     |


## Calendar

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/calendar.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.calendar/core)

The `Calendar` Sheet lets you pick a date or date range. This type was build using the library [CalendarView](https://github.com/kizitonwose/CalendarView).

<details open>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/CalendarSheet Dialog Period.png" width="80%" alt="Sheets CalendarSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/CalendarSheet BottomSheet Period.png" width="80%" alt="Sheets CalendarSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:calendar:<latest-version>'
}
```

### Usage

For the default time sheet use it as following:

    CalendarSheet().show(this) { // Build and show
      title("What's your date of birth?") // Set the title of the sheet
      onPositive { dateStart, dateEnd ->
        // Handle date or range
      }

| Function          | Action                                                           |
| ----------------- | ---------------------------------------------------------------- |
| selectionMode()   | Choose the selection mode (date or range).                       |
| calendarMode()    | Choose the calendar mode (week with various rows or month-view). |
| disableTimeline() |  Disable either past or future dates.                            |
| rangeYears()      | Set the range of years into past and future.                     |
| disable()         | Pass a `Calendar` object to disable various dates for selection. |
| displayButtons()  | Show or hide the buttons view.                                   |

## Storage

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/storage.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/storage)

The `Storage` Sheet lets you pick one or more files or folders.

<details open>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/StorageSheet Dialog.png" width="80%" alt="Sheets StorageSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/StorageSheet BottomSheet.png" width="80%" alt="Sheets StorageSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:storage:<latest-version>'
}
```

### Usage

For the default storage sheet use it as following:

    StorageSheet().show(this) {
      fileDisplayMode(FileDisplayMode.HORIZONTAL)
      selectionMode(StorageSelectionMode.FILE)
      onPositive { files -> /* Handle files or folders */ }
    }

| Function                     | Action                                                                                                                                        |
| ---------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| fileDisplayMode()            | Display a file either horizontal or vertical.                                                                                                 |
| fileColumns()                | Specify the amount of colums in which the files are displayed.                                                                                |
| selectionMode()              | Select either files or folders.                                                                                                               |
| selected()                   | Define files that are by default selected.                                                                                                    |
| homeLocation()               | Define the home location. User can not navigate higher than the home location.                                                                |
| currentLocation()            | Define the current location when the sheet is opened.                                                                                         |
| filter()                     | A `FileFilter` to ignore certain files.                                                                                                       |
| multipleChoices()            | Allow multiple choices for files or folders.                                                                                                  |
| displayMultipleChoicesInfo() | Display info of the multiple choices.                                                                                                         |
| minChoices()                 | Set the minimum amount of choices.                                                                                                            |
| maxChoices()                 | Set the maximum amount of choices.                                                                                                            |
| onCreateFolder()             | Pass a listener that is invokes when the user is allowed and intends to create a folder. Return a fitting name through the callback variable. |

## Color

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/color.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/color)

The `Color` Sheet lets you pick a color. Display the default material colors or specify which colors can be choosen from. You can allow to chose a custom color as well.

<details>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/ColorSheet Dialog Templates.png" width="80%" alt="Sheets ColorSheet Dialog"><br/>
<img src="art/ColorSheet Dialog Custom.png" width="80%" alt="Sheets ColorSheet Dialog">

</details>
</br>

<details open>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/ColorSheet BottomSheet Templates.png" width="80%" alt="Sheets ColorSheet BottomSheet"><br/>
<img src="art/ColorSheet BottomSheet Custom.png" width="80%" alt="Sheets ColorSheet BottomSheet">

</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:color:<latest-version>'
}
```

### Usage

For the default color sheet use it as following:

    ColorSheet().show(context) {
      title("Background color")
      onPositive { color ->
        // Use color
      }
    }

| Function                 | Action                                                           |
| ------------------------ | ---------------------------------------------------------------- |
| defaultView()            | Select the default color view (Colors from templates or custom). |
| disableSwitchColorView() | Disable to switch between color views.                           |
| defaultColor()           | Set default selected color.                                      |
| colors()                 | Pass all colors to be displayed in the color templates view.     |
| disableAlpha()           | Disable alpha colors for custom colors.                          |

## Custom

With just the 'core' module you are able to create your own sheet based on this library. You can use some components and styles within your own custom sheet automatically. By default the buttons and toolbar view with logic is ready to be used by your own implementation.

<details>
<br/><br/>
<summary>Showcase as Dialog</summary>

<img src="art/Custom Sheet Dialog.png" width="80%" alt="Sheets Custom Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>Showcase as BottomSheet</summary>

<img src="art/Custom Sheet BottomSheet.png" width="80%" alt="Sheets Custom BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:core:<latest-version>'
}
```

### Get started

You can find a custom sheet implementation in the sample module.

1.  Step: Create a class and extend from the class `Sheet`.

    class CustomSheet : Sheet() {

2.  Step: Implement the method: `onCreateLayoutView` and pass your custom layout.

    override fun onCreateLayoutView(): View {
    return LayoutInflater.from(activity).inflate(R.layout.sheets_custom, null)
    }

All of the base functionality can be used and on top of that you can extend the logic and behavior as you wish.

### Components

You are free to use the components this library uses for it's sheet types.

- `SheetsTitle`
- `SheetsContent`
- `SheetsDigit`
- `SheetsNumericalInput`
- `SheetsDivider`
- `SheetsButton`
- `SheetsEdit`
- `SheetsRecyclerView`
- `SheetsValue`

## Lottie

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/lottie.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/lottie)

The `Lottie` modules gives you the ability to use a [Lottie animations](https://airbnb.design/lottie/) as cover view.

<details open>
<br/>
<br/>
<summary>Showcase as Dialog</summary>

<img src="art/InfoSheet Dialog Cover Lottie Animation.png" width="80%" alt="Sheets InfoSheet">
</details>
</br>

<details>
<summary>Showcase as BottomSheet</summary>

<br/>
<br/>
<img src="art/InfoSheet BottomSheet Cover Lottie Animation.png" width="80%" alt="Sheets InfoSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:lottie:<latest-version>'
}
```

### Usage

You can use the Lottie animation as a cover for any type of sheet.

    InfoSheet().show(this) {
      title("Team Collaboration")
      content("In the world of software projects, it is inevitable...")
      ...
      withCoverLottieAnimation(LottieAnimation {
        setAnimation(R.raw.anim_lottie_business_team)
        ... Setup Lottie animation
      })
      ...
    }

| Function               | Action                |
| ---------------------- | --------------------- |
| playCoverAnimation()   | Play the animation.   |
| resumeCoverAnimation() | Resume the animation. |
| pauseCoverAnimation()  | Pause the animation.  |
| cancelCoverAnimation() | Cancel the animation. |

## Appearance

By default, the library switches to either day or night mode depending on the attr `textColorPrimary`.
By default it uses the activity's colorPrimary. The default `highlightColor` is generated based on the color `sheetsPrimaryColor`, or if not available `colorPrimary`.

### Base

You want a different sheet background shape?
Then just override the corner family and radius.

    <item name="sheetsCornerRadius">12dp</item>
    <item name="sheetsCornerFamily">cut</item>

Just overwrite the base colors, if you want to achieve a different look of the sheets than your app.

    <item name="sheetsPrimaryColor">@color/customPrimaryColor</item>
    <item name="sheetsHighlightColor">@color/customHighlightColor</item>
    <item name="sheetsBackgroundColor">@color/customBackgroundColor</item>
    <item name="sheetsDividerColor">@color/customDividerColor</item>
    <item name="sheetsIconsColor">@color/customIconsColor</item>

You can override the basic style of a sheet. Instead of displaying the toolbar, you can just hide it and display the typical handle.

    <item name="sheetsDisplayHandle">true</item>
    <item name="sheetsDisplayToolbar">false</item>
    <item name="sheetsDisplayCloseButton">false</item>

Change the appearance of the title.

    <item name="sheetsTitleColor">@color/customTitleTextColor</item>
    <item name="sheetsTitleFont">@font/font</item>
    <item name="sheetsTitleLineHeight">@dimen/dimen</item>
    <item name="sheetsTitleLetterSpacing">value</item>

Change the appearance of the content text.

    <item name="sheetsContentColor">@color/customContentTextColor</item>
    <item name="sheetsContentInverseColor">@color/customContentTextInverseColor</item>
    <item name="sheetsContentFont">@font/font</item>
    <item name="sheetsContentLineHeight">@dimen/dimen</item>
    <item name="sheetsContentLetterSpacing">value</item>

Change the appearance of the value texts. (e.g. the time in the TimeSheet & ClockTimeSheet or the selected date & period in the Calendarsheet.)

    <item name="sheetsValueTextActiveColor">@color/customValueTextColor</item>
    <item name="sheetsValueFont">@font/font</item>
    <item name="sheetsValueLineHeight">@dimen/dimen</item>
    <item name="sheetsValueLetterSpacing">value</item>

Change the appearance of the digit keys on the numerical input.

    <item name="sheetsDigitColor">@color/customDigitTextColor</item>
    <item name="sheetsDigitFont">@font/font</item>
    <item name="sheetsDigitLineHeight">@dimen/dimen</item>
    <item name="sheetsDigitLetterSpacing">value</item>

### Buttons

Override the appearance of the button text.

    <item name="sheetsButtonTextFont">@font/font</item>
    <item name="sheetsButtonTextLetterSpacing">value</item>

Override the general appearance of the buttons (negative and positive button).

    <item name="sheetsButtonColor">@color/customButtonColor<item>
    <item name="sheetsButtonTextFont">@font/font<item>
    <item name="sheetsButtonTextLetterSpacing">value<item>
    <item name="sheetsButtonCornerRadius">12dp<item>
    <item name="sheetsButtonCornerFamily">cut<item>
    <item name="sheetsButtonWidth">match_content/wrap_content<item>

Override the appearance of the negative button.

    <item name="sheetsNegativeButtonType">text_button/outlined_button/button<item>
    <item name="sheetsNegativeButtonCornerRadius">12dp<item>
    <item name="sheetsNegativeButtonCornerFamily">cut<item>

Override the appearance of the positive button.

    <item name="sheetsPositiveButtonType">text_button/outlined_button/button<item>
    <item name="sheetsPositiveButtonCornerRadius">12dp<item>
    <item name="sheetsPositiveButtonCornerFamily">cut<item>

Override the border appearance of the outlined button.

    <item name="sheetsButtonOutlinedButtonBorderColor">@color/borderColor<item>
    <item name="sheetsButtonOutlinedButtonBorderWidth">1dp<item>

The corner family and radius is applied to the button shape or in the case of a outlined or text button, to the ripple background shape.

**Fine control**
You can even define the corner family and radius of the negative and positive button for each corner.

    <item name="sheetsNegativeButtonBottomLeftCornerRadius">4dp<item>
    <item name="sheetsNegativeButtonBottomLeftCornerFamily">cut<item>
    ...
    <item name="sheetsPositiveButtonBottomRightCornerRadius">8dp<item>
    <item name="sheetsPositiveButtonBottomRightCornerFamily">rounded<item>

### Handle

The size and the appearance of the handle can be changed like this:

    <item name="sheetsHandleCornerRadius">8dp</item>
    <item name="sheetsHandleCornerFamily">rounded</item>
    <item name="sheetsHandleFillColor">?sheetPrimaryColor</item>
    <item name="sheetsHandleBorderColor">?sheetPrimaryColor</item>
    <item name="sheetsHandleBorderWidth">1dp</item>
    <item name="sheetsHandleWidth">42dp</item>
    <item name="sheetsHandleHeight">4dp</item>

### OptionsSheet

Override appearance of selected options.

    <item name="sheetsOptionSelectedImageColor">@color/customSelectedOptionImageColor</item>
    <item name="sheetsOptionSelectedTextColor">@color/customSelectedOptionTextColor</item>

Override appearance of disabled options.

    <item name="sheetsOptionDisabledImageColor">@color/customDisabledOptionImageColor</item>s
    <item name="sheetsOptionDisabledTextColor">@color/customDisabledOptionImageColor</item>
    <item name="sheetsOptionDisabledBackgroundColor">@color/customDisabledOptionBackgColor</item>

### InputSheet

Override the appearance of the TextInputLayout (used for the InputEditText).

    <item name="sheetsTextInputLayoutCornerRadius">12dp</item>
    <item name="sheetsTextInputLayoutBottomLeftCornerRadius">12dp</item>
    ... and for all other corners
    <item name="sheetsTextInputLayoutEndIconColor">@color/customEndIconColor</item>
    <item name="sheetsTextInputLayoutHelperTextColor">@color/customHelperTextColor</item>
    <item name="sheetsTextInputLayoutBoxStrokeColor">@color/customBoxStrokeColor</item>
    <item name="sheetsTextInputLayoutHintTextColor">@color/customHintTextColor</item>
    <item name="sheetsTextInputLayoutBoxStrokeErrorColor">@color/customBoxStrokeErrorColor</item>
    <item name="sheetsTextInputLayoutErrorTextColor">@color/customErrorTextColor</item>

# Misc

## Support this project
- Leave a star and tell others about it
- Watch for updates and improvements.
- [Open an issue](https://github.com/MaxKeppeler/sheets/issues/) if you see or got any error.
- Leave your thanks [here](https://github.com/MaxKeppeler/sheets/discussions/categories/show-and-tell) and showcase your implementation.
- Donate me a coffee.

## Contribute
1. Open an issue to discuss what you would like to change.
2. Fork the Project
3. Create your feature branch (feature-[some-name])
4. Commit your changes
5. Push to the branch (origin feature-[some-name])
6. Open a pull request

## Donate

Show your appreciation by donating me a coffee. Thank you very much!

<a href="https://ko-fi.com/maxkeppeler" target='_blank'>
 <img width="180" src='https://cdn.ko-fi.com/cdn/kofi2.png?v=2' alt='Buy Me a Coffee at ko-fi.com' />
</a>

<a href="https://www.buymeacoffee.com/maxkeppeler" target="_blank">
    <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" width="160">
</a>

<a href="https://www.paypal.me/maximiliankeppeler" target="_blank">
    <img src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" alt="Donate on PaPal" width="160">
</a>

## Showcase

Check out some apps which are using this library.<br/>

- [Aquafy](http://aquafy-mk.com) - Beautiful hydration tracker and reminder.
- [Awake](http://awake-mk.com) - Intelligent alarms and wake-up challenges and sleep tracking to improve your daily sleep and day-time quality.
- [Sign for Spotify](https://play.google.com/store/apps/details?id=com.mk.sign.spotifyv2) - Playlist and control widgets for Spotify content.

- [Buddha Quotes](https://play.google.com/store/apps/details?id=org.bandev.buddhaquotes) - Open Source Buddha Quotes.

## License

    Copyright 2020 Maximilian Keppeler https://maxkeppeler.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
