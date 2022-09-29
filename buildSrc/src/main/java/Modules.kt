@file:Suppress("PropertyName")

enum class Modules(val moduleName: String) {

    CORE("core"),
    INFO("info"),
    COLOR("color"),
    CALENDAR("calendar"),
    CLOCK("clock"),
    DURATION("duration"),
    OPTION("option"),
    INPUT("input"),
    LOTTIE("lottie"),
    STORAGE("storage");

    val path: String
        get() = ":$moduleName"

    val namespace: String
        get() = "com.maxkeppeler.sheets.$moduleName"
}