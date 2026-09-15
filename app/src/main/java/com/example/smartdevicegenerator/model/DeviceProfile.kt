package com.example.smartdevicegenerator.model

data class DeviceProfile(
    val id: String,
    val brand: String,
    val manufacturer: String,
    val model: String,
    val productName: String,
    val deviceName: String,
    val androidVersions: List<Int>,
    val minimumAndroidApi: Int,
    val maximumKnownAndroidApi: Int,
    val soc: String,
    val cpu: String,
    val gpu: String,
    val cpuArchitecture: String,
    val abi: String,
    val ramOptions: List<String>,
    val storageOptions: List<String>,
    val screenWidth: Int,
    val screenHeight: Int,
    val screenSize: String,
    val density: Int,
    val refreshRate: Int,
    val camera: String,
    val battery: String,
    val releaseYear: Int,
    val buildFingerprintTemplate: String,
    val buildIdPatterns: List<String>,
    val deviceCodename: String
) {
    fun supportsApi(apiLevel: Int): Boolean {
        return apiLevel in minimumAndroidApi..maximumKnownAndroidApi
    }

    val formattedResolution: String
        get() = "${screenWidth} × ${screenHeight} px"
}
