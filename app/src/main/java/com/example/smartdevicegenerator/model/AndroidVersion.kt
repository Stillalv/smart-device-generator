package com.example.smartdevicegenerator.model

enum class VersionEra {
    LEGACY,   // Android 1.0 - 4.4W (API 1 - 20)
    CLASSIC,  // Android 5.0 - 9.0 (API 21 - 28)
    MODERN    // Android 10 - 17 (API 29 - 37)
}

data class AndroidVersion(
    val displayName: String,
    val apiLevel: Int,
    val releaseName: String,
    val codename: String,
    val releaseDate: String,
    val isLegacy: Boolean,
    val supportedDeviceYears: IntRange
) {
    val era: VersionEra
        get() = when {
            apiLevel < 21 -> VersionEra.LEGACY
            apiLevel <= 28 -> VersionEra.CLASSIC
            else -> VersionEra.MODERN
        }

    val fullLabel: String
        get() = "$displayName (API $apiLevel)"

    val versionNumber: String
        get() = displayName.replace("Android ", "").trim()
}
