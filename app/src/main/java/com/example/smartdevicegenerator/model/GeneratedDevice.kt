package com.example.smartdevicegenerator.model

import org.json.JSONObject

data class GeneratedDevice(
    val id: String,
    val profileId: String,
    val brand: String,
    val manufacturer: String,
    val model: String,
    val productName: String,
    val deviceName: String,
    val androidVersion: AndroidVersion,
    val sdkVersion: Int,
    val buildId: String,
    val buildType: String = "user/release-keys",
    val deviceCodename: String,
    val buildFingerprint: String,
    val soc: String,
    val cpu: String,
    val gpu: String,
    val cpuArchitecture: String,
    val abi: String,
    val ram: String,
    val storage: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val screenSize: String,
    val density: Int,
    val refreshRate: Int,
    val camera: String,
    val battery: String,
    val releaseYear: Int,
    val serialNumber: String,
    val androidId: String,
    val generatedAt: Long = System.currentTimeMillis()
) {
    val formattedResolution: String
        get() = "${screenWidth} × ${screenHeight} px"

    fun toJsonString(): String {
        val json = JSONObject()
        json.put("model", model)
        json.put("brand", brand)
        json.put("manufacturer", manufacturer)
        json.put("productName", productName)
        json.put("deviceName", deviceName)
        json.put("androidVersion", androidVersion.displayName)
        json.put("sdkVersion", sdkVersion)
        json.put("buildId", buildId)
        json.put("buildType", buildType)
        json.put("deviceCodename", deviceCodename)
        json.put("fingerprint", buildFingerprint)
        json.put("soc", soc)
        json.put("cpu", cpu)
        json.put("gpu", gpu)
        json.put("architecture", cpuArchitecture)
        json.put("abi", abi)
        json.put("ram", ram)
        json.put("storage", storage)

        val screenObj = JSONObject()
        screenObj.put("width", screenWidth)
        screenObj.put("height", screenHeight)
        screenObj.put("size", screenSize)
        screenObj.put("density", density)
        screenObj.put("refreshRate", refreshRate)
        json.put("screen", screenObj)

        json.put("camera", camera)
        json.put("battery", battery)
        json.put("releaseYear", releaseYear)
        json.put("serialNumber", serialNumber)
        json.put("androidId", androidId)
        json.put("generatedAt", generatedAt)
        return json.toString(2)
    }

    fun toShareText(): String {
        return """
[Smart Device Generator Profile]
Device: $deviceName ($model)
Brand: $brand ($manufacturer)
Android: ${androidVersion.displayName} (API $sdkVersion)
Codename: $deviceCodename
Build ID: $buildId
Fingerprint: $buildFingerprint
SoC: $soc
CPU: $cpu
GPU: $gpu
Architecture: $cpuArchitecture ($abi)
Memory: $ram RAM | $storage Storage
Display: $screenWidth x $screenHeight px ($screenSize, ${density}dpi, ${refreshRate}Hz)
Camera: $camera
Battery: $battery
Serial Number: $serialNumber
Android ID: $androidId
""".trimIndent()
    }

    companion object {
        fun fromJson(jsonStr: String, versionLookup: (Int) -> AndroidVersion): GeneratedDevice {
            val json = JSONObject(jsonStr)
            val sdk = json.getInt("sdkVersion")
            val screen = json.getJSONObject("screen")
            return GeneratedDevice(
                id = json.optString("id", java.util.UUID.randomUUID().toString()),
                profileId = json.optString("profileId", ""),
                brand = json.getString("brand"),
                manufacturer = json.getString("manufacturer"),
                model = json.getString("model"),
                productName = json.optString("productName", json.getString("model")),
                deviceName = json.getString("deviceName"),
                androidVersion = versionLookup(sdk),
                sdkVersion = sdk,
                buildId = json.getString("buildId"),
                buildType = json.optString("buildType", "user/release-keys"),
                deviceCodename = json.getString("deviceCodename"),
                buildFingerprint = json.getString("fingerprint"),
                soc = json.getString("soc"),
                cpu = json.getString("cpu"),
                gpu = json.getString("gpu"),
                cpuArchitecture = json.getString("architecture"),
                abi = json.getString("abi"),
                ram = json.getString("ram"),
                storage = json.getString("storage"),
                screenWidth = screen.getInt("width"),
                screenHeight = screen.getInt("height"),
                screenSize = screen.optString("size", "6.0\""),
                density = screen.getInt("density"),
                refreshRate = screen.getInt("refreshRate"),
                camera = json.optString("camera", "12 MP"),
                battery = json.optString("battery", "4000 mAh"),
                releaseYear = json.optInt("releaseYear", 2020),
                serialNumber = json.getString("serialNumber"),
                androidId = json.getString("androidId"),
                generatedAt = json.optLong("generatedAt", System.currentTimeMillis())
            )
        }
    }
}
