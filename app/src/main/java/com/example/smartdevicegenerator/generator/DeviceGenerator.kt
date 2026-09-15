package com.example.smartdevicegenerator.generator

import com.example.smartdevicegenerator.data.DeviceDataset
import com.example.smartdevicegenerator.model.AndroidVersion
import com.example.smartdevicegenerator.model.DeviceProfile
import com.example.smartdevicegenerator.model.GeneratedDevice
import java.util.UUID
import kotlin.random.Random

object DeviceGenerator {

    /**
     * Executes the 12-step generation algorithm with full consistency check and validation retry.
     */
    fun generateDevice(
        androidVersion: AndroidVersion,
        selectedBrand: String? = null,
        customProfile: DeviceProfile? = null
    ): GeneratedDevice {
        var attempts = 0
        val maxAttempts = 10

        while (attempts < maxAttempts) {
            attempts++

            // 1 & 2. Find compatible profiles
            val candidateProfile = customProfile ?: run {
                val compatibleByVersion = DeviceDataset.profiles.filter { it.supportsApi(androidVersion.apiLevel) }
                val compatible = if (!selectedBrand.isNullOrEmpty()) {
                    val byBrand = compatibleByVersion.filter { it.brand.equals(selectedBrand, ignoreCase = true) }
                    if (byBrand.isNotEmpty()) {
                        byBrand
                    } else {
                        // If no profile of this brand supports this exact version, fallback to all profiles of this brand
                        val allBrandProfiles = DeviceDataset.profiles.filter { it.brand.equals(selectedBrand, ignoreCase = true) }
                        if (allBrandProfiles.isNotEmpty()) allBrandProfiles else compatibleByVersion
                    }
                } else {
                    compatibleByVersion
                }

                if (compatible.isNotEmpty()) {
                    compatible[Random.nextInt(compatible.size)]
                } else {
                    // Fallback to closest known profile
                    DeviceDataset.profiles.minByOrNull {
                        kotlin.math.abs(it.minimumAndroidApi - androidVersion.apiLevel)
                    } ?: DeviceDataset.profiles.first()
                }
            }

            // 3. Select RAM variant
            val selectedRam = if (candidateProfile.ramOptions.isNotEmpty()) {
                candidateProfile.ramOptions[Random.nextInt(candidateProfile.ramOptions.size)]
            } else {
                "4 GB"
            }

            // 4. Select Storage variant
            val selectedStorage = if (candidateProfile.storageOptions.isNotEmpty()) {
                candidateProfile.storageOptions[Random.nextInt(candidateProfile.storageOptions.size)]
            } else {
                "64 GB"
            }

            // 5. Generate synthetic serial number (alphanumeric)
            val serial = IdGenerator.generateSerialNumber(12)

            // 6. Generate synthetic Android ID (16 hex)
            val androidId = IdGenerator.generateAndroidId()

            // 7. Generate compatible build info
            val buildId = BuildFingerprintGenerator.generateBuildId(
                version = androidVersion,
                patterns = candidateProfile.buildIdPatterns
            )

            // 8. Generate build fingerprint
            val fingerprint = BuildFingerprintGenerator.generateFingerprint(
                profile = candidateProfile,
                version = androidVersion,
                buildId = buildId
            )

            // 9. Construct candidate GeneratedDevice
            val generated = GeneratedDevice(
                id = UUID.randomUUID().toString(),
                profileId = candidateProfile.id,
                brand = candidateProfile.brand,
                manufacturer = candidateProfile.manufacturer,
                model = candidateProfile.model,
                productName = candidateProfile.productName,
                deviceName = candidateProfile.deviceName,
                androidVersion = androidVersion,
                sdkVersion = androidVersion.apiLevel,
                buildId = buildId,
                buildType = "user/release-keys",
                deviceCodename = candidateProfile.deviceCodename,
                buildFingerprint = fingerprint,
                soc = candidateProfile.soc,
                cpu = candidateProfile.cpu,
                gpu = candidateProfile.gpu,
                cpuArchitecture = candidateProfile.cpuArchitecture,
                abi = candidateProfile.abi,
                ram = selectedRam,
                storage = selectedStorage,
                screenWidth = candidateProfile.screenWidth,
                screenHeight = candidateProfile.screenHeight,
                screenSize = candidateProfile.screenSize,
                density = candidateProfile.density,
                refreshRate = candidateProfile.refreshRate,
                camera = candidateProfile.camera,
                battery = candidateProfile.battery,
                releaseYear = candidateProfile.releaseYear,
                serialNumber = serial,
                androidId = androidId,
                generatedAt = System.currentTimeMillis()
            )

            // 10 & 11. Validate entire profile
            val validation = DeviceValidator.validate(generated)
            if (validation.isValid) {
                return generated
            }
        }

        // If after maxAttempts still failed, throw descriptive error
        throw IllegalStateException("Failed to generate a valid device profile for API ${androidVersion.apiLevel} after $maxAttempts attempts.")
    }
}
