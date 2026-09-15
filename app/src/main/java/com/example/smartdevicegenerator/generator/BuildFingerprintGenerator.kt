package com.example.smartdevicegenerator.generator

import com.example.smartdevicegenerator.model.AndroidVersion
import com.example.smartdevicegenerator.model.DeviceProfile
import java.util.Random

object BuildFingerprintGenerator {
    private val random = Random()

    fun generateBuildId(version: AndroidVersion, patterns: List<String>): String {
        if (patterns.isNotEmpty()) {
            val template = patterns[random.nextInt(patterns.size)]
            val dateNum = String.format("%02d%02d%02d", (20 + random.nextInt(6)), (1 + random.nextInt(12)), (1 + random.nextInt(28)))
            val buildNum = String.format("%03d", 1 + random.nextInt(99))
            return template.replace("{date}", dateNum).replace("{num}", buildNum)
        }

        return when (version.apiLevel) {
            1, 2 -> "TC4-RC30"
            3, 4 -> "CRC1"
            5, 6, 7 -> "ERD79"
            8 -> "FRF91"
            9, 10 -> "GRI40"
            11, 12, 13 -> "HTJ85B"
            14, 15 -> "ITL41D"
            16, 17, 18 -> "JWR66Y"
            19, 20 -> "KTU84P"
            21, 22 -> "LMY48T"
            23 -> "M4B30Z"
            24, 25 -> "N2G48H"
            26, 27 -> "OPR6.170623.013"
            28 -> "PPR1.180610.011"
            29 -> "QQ3A.200805.001"
            30 -> "RQ3A.210905.001"
            31, 32 -> "SQ3A.220705.003"
            33 -> "TQ3A.230805.001"
            34 -> "UP1A.231005.007"
            35 -> "AP2A.240805.005"
            36 -> "BP1A.250305.004"
            37 -> "CP1A.260405.002"
            else -> "AP1A.240305.019"
        }
    }

    fun generateFingerprint(
        profile: DeviceProfile,
        version: AndroidVersion,
        buildId: String,
        buildType: String = "user/release-keys"
    ): String {
        val incremental = (1000000 + random.nextInt(8999999)).toString()
        val template = if (profile.buildFingerprintTemplate.isNotBlank()) {
            profile.buildFingerprintTemplate
        } else {
            "{brand}/{product}/{codename}:{version}/{buildId}/{incremental}:{buildType}"
        }

        val versionNumber = version.displayName.replace("Android ", "").trim()

        return template
            .replace("{brand}", profile.brand.lowercase().replace(" ", ""))
            .replace("{manufacturer}", profile.manufacturer.lowercase().replace(" ", ""))
            .replace("{product}", profile.productName)
            .replace("{model}", profile.model)
            .replace("{codename}", profile.deviceCodename)
            .replace("{version}", versionNumber)
            .replace("{buildId}", buildId)
            .replace("{incremental}", incremental)
            .replace("{buildType}", buildType)
    }
}
