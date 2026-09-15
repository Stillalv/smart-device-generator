package com.example.smartdevicegenerator.data

import android.content.Context
import com.example.smartdevicegenerator.model.DeviceProfile
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

object DeviceDataset {
    private var _cachedProfiles: List<DeviceProfile>? = null

    val profiles: List<DeviceProfile>
        get() {
            if (_cachedProfiles == null) {
                _cachedProfiles = loadEmbeddedProfiles()
            }
            return _cachedProfiles ?: emptyList()
        }

    fun initialize(context: Context) {
        try {
            val inputStream: InputStream = context.assets.open("devices.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            _cachedProfiles = parseProfilesJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to embedded dataset if assets reading fails
            _cachedProfiles = loadEmbeddedProfiles()
        }
    }

    fun parseProfilesJson(jsonString: String): List<DeviceProfile> {
        val list = mutableListOf<DeviceProfile>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(parseSingleProfile(obj))
        }
        return list
    }

    private fun parseSingleProfile(obj: JSONObject): DeviceProfile {
        val versionsArray = obj.getJSONArray("androidVersions")
        val versions = mutableListOf<Int>()
        for (j in 0 until versionsArray.length()) {
            versions.add(versionsArray.getInt(j))
        }

        val ramArray = obj.getJSONArray("ramOptions")
        val ramOptions = mutableListOf<String>()
        for (j in 0 until ramArray.length()) {
            ramOptions.add(ramArray.getString(j))
        }

        val stgArray = obj.getJSONArray("storageOptions")
        val storageOptions = mutableListOf<String>()
        for (j in 0 until stgArray.length()) {
            storageOptions.add(stgArray.getString(j))
        }

        val patArray = obj.optJSONArray("buildIdPatterns")
        val patterns = mutableListOf<String>()
        if (patArray != null) {
            for (j in 0 until patArray.length()) {
                patterns.add(patArray.getString(j))
            }
        }

        return DeviceProfile(
            id = obj.getString("id"),
            brand = obj.getString("brand"),
            manufacturer = obj.getString("manufacturer"),
            model = obj.getString("model"),
            productName = obj.optString("productName", obj.getString("model")),
            deviceName = cleanDeviceName(obj.getString("deviceName")),
            androidVersions = versions,
            minimumAndroidApi = obj.getInt("minimumAndroidApi"),
            maximumKnownAndroidApi = obj.getInt("maximumKnownAndroidApi"),
            soc = obj.getString("soc"),
            cpu = obj.getString("cpu"),
            gpu = obj.getString("gpu"),
            cpuArchitecture = obj.getString("cpuArchitecture"),
            abi = obj.getString("abi"),
            ramOptions = ramOptions,
            storageOptions = storageOptions,
            screenWidth = obj.getInt("screenWidth"),
            screenHeight = obj.getInt("screenHeight"),
            screenSize = obj.getString("screenSize"),
            density = obj.getInt("density"),
            refreshRate = obj.getInt("refreshRate"),
            camera = obj.getString("camera"),
            battery = obj.getString("battery"),
            releaseYear = obj.getInt("releaseYear"),
            buildFingerprintTemplate = obj.getString("buildFingerprintTemplate"),
            buildIdPatterns = patterns,
            deviceCodename = obj.getString("deviceCodename")
        )
    }

    /**
     * Strips promotional slogans and technical marketing parentheticals while preserving
     * valid model identifiers, generations, and release years.
     */
    fun cleanDeviceName(raw: String): String {
        var name = raw.trim()

        val trailingMarketing = listOf(
            "UltraPixel",
            "Duo Camera BoomSound",
            "Duo Camera",
            "BoomSound"
        )
        for (term in trailingMarketing) {
            if (name.endsWith(term, ignoreCase = true)) {
                name = name.removeSuffix(term).trim()
            }
        }

        val parenRegex = Regex("""\s*\(([^)]+)\)$""")
        var match = parenRegex.find(name)
        while (match != null) {
            val inside = match.groupValues[1].trim()
            val isModelDesignation = inside.matches(
                Regex("""^(M[0-9]|20[0-2][0-9]|[1-9](st|nd|rd|th)\s+Gen|[1-2][a-z]?|[45]G)$""", RegexOption.IGNORE_CASE)
            )
            if (!isModelDesignation) {
                name = name.substring(0, match.range.first).trim()
                match = parenRegex.find(name)
            } else {
                break
            }
        }
        return name
    }

    /**
     * Loads the raw JSON string directly embedded or from classpath for standalone unit tests.
     */
    private fun loadEmbeddedProfiles(): List<DeviceProfile> {
        return try {
            val stream = DeviceDataset::class.java.classLoader?.getResourceAsStream("assets/devices.json")
                ?: DeviceDataset::class.java.getResourceAsStream("/assets/devices.json")
            if (stream != null) {
                val json = stream.bufferedReader().use { it.readText() }
                parseProfilesJson(json)
            } else {
                // Fallback: Read directly from local file in unit test environment
                val candidates = listOf(
                    java.io.File("src/main/assets/devices.json"),
                    java.io.File("app/src/main/assets/devices.json"),
                    java.io.File("../app/src/main/assets/devices.json"),
                    java.io.File("C:/Users/uni/Documents/uni-device/app/src/main/assets/devices.json")
                )
                val targetFile = candidates.firstOrNull { it.exists() }
                if (targetFile != null) {
                    parseProfilesJson(targetFile.readText())
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
