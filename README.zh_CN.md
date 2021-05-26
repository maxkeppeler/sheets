# Sheets

<p>

  <img src="art/ic_library.png" width="96px" height="96px" alt="Sheets Library" align="left" style="margin-right: 24px; margin-bottom: 24px">

  <p>

流畅的对话框和底部表单，可在你的应用程序中快速使用，选择一个可用的表或在现有功能的基础上建立自定义表

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

[English](README.md) [简体中文](README.zh_CN.md).


<img src="art/showcase.png" alt="sheetss Library">

## 目录

- [Get started](#get-started)
  - [Info Sheet](#info)
  - [Options Sheet](#options)
  - [Clock Time Sheet](#clock-time)
  - [Time Sheet](#time)
  - [Input Sheet](#input)
  - [Calendar Sheet](#calendar)
  - [Color Sheet](#color)
  - [Custom Sheet](#custom)
  - [Lottie](#lottie)
  - [Appearance](#appearance)
- [Misc](#misc)
  - [Showcase](#showcase)
  - [Support this project](#support-this-project)
  - [Credits](#credits)
  - [License](#license)

# 开始

一个 `sheet` 可以动态地显示为一个对话框或一个底部表单

[例子](https://github.com/MaxKeppeler/sheets/blob/main/sample/sample.apk).

你必须使用 `core` 模块，因为它是任何工作表的基础

在 `build.gradle` 中:

```gradle
repositories {
  ...
  mavenCentral()
}
```

在 app `build.gradle` 中:

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/core.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/core)

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:core:<latest-version>'
}
```

**基础功能** <br/>
以下函数可以从任何类型的表中调用.

| 方法                   | 功能                                                                             |
|-----------------------|----------------------------------------------------------------------------------|
| style()               | 显示为对话框或者底层表单 |
| title()               | 设置标题文本 |
| titleColor()          | 设置标题文本颜色 |
| titleColorRes()       | 通过一个 `Resource` 来设置标题文本颜色 |
| withCoverImage()      | 添加一个封面图片|
| topStyle()            | 指定封面图片和 `topBar` 的样式 |
| positiveButtonStyle() | 定义确认按钮的样式（文本、填充、轮廓) |
| negativeButtonStyle() | 定义否定按钮的样式（文本、填充、轮廓）|
| withIconButton()      | 在 `topBar` 中最多添加3个图标按钮 |
| closeIconButton()     | 设置一个自定义的 `closeIconButton` |
| displayHandle()       | 显示可调节窗口大小的控件（位于右下角） |
| displayCloseButton()  | 显示 `closeIconButton` |
| displayToolbar()      | 显示工具栏 (`closeIconButton`, `title`, `divider` and `icon` `buttons`) |
| peekHeight()          | Set the peek height. (Only bottom-sheet)|
| cornerRadius()        | 设置转角半径 |
| cornerFamily()        | 设置转角的样式 （裁剪或圆角）|
| borderWidth()         | 设置边框宽度 |
| borderColor()         | 设置边框颜色 |
| cancelableOutside()   | 使 `sheet` 可以在对话框视图之外取消 |
| onNegative()          | 设置否定按钮的文本和监听|
| onPositive()          | 设置确认按钮的文本和监听|
| onDismiss()           | 设置一个监听器，当 `sheet` 被撤消时被调用|
| onCancel()            | 设置一个监听器，当 `sheet` 被取消时被调用（仅当可取消时）|
| onClose()             | 设置一个监听器，当 `sheet` 关闭时被调用|
| show()                | show the sheet|

每个工作表都有一个扩展功能，叫做 `build `和 `show`。<br/>

使用 `build` 来构建一个 sheet 并在之后显示出来。

    val sheet = InfoSheet().build(context) {
      // build sheet
    }

    sheet.show() // Show sheet when ready

如果你想构建它，并且立刻显示出来，请使用 `show`

    InfoSheet().show(context) {
      // build sheet
    } // Show sheet

## Info

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/info.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/core)

`Info sheet` 可以让你显示信息或警告

<details open>
<br/>
<br/>
<summary>Dialog 的演示</summary>

<img src="art/InfoSheet Dialog.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Top.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Bottom.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Mixed.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>

</details>
</br>

<details>
<summary>BottomSheet 的演示</summary>

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

### 使用方法

对于默认的 `info sheet`，使用方法如下：

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

| 方法       | 行为           |
| --------------- | ------------------- |
| content()       | 设置内容文本   |
| drawable()      | Set drawable.       |
| drawableColor() | Set drawable color. |

## Options

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/options.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/options)

`Options sheet` 可以让你显示一个网格或选项列表.

<details open>
<br/><br/>
<summary>Dialog 的演示</summary>

<img src="art/OptionsSheet Dialog Grid Middle.png" width="80%" alt="Sheets OptionsSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>BottomSheet 的演示</summary>

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

### 使用方法

对于默认的 `options sheet`，按以下方式使用：

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
| multipleChoices()            | 允许多选内容|
| displayMultipleChoicesInfo() | 显示多项选择的信息|
| maxChoicesStrictLimit()      | Specify that the max choices is strict and more choices can't be selected temporarily. |
| minChoices()                 | 设置选择的最小数量|
| maxChoices()                 | 设置最大的选择数量|
| onPositiveMultiple()         | 设置多重选择的监听器|
| displayButtons()             | 显示按钮，并且需要一个确认按钮点击进行选择
| displayMode()                | 以 list/vertical/horizontal 的可滚动网格显示选项|

**可选的**

| 方法   | 行为 |
| ---------- | -------------------- |
| selected() | 预先选择一个选项|
| disable()  | 禁用一个选项|

**注意**。预选的选项会自动增加当前的选择，而禁用的选项会减少最大的选择数量。

## Clock Time

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/time-clock.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/time-clock)

`Clock Time` sheet 可以让你快速的选择时间

<details>
<br/><br/>
<summary>Dialog 的演示</summary>

<img src="art/ClockTimeSheet Dialog.png" width="80%" alt="Sheets OptionsSheet Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>BottomSheet 的演示</summary>

<img src="art/ClockTimeSheet BottomSheet.png" width="80%" alt="Sheets OptionsSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:time-clock:<latest-version>'
}
```

### 使用方法

对于默认的 `Clock time` sheet，以 `24` 小时的格式，使用方法如下：

    ClockTimeSheet().show(context) {
      title("Wake-up time")
      onPositive { clockTimeInMillis: Long ->
        // Handle selected time
      }
    }

| Function        | Action                                |
| --------------- | ------------------------------------- |
| format24Hours() | 使用 24 小时格式或者 12 小时格式。     |
| currentTime()   | 以毫秒为单位设置当前时间。 |

## Time

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/time.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/time)

`Time` sheet 可以让你以特定的格式选择一个持续时间。

<details open>
<br/><br/>
<summary>Dialog 的演示</summary>

<img src="art/TimeSheet Dialog.png" width="80%" alt="Sheets TimeSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>BottomSheet 的演示</summary>

<img src="art/TimeSheet BottomSheet.png" width="80%" alt="Sheets TimeSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:time:<latest-version>'
}
```

### 使用方法

对于默认的 `Time` sheet，按以下方式使用。

    TimeSheet().show(context) {
      title("Snooze time")
      onPositive { durationTimeInMillis: Long ->
        // Handle selected time
      }
    }

| Function      | Action                                          |
| ------------- | ----------------------------------------------- |
| format()      | 选择时间格式。(hh:mm:ss, mm:ss, ...) |
| currentTime() | 设置当前时间，以秒为单位。                |
| minTime()     | 设置最短的时间。                           |
| maxTime()`    | 设置最长时间。                          |

## Input

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/input.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/input)

`input sheet` 可以让你显示一个由各种 input 组成的表单:

<details>
<br/><br/>
<summary>Dialog 的演示</summary>

<img src="art/InputSheet Dialog Short.png" width="80%" alt="Sheets InputSheet Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>BottomSheet 的演示</summary>

<img src="art/InputSheet BottomSheet Short.png" width="80%" alt="Sheets InputSheet BottomSheet">
</details>

<br/>
<details>
<br/><br/>
<summary>一些关于 `Dialogs` 的其他形式</summary>

<img src="art/InputSheet Dialog Long.png" width="80%" alt="Sheets InputSheet Dialog"><br/>

</details>
</br>

<details>
<br/><br/>
<summary>一些关于 `BottomSheet` 的其他形式</summary>

<img src="art/InputSheet BottomSheet Long.png" width="80%" alt="Sheets InputSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:input:<latest-version>'
}
```

### 使用方法

对于默认的 `input sheet`，按以下方式使用：

    InputSheet()).show(context) {
        title("Short survey")
        content("We would like to ask you some questions reading your streaming platform usage.")
      with(InputEditText {
        required())
        label("Your favorite TV-Show")
        hint("The Mandalorian, ...")
        validationListener { value -> } // 添加自定义验证逻辑
        changeListener { value -> } // 输入的值被改变时
        resultListener { value -> } // 当表单完成后输入的值被改变时
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

| 函数  | 功能                                       |
| --------- | --------------------------------------------- |
| with()    | 添加一个 input。(见输入选项)             |
| content() | 设置内容文本。(例如，解释一项调查 |

**Input options:**

- `InputEditText`
- `InputCheckBox`
- `InputRadioButtons`
- `InputSpinner`

**Input**<br/>

| 函数         | 功能                           |
| ---------------- | -------------------------------- |
| label()          | 设置标签文本           |
| drawable()       | Set the drawable.                |
| required()       | Mark input as required.          |
| changeListener() | 设置监听器来观察变化。 |
| resultListener() | 为最终值设置监听器。   |

**InputEditText**<br/>

| 函数             | 功能                                            |
| -------------------- | ------------------------------------------------- |
| hint()               | 设置 hint 文本。                                |
| defaultValue()       | 设置默认文本。                                 |
| inputType()          | Set the `android.text.InputType`'s。               |
| inputFilter()        | Set the `android.text.inputFilter`                |
| maxLines()           | 设置最大行数。                      |
| endIconMode()        | Set TextInputLayout.EndIconMode。                  |
| endIconActivated()   | Set the EndIcon activated。                        |
| passwordVisible()    | 使密码最初可见或不可见。 |
| validationListener() | 用你自己的逻辑验证文本输入。      |

**InputCheckBox** <br/>

| 函数       | 功能             |
| -------------- | ------------------ |
| text()         | 设置文本。     |
| defaultValue() | 设置默认的值。 |

**InputRadioButtons** <br/>

| 函数   | 功能                             |
| ---------- | ---------------------------------- |
| options()  | 设置一个RadioButton的选项列表。 |
| selected() | 设置一个选定的索引。              |

**InputSpinner** <br/>

| 函数          | 功能                                                   |
| ----------------- | --------------------------------------------------------- |
| noSelectionText() | 设置在没有选择时显示的文本。 |
| options()         | 设置一个选项列表。                                    |
| selected()        | 设置一个选定的索引。                                     |

## Calendar

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/calendar.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.calendar/core)

`Calendar` 表可以让你选择一个日期或日期范围。这种类型是使用 [CalendarView](https://github.com/kizitonwose/CalendarView) 库

<details open>
<br/><br/>
<summary>Dialog 的演示</summary>

<img src="art/CalendarSheet Dialog Period.png" width="80%" alt="Sheets CalendarSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>BottomSheet 的演示</summary>

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
| disableTimeline() |  Disable either past or future dates.                            |
| rangeYears()      | Set the range of years into past and future.                     |
| disable()         | Pass a `Calendar` object to disable various dates for selection. |
| displayButtons()  | Show or hide the buttons view.                                   |

## Color

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/color.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/color)

The `Color` Sheet lets you pick a color. Display the default material colors or specify which colors can be choosen from. You can allow to chose a custom color as well.

<details>
<br/><br/>
<summary>Dialog 的演示</summary>

<img src="art/ColorSheet Dialog Templates.png" width="80%" alt="Sheets ColorSheet Dialog"><br/>
<img src="art/ColorSheet Dialog Custom.png" width="80%" alt="Sheets ColorSheet Dialog">

</details>
</br>

<details open>
<br/><br/>
<summary>BottomSheet 的演示</summary>

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
<summary>Dialog 的演示</summary>

<img src="art/Custom Sheet Dialog.png" width="80%" alt="Sheets Custom Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>BottomSheet 的演示</summary>

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
<summary>Dialog 的演示</summary>

<img src="art/InfoSheet Dialog Cover Lottie Animation.png" width="80%" alt="Sheets InfoSheet">
</details>
</br>

<details>
<summary>BottomSheet 的演示</summary>

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
By default it uses the activity's colorPrimary. The default `highlightColor` is generated based on the color `sheetPrimaryColor`, or if not available `colorPrimary`.

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

## Showcase

Check out some real apps which use this library.<br/>
Feel free to hit me up to include your app here.

- [Sign for Spotify](https://play.google.com/store/apps/details?id=com.mk.sign.spotifyv2) - Playlist and control widgets for Spotify on your home screen. (Uses: `Info`, `Options`, `Input`, `Color`)

- [Buddha Quotes](https://play.google.com/store/apps/details?id=org.bandev.buddhaquotes) - A collaborative project to create a Free and Open Source Buddha Quotes app for Android with a focus on privacy. (Uses: `Options`, `Input`, `Color`, `Time`)

## Support this project

- Leave a **Star** and tell other devs about it.

- **Watch** for updates and improvements.

- **[Open an issue](https://github.com/MaxKeppeler/sheets/issues/)** if you see or got any error.

- Leave your thanks [here](https://github.com/MaxKeppeler/sheets/discussions/categories/show-and-tell) and showcase your implementation.

## Motivation

I created several sheets for my apps [Sign for Spotify](https://play.google.com/store/apps/details?id=com.mk.sign.spotifyv2) and [Awake](https://play.google.com/store/apps/details?id=com.mk.awake) in the recent months.
I especially wanted to have a 'writable' clock time and duration time picker in form of a sheet
This is my first library - I'm happy about any feedback, tips etc. I hope you like it and can make use of it. :)

## Credits

- Thanks to [Sasikanth](https://github.com/msasikanth) for inspiration regarding the the appearance of the sheets through [Color Sheet](https://github.com/msasikanth/ColorSheet) and [Memoire](https://play.google.com/store/apps/details?id=com.primudesigns.stories).
- Thanks to [Aidan Follestad](https://github.com/afollestad) and his [material-dialogs](https://github.com/afollestad/material-dialogs) library for the inspiration to make this library modular.

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
