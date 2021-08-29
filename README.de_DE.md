# Sheets

<p>

  <img src="art/ic_library.png" width="96px" height="96px" alt="Sheets Library" align="left" style="margin-right: 24px; margin-bottom: 24px">

  <p>

Elegante Dialoge und BottomSheets zur schnellen Verwendung in deiner App. Wähle bereits existierende Sheets (Blätter) aus oder erstelle dein eigenes auf Basis vorhandener Funktionen.

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

Lese in [English](README.md) oder [简体中文](README.zh_CN.md).

<img src="art/showcase.png" alt="sheetss Library">

## Übersicht

- [Erste Schritte](#erste-schritte)
  - [Info Sheet](#info)
  - [Options Sheet](#options)
  - [Clock Time Sheet](#clock-time)
  - [Time Sheet](#time)
  - [Input Sheet](#input)
  - [Calendar Sheet](#calendar)
  - [Color Sheet](#color)
  - [Eigenges Sheet](#custom)
  - [Lottie](#lottie)
  - [Aussehen](#aussehen)
- [Sonstiges](#sonstiges)
  - [Unterstütze das Projekt](#unterstütze-das-projekt)
  - [Lizenz](#lizenz)

# Erste Schritte

Ein Sheet (Blatt) kann dynamisch entweder als Dialog oder als BottomSheet dargestellt werden.
Probiere das [sample](https://github.com/MaxKeppeler/sheets/blob/main/sample/sample.apk) aus.

In jedem Fall musst du das `core` Modul implementieren, welches das Fundament für alle Arten von Sheets ist.

In deiner übergeordneten `build.gradle` Datei:

```gradle
repositories {
  ...
  mavenCentral()
}
```

In deiner App `build.gradle` Datei:

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/core.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/core)

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:core:<aktuelle-version>'
}
```

**Basisfunktionen** <br/>
Folgenden Funktionen können von jeder Art von Sheet aufgerufen werden.

| Funktion              | Aktion                                                                                  |
| --------------------- | --------------------------------------------------------------------------------------- |
| style()               | Stelle das Sheet entweder als Dialog oder BottomSheet dar.                              |
| title()               | Setzt den Text für den Titel.                                                           |
| titleColor()          | Setze die Farbe für den Titel.                                                          |
| titleColorRes()       | Setze die Farbe für den Titel mit einer Ressource ID.                                   |
| withCoverImage()      | Fügt ein Titelbild hinzu.                                                               |
| topStyle()            | Setze den Stil für das Titelbild und der Leiste.                                        |
| positiveButtonStyle() | Setze den Stil für den positiven Button (Text, Filled, Outlined).                       |
| negativeButtonStyle() | Setze den Stil für den negativen Button (Text, Filled, Outlined).                       |
| withIconButton()      | Füge bis zu 3 Icon-Buttons zur oberen Leiste hinzu.                                     |
| closeIconButton()     | Setze eine eigenen Schließen-Icon-Button.                                               |
| displayHandle()       | Stelle einen Griff, typisch für BottomSheets, dar.                                      |
| displayCloseButton()  | Stelle den Schließen-Button dar.                                                        |
| displayToolbar()      | Stelle die obere Leiste dar. (Schließen-Button, Titel, Teiler und weitere Icon-Buttons. |
| peekHeight()          | Setze die peek-Höhe. (Nur für BottomSheet)                                              |
| cornerRadius()        | Setze den Radius für die Ecken.                                                         |
| cornerFamily()        | Setze die Familie für die Ecken. (Cut oder rounded)                                     |
| borderWidth()         | Setze die Breite für den Rand.                                                          |
| borderColor()         | Setze die Farbe für den Rand.                                                           |
| cancelableOutside()   | Definiere, ob das Sheet abgebrochen werden kann, wenn man außerhalb der Fläche klickt.  |
| onNegative()          | Setze den Text und einen Listener für den negativen Button.                             |
| onPositive()          | Setze den Text und einen Listener für den positiven Button.                             |
| onDismiss()           | Setze einen Listener, der ausgelöst wird, wenn der Dialog beendet wird.                 |
| onCancel()            | Setze einen Listener, der ausgelöst wird, wenn der Dialog abgebrochen wird.             |
| onClose()             | Setze einen Listener, der ausgelöst wird, wenn der Dialog geschlossen wird.             |
| show()                | Stelle das Sheet dar.                                                                   |

Jedes Sheet hat eine Erweiterungsfunktion namens `build` und `show`.<br/>

Benutze `build` um ein Sheet aufzubauen und später darzustellen.

```
val sheet = InfoSheet().build(context) {
  // aufbauen
}

sheet.show() // Später darstellen
```

Benutze `show` um ein Sheet aufzubauen und direkt darzustellen.

```
InfoSheet().show(context) {
  // aufbauen
} // darstellen
```

## Info

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/info.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/core)

Das `Info` Sheet ermöglicht es dir, Informationen und Warnungen darzustellen.

<details open>
<br/>
<br/>
<summary>Als Dialog darstellen</summary>

<img src="art/InfoSheet Dialog.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Top.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Bottom.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>
<img src="art/InfoSheet Dialog Cover TopStyle Mixed.png" width="80%" alt="Sheets InfoSheet Dialog"><br/>

</details>
</br>

<details>
<summary>Als BottomSheet darstellen</summary>

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
  implementation 'com.maxkeppeler.sheets:info:<aktuelle-version>'
}
```

### Verwendung

Ein einfaches InfoSheet kann folgenderweise erstellt werden:

```
InfoSheet().show(context) {
  title("Der Text für den Titel")
  content("Der Text für den Inhalt")
  onNegative("Nein") {
    // Aktion verarbeiten
  }
  onPositive("Ja") {
    // Aktion verarbeiten
  }
}
```

| Funktion        | Aktion                             |
| --------------- | ---------------------------------- |
| content()       | Setze den Text für den Inhalt.     |
| drawable()      | Setze ein Symbol links vom Inhalt. |
| drawableColor() | Setze die Farbe für das Symbol.    |
| customView() | Verwende eine eigene View. |

## Options

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/options.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/options)

Das `Options` Sheet ermöglicht es dir, eine oder mehrere Optionen auszuwählen.

<details open>
<br/><br/>
<summary>Als Dialog darstellen</summary>

<img src="art/OptionsSheet Dialog Grid Middle.png" width="80%" alt="Sheets OptionsSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>Als BottomSheet darstellen</summary>

<img src="art/OptionsSheet BottomSheet Grid Middle.png" width="80%" alt="Sheets OptionsSheet BottomSheet">
</details>

<br/>
<details>
<br/><br/>
<summary>Varianten als Dialog darstellen</summary>

<img src="art/OptionsSheet Dialog List.png" width="80%" alt="Sheets OptionsSheet Dialog"><br/>
<img src="art/OptionsSheet Dialog Grid Small.png" width="80%" alt="Sheets OptionsSheet" Dialog><br/>
<img src="art/OptionsSheet Dialog Grid Large Horizontal.png" width="80%" alt="Sheets OptionsSheet" Dialog><br/>

</details>
</br>

<details>
<br/><br/>
<summary>Varianten als BottomSheet darstellen</summary>

<img src="art/OptionsSheet BottomSheet List.png" width="80%" alt="Sheets OptionsSheet BottomSheet"><br/>
<img src="art/OptionsSheet BottomSheet Grid Small.png" width="80%" alt="Sheets OptionsSheet" BottomSheet><br/>
<img src="art/OptionsSheet BottomSheet Grid Large Horizontal.png" width="80%" alt="Sheets OptionsSheet" BottomSheet><br/>

</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:info:<aktuelle-version>'
}
```

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:options:<aktuelle-version>'
}
```

### Verwendung

Ein einfaches OptionsSheet kann folgenderweise erstellt werden:

```
OptionsSheet().show(context) {
  title("Der Text für den Titel")
  with(
    Option(R.drawable.ic_copy, "Kopieren"),
    Option(R.drawable.ic_translate, "Übersetzen"),
    Option(R.drawable.ic_paste, "Einfügen")
  )
  onPositive { index: Int, option: Option ->
    // Aktion verarbeiten
  }
}
```

| Funktion s                   | Aktion                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------- |
| multipleChoices()            | Erlaube mehrere Auswahlmöglichkeiten.                                                       |
| displayMultipleChoicesInfo() | Stelle Informationen über die Auswahlmöglichkeiten dar.                                     |
| maxChoicesStrictLimit()      | Setze, dass die maximale Auswahl strikt ist und temporär nicht mehr ausgewählt werden darf. |
| minChoices()                 | Lege die minimum Anzahl an Möglichkeiten fest.                                              |
| maxChoices()                 | Lege die maximale Anzahl an Möglichkeiten fest.                                             |
| onPositiveMultiple()         | Setze einen Listener für mehrere Auswahlmöglichkeiten.                                      |
| displayButtons()             | Stelle die Buttons dar und erzwinge einen Button-Klick um die Auswahl zu bestätigen.        |
| displayMode()                | Stelle die Optionen als Liste oder in einem vertikalen oder horizontalen Gitter dar.        |
| preventIconTint()                | (Global) Prevents the lib to use a tint for the icons. Keeps the default colors of a drawable.           |

**Option**

| Funktion   | Aktion                              |
| ---------- | ----------------------------------- |
| selected() | Markiere die Option als ausgewählt. |
| disable()  | Markie die Option als deaktiviert.  |
| preventIconTint()                | (Local) Prevents the lib to use a tint for the icons. Keeps the default colors of a drawable.           |

**Wichtig**: Vorausgewählte Optionen erhöhen automatisch die aktuelle Auswahl, während deaktivierte Optionen die maximale Auswahl verringern.

## Clock Time

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/time-clock.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/time-clock)

Das `Clock Time` Sheet ermöglicht es dir, eine Uhrzeit auszuwählen.

<details>
<br/><br/>
<summary>Als Dialog darstellen</summary>

<img src="art/ClockTimeSheet Dialog.png" width="80%" alt="Sheets OptionsSheet Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>Als BottomSheet darstellen</summary>

<img src="art/ClockTimeSheet BottomSheet.png" width="80%" alt="Sheets OptionsSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:time-clock:<aktuelle-version>'
}
```

### Verwendung

Ein ClockTimeSheet kann folgenderweise erstellt werden:

```
ClockTimeSheet().show(context) {
  title("Der Text für den Titel")
  onPositive { clockTimeInMillis: Long ->
    // Vearbeite Aktion
  }
}
```

| Funktion        | Aktion                                                  |
| --------------- | ------------------------------------------------------- |
| format24Hours() | Benutze entweder das 24-Stunden oder 12-Stunden Format. |
| currentTime()   | Setze die aktuelle Zeit in Millisekunden.               |

## Time

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/time.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/time)

Das `Time` Sheet ermöglicht es dir, eine Zeit bzw. eine Dauer in einem spezfischen Format auszuwählen.

<details open>
<br/><br/>
<summary>Als Dialog darstellen</summary>

<img src="art/TimeSheet Dialog.png" width="80%" alt="Sheets TimeSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>Als BottomSheet darstellen</summary>

<img src="art/TimeSheet BottomSheet.png" width="80%" alt="Sheets TimeSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:time:<aktuelle-version>'
}
```

### Verwendung

Ein TimeSheet kann folgenderweise erstellt werden:

```
TimeSheet().show(context) {
  title("Der Text für den Titel")
  onPositive { durationTimeInMillis: Long ->
    // Verarbeite Aktion
  }
}
```

| Funktion      | Aktion                                   |
| ------------- | ---------------------------------------- |
| format()      | Setze das Format. (hh:mm:ss, mm:ss, ...) |
| currentTime() | Setze die aktuelle Zeit in Sekunden.     |
| minTime()     | Setze die mindest Zeit.                  |
| maxTime()`    | Setze die maximale Zeit.                 |

## Input

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/input.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/input)

Das `Input` Sheet ermöglicht es dir, ein Formular mit verschiedenen Eingabemöglichkeiten zu erstellen.

<details>
<br/><br/>
<summary>Als Dialog darstellen</summary>

<img src="art/InputSheet Dialog Short.png" width="80%" alt="Sheets InputSheet Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>Als BottomSheet darstellen</summary>

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
  implementation 'com.maxkeppeler.sheets:input:<aktuelle-version>'
}
```

### Verwendung

Ein einfaches InputSheet kann folgenderweise erstellt werden:

    InputSheet()).show(context) {
        title("Umfrage")
        content("Wir möchten dir einige Fragen zur Nutzung von Streaming-Plattform stellen.")
      with(InputEditText {
        required())
        label("Deine Lieblingsfernsehshow")
        hint("The Mandalorian, ...")
        validationListener { value -> } // Füge eigene Validationslogik hinzu
        changeListener { value -> } // Eingabe-Weert hat sich geändert
        resultListener { value -> } // Eingabe-Wert bei Abschluss des Formulars
      })
      with(InputCheckBox("binge_watching") { // Lese die Werte entweder durch den Index oder einen Schlüssel aus
        label("Binge Watching")
        text("Ich schaue mir regelmäßig Shows auf Netflix an.")
        // ... mehr Optionen
      })
      with(InputRadioButtons() {
        required()
        label("Streaming-Plattform nach deiner Wahl")
        options(mutableListOf("Netflix", "Amazon", "Andere"))
      })
      // ... mehr Optionen
      onNegative { showToast("InputSheet abgebrochen", "Kein Ergebnis") }
      onPositive { result ->
          showToastLong("InputSheet Ergebnis", result.toString())
          val text = result.getString("0") // Lese Wert durch Index aus
          val check = result.getBoolean("binge_watching") // Lese Wert durch Schlüssel aus
      }
    }

| Funktion  | Aktion                                             |
| --------- | -------------------------------------------------- |
| with()    | Füge Eingabemethoden hinzu.                        |
| content() | Setze den Text für die Beschreibung des Formulars. |

**Eingabemöglichkeiten:**

- `InputEditText`
- `InputCheckBox`
- `InputRadioButtons`
- `InputSpinner`

**Input Basisfunktionen**<br/>

| Funktion         | Aktion                                                               |
| ---------------- | -------------------------------------------------------------------- |
| label()          | Setze den Text für die Überschrift.                                  |
| drawable()       | Setze ein Symbol.                                                    |
| required()       | Markiere Eingabe als notwendig.                                      |
| changeListener() | Setze einen Listener, um über Änderungen informiert zu werden.       |
| resultListener() | Setze einen Listener, um über den finalen Wert informiert zu werden. |

**InputEditText**<br/>

| Funktion             | Aktion                                   |
| -------------------- | ---------------------------------------- |
| hint()               | Setze den Text für den Hinweis.          |
| defaultValue()       | Setze den Standardtext.                  |
| inputType()          | Setze die `android.text.InputType`s.     |
| inputFilter()        | Setze den `android.text.inputFilter`     |
| maxLines()           | Setze die maximale Anzahl an Zeilen.     |
| endIconMode()        | Setze den `TextInputLayout.EndIconMode`. |
| endIconActivated()   | Aktiviere den EndIconMode.               |
| passwordVisible()    | Mache das Passwort inital sichtbar.      |
| validationListener() | Validiere den Text mit eigener Logik.    |

**InputCheckBox** <br/>

| Funktion       | Aktion                  |
| -------------- | ----------------------- |
| text()         | Setze den Text.         |
| defaultValue() | Setze den Standardwert. |

**InputRadioButtons** <br/>

| Funktion   | Aktion                                     |
| ---------- | ------------------------------------------ |
| options()  | Setze eine Liste von RadioButton Optionen. |
| selected() | Setze den Standard-Index.                  |

**InputSpinner** <br/>

| Funktion          | Aktion                                                                |
| ----------------- | --------------------------------------------------------------------- |
| noSelectionText() | Setze den Text, welcher dargestellt wird, wenn nichts ausgewählt ist. |
| options()         | Setze eine Liste von Optionen.                                        |
| selected()        | Setze den Standard-Index.                                             |

## Calendar

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/calendar.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.calendar/core)

Das `Calendar` Sheet ermöglicht es dir, ein Datum oder einen Zeitraum auszuwählen.

<details open>
<br/><br/>
<summary>Als Dialog darstellen</summary>

<img src="art/CalendarSheet Dialog Period.png" width="80%" alt="Sheets CalendarSheet Dialog">
</details>
</br>

<details>
<br/><br/>
<summary>Als BottomSheet darstellen</summary>

<img src="art/CalendarSheet BottomSheet Period.png" width="80%" alt="Sheets CalendarSheet BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:calendar:<aktuelle-version>'
}
```

### Verwendung

Ein CalendarSheet kann folgenderweise erstellt werden:

    CalendarSheet().show(this) {
      title("Wann hast du Geburtstag?")
      onPositive { dateStart, dateEnd ->
        // Verarbeite ergebnis
      }

| Funktion          | Aktion                                                                  |
| ----------------- | ----------------------------------------------------------------------- |
| selectionMode()   | Wähle den Auswahl-Modus aus. (Datum oder Zeitraum).                     |
| calendarMode()    | Wähle den Kalendar-Ansichtsmodus aus (1-3 Wochen oder Monatsansicht).   |
| disableTimeline() | Deaktiviere vergangene oder zukünftige Tage.                            |
| rangeYears()      | Setze den möglichen Zeitraum in Jahre in die Vergangenheit und Zukunft. |
| disable()         | Deaktiviere bestimmte Kalendertage mit einem `Calendar` Objekt.         |
| displayButtons()  | Stelle Buttons dar.                                                     |

## Color

[ ![Download](https://img.shields.io/maven-central/v/com.maxkeppeler.sheets/color.svg?label=Maven%20Central) ](https://search.maven.org/artifact/com.maxkeppeler.sheets/color)

Das `Color` Sheet ermöglicht es dir, eine Farbe auszuwählen oder zu erstellen.

<details>
<br/><br/>
<summary>Als Dialog darstellen</summary>

<img src="art/ColorSheet Dialog Templates.png" width="80%" alt="Sheets ColorSheet Dialog"><br/>
<img src="art/ColorSheet Dialog Custom.png" width="80%" alt="Sheets ColorSheet Dialog">

</details>
</br>

<details open>
<br/><br/>
<summary>Als BottomSheet darstellen</summary>

<img src="art/ColorSheet BottomSheet Templates.png" width="80%" alt="Sheets ColorSheet BottomSheet"><br/>
<img src="art/ColorSheet BottomSheet Custom.png" width="80%" alt="Sheets ColorSheet BottomSheet">

</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:color:<aktuelle-version>'
}
```

### Verwendung

Ein ColorSheet kann folgenderweise erstellt werden:

    ColorSheet().show(context) {
      title("Background color")
      onPositive { color ->
        // Use color
      }
    }

| Funktion                 | Aktion                                                         |
| ------------------------ | -------------------------------------------------------------- |
| defaultView()            | Setze die Standard-Ansicht (Farbvorlagen oder Farberstellung). |
| disableSwitchColorView() | Deaktiviere das Wechseln zwischen den Ansichten.               |
| defaultColor()           | Setze die Standard-Farbe.                                      |
| colors()                 | Setze alle Farbe für die Farbvorlagen-Ansicht.                 |
| disableAlpha()           | Deaktiviere Transparenz bei der Farberstellung.                |

## Custom

Mit dem 'core' Modul kannst du auf Basis dieser Bibliothek dein eigenes Sheet (dialog und BottomSheet) erstellen.
Alle Basisfunktionen und Anpassmöglichkeiten sind daher automatisch für deine eigenen Implementationen verfügbar.

<details>
<br/><br/>
<summary>Als Dialog darstellen</summary>

<img src="art/Custom Sheet Dialog.png" width="80%" alt="Sheets Custom Dialog">
</details>
</br>

<details open>
<br/><br/>
<summary>Als BottomSheet darstellen</summary>

<img src="art/Custom Sheet BottomSheet.png" width="80%" alt="Sheets Custom BottomSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:core:<aktuelle-version>'
}
```

### Erste Schritte

Im `sample` befindet sich ein ausführliches Beispiel, wie man eigenene Sheets erstellt.

1.  Schritt: Erstelle eine Klasse und erweitere diese durch die Klasse `Sheet`.

```
class CustomSheet : Sheet() {
```

2.  Schritt: Implementiere die Methode `onCreateLayoutView` und gebe dein eigenes Layout zurück.

```
override fun onCreateLayoutView(): View {
  return LayoutInflater.from(activity).inflate(R.layout.sheet_eigenes, null)
}
```

Alle Basisfunktionen können verwendet werden.
Darüber hinaus kannst du nun dieses Sheet mit einer eigenen Logik und eigenem Verhalten nach deinen Wünschen erweitern.

### Komponenten

Du kannst auch die gleichen Kompomenenten verwenden, welche die Bibliothek verwendet.

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

Das `Lottie` Modul ermöglicht es dir, wunderschöne [Lottie Animationen](https://airbnb.design/lottie/) im Titelbild darzustellen.

<details open>
<br/>
<br/>
<summary>Als Dialog darstellen</summary>

<img src="art/InfoSheet Dialog Cover Lottie Animation.png" width="80%" alt="Sheets InfoSheet">
</details>
</br>

<details>
<summary>Als BottomSheet darstellen</summary>

<br/>
<br/>
<img src="art/InfoSheet BottomSheet Cover Lottie Animation.png" width="80%" alt="Sheets InfoSheet">
</details>

```gradle
dependencies {
  ...
  implementation 'com.maxkeppeler.sheets:lottie:<aktuelle-version>'
}
```

### Verwendung

Du kannst zu jedem Sheet eine Lottie-Animation hinzufügen:

```
InfoSheet().show(context) {
  title("...")
  content("...")
  ...
  withCoverLottieAnimation(LottieAnimation {
    setAnimation(R.raw.anim_lottie_business_team)
    ... Bereite die Animation auf
  })
  ...
}
```

| Funktion               | Aktion                              |
| ---------------------- | ----------------------------------- |
| playCoverAnimation()   | Spiele die Animation ab.            |
| resumeCoverAnimation() | Abspielen der Animation fortsetzen. |
| pauseCoverAnimation()  | Pausiere die Animation.             |
| cancelCoverAnimation() | Breche die Animation ab.            |

## Aussehen

Standardmäßig wechselt die Bibliothek je nach attr `textColorPrimary` entweder in den Tag- oder den Nachtmodus.
Standardmäßig wird das `colorPrimary` der Aktivität verwendet. Die Standard-`highlightColor` wird basierend auf der Farbe `sheetPrimaryColor` oder, falls nicht verfügbar, durch `colorPrimary` generiert.

### Basis

Du möchtest verschiedene Hintergrundformen verwenden?
Dann überschreiben Sie einfach die Eckfamilie und den Radius.

    <item name="sheetsCornerRadius">12dp</item>
    <item name="sheetsCornerFamily">cut</item>

Überschreiben einfach die Grundfarben, wenn due ein anderes Aussehen erzielen möchten.

    <item name="sheetsPrimaryColor">@color/customPrimaryColor</item>
    <item name="sheetsHighlightColor">@color/customHighlightColor</item>
    <item name="sheetsBackgroundColor">@color/customBackgroundColor</item>
    <item name="sheetsDividerColor">@color/customDividerColor</item>
    <item name="sheetsIconsColor">@color/customIconsColor</item>

Du kannst den den Ansichtsstil eines Blattes überschreiben.
Anstatt die Symbolleiste anzuzeigen, kannst du sie einfach ausblenden und den typische Griff anzeigen.

    <item name="sheetsDisplayHandle">true</item>
    <item name="sheetsDisplayToolbar">false</item>
    <item name="sheetsDisplayCloseButton">false</item>

Ändere das Erscheinungsbild des Titels.

    <item name="sheetsTitleColor">@color/customTitleTextColor</item>
    <item name="sheetsTitleFont">@font/font</item>
    <item name="sheetsTitleLineHeight">@dimen/dimen</item>
    <item name="sheetsTitleLetterSpacing">value</item>

Ändere das Erscheinungsbild des Inhaltstextes.

    <item name="sheetsContentColor">@color/customContentTextColor</item>
    <item name="sheetsContentInverseColor">@color/customContentTextInverseColor</item>
    <item name="sheetsContentFont">@font/font</item>
    <item name="sheetsContentLineHeight">@dimen/dimen</item>
    <item name="sheetsContentLetterSpacing">value</item>

Ändere das Erscheinungsbild der Wertetexte.

    <item name="sheetsValueTextActiveColor">@color/customValueTextColor</item>
    <item name="sheetsValueFont">@font/font</item>
    <item name="sheetsValueLineHeight">@dimen/dimen</item>
    <item name="sheetsValueLetterSpacing">value</item>

Ändere das Erscheinungsbild der Zifferntasten.

    <item name="sheetsDigitColor">@color/customDigitTextColor</item>
    <item name="sheetsDigitFont">@font/font</item>
    <item name="sheetsDigitLineHeight">@dimen/dimen</item>
    <item name="sheetsDigitLetterSpacing">value</item>

### Buttons

Überschreibe Erscheinungsbild des Textes eines Buttons.

    <item name="sheetsButtonTextFont">@font/font</item>
    <item name="sheetsButtonTextLetterSpacing">value</item>

Überschreibe das gesamte Erscheinungsbild der Buttons (negativer und positiver Button).

    <item name="sheetsButtonColor">@color/customButtonColor<item>
    <item name="sheetsButtonTextFont">@font/font<item>
    <item name="sheetsButtonTextLetterSpacing">value<item>
    <item name="sheetsButtonCornerRadius">12dp<item>
    <item name="sheetsButtonCornerFamily">cut<item>
    <item name="sheetsButtonWidth">match_content/wrap_content<item>

Überschreibe das Erscheinungsbild des negativen Buttons.

    <item name="sheetsNegativeButtonType">text_button/outlined_button/button<item>
    <item name="sheetsNegativeButtonCornerRadius">12dp<item>
    <item name="sheetsNegativeButtonCornerFamily">cut<item>

Überschreibe das Erscheinungsbild des positiven Buttons.

    <item name="sheetsPositiveButtonType">text_button/outlined_button/button<item>
    <item name="sheetsPositiveButtonCornerRadius">12dp<item>
    <item name="sheetsPositiveButtonCornerFamily">cut<item>

Überschreibe das Erscheinungsbild des Rahmens beim Button mit einem Rand.

    <item name="sheetsButtonOutlinedButtonBorderColor">@color/borderColor<item>
    <item name="sheetsButtonOutlinedButtonBorderWidth">1dp<item>

Die Familie und der Radius wird für jeden Button übernommen.

**Spezifsche Anpassungen**
Du kannst sogar die Eckenfamilie und den Radius der negativen und positiven Schaltfläche für jede Ecke definieren.

    <item name="sheetsNegativeButtonBottomLeftCornerRadius">4dp<item>
    <item name="sheetsNegativeButtonBottomLeftCornerFamily">cut<item>
    ...
    <item name="sheetsPositiveButtonBottomRightCornerRadius">8dp<item>
    <item name="sheetsPositiveButtonBottomRightCornerFamily">rounded<item>

### Griff

Die Größe und das Aussehen des Griffs können folgendermaßen geändert werden:

    <item name="sheetsHandleCornerRadius">8dp</item>
    <item name="sheetsHandleCornerFamily">rounded</item>
    <item name="sheetsHandleFillColor">?sheetPrimaryColor</item>
    <item name="sheetsHandleBorderColor">?sheetPrimaryColor</item>
    <item name="sheetsHandleBorderWidth">1dp</item>
    <item name="sheetsHandleWidth">42dp</item>
    <item name="sheetsHandleHeight">4dp</item>

### OptionsSheet

Überschreibe das Erscheinungsbild von ausgewählten Optionen.

    <item name="sheetsOptionSelectedImageColor">@color/customSelectedOptionImageColor</item>
    <item name="sheetsOptionSelectedTextColor">@color/customSelectedOptionTextColor</item>

Überschreibe das Erscheinungsbild von deaktivierten Optionen.

    <item name="sheetsOptionDisabledImageColor">@color/customDisabledOptionImageColor</item>s
    <item name="sheetsOptionDisabledTextColor">@color/customDisabledOptionImageColor</item>
    <item name="sheetsOptionDisabledBackgroundColor">@color/customDisabledOptionBackgColor</item>

### InputSheet

Überschreibe das Erscheinungsbild des TextInputLayout (wird für den InputEditText verwendet).

    <item name="sheetsTextInputLayoutCornerRadius">12dp</item>
    <item name="sheetsTextInputLayoutBottomLeftCornerRadius">12dp</item>
    ... and for all other corners
    <item name="sheetsTextInputLayoutEndIconColor">@color/customEndIconColor</item>
    <item name="sheetsTextInputLayoutHelperTextColor">@color/customHelperTextColor</item>
    <item name="sheetsTextInputLayoutBoxStrokeColor">@color/customBoxStrokeColor</item>
    <item name="sheetsTextInputLayoutHintTextColor">@color/customHintTextColor</item>
    <item name="sheetsTextInputLayoutBoxStrokeErrorColor">@color/customBoxStrokeErrorColor</item>
    <item name="sheetsTextInputLayoutErrorTextColor">@color/customErrorTextColor</item>

# Sonstiges

## Unterstütze das Projekt

- Hinterlasse einen **Stern** und erzähle anderen davon.

- **Watch** für Updates und Verbesserungen.

- **[Öffne ein Issue](https://github.com/maxkeppeler/sheets/issues/)** wenn du einen Fehler findest.

- Zeige deine Dankbarkeit und deine Implementation [hier](https://github.com/MaxKeppeler/sheets/discussions/categories/show-and-tell).

## Lizenz

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
