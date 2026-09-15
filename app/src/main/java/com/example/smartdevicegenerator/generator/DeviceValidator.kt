package com.example.smartdevicegenerator.generator

import com.example.smartdevicegenerator.model.GeneratedDevice

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)

object DeviceValidator {
    private val ANDROID_ID_REGEX = Regex("^[0-9a-f]{16}$")
    private val SERIAL_REGEX = Regex("^[A-Z0-9]{8,16}$")

    fun validate(device: GeneratedDevice): ValidationResult {
        val errors = mutableListOf<String>()

        // 1. Brand and Model consistency
        if (device.brand.isBlank()) {
            errors.add("Brand cannot be empty")
        }
        if (device.model.isBlank()) {
            errors.add("Model cannot be empty")
        }

        // 2. Manufacturer and Model consistency
        if (device.manufacturer.isBlank()) {
            errors.add("Manufacturer cannot be empty")
        }

        // 3. Android compatibility and API level consistency
        if (device.sdkVersion != device.androidVersion.apiLevel) {
            errors.add("SDK version (${device.sdkVersion}) does not match AndroidVersion apiLevel (${device.androidVersion.apiLevel})")
        }
        if (device.sdkVersion !in 1..37) {
            errors.add("SDK version (${device.sdkVersion}) out of valid range 1..37")
        }

        // 4. CPU and GPU consistency
        if (device.soc.isBlank()) {
            errors.add("SoC cannot be empty")
        }
        if (device.cpu.isBlank()) {
            errors.add("CPU cannot be empty")
        }
        if (device.gpu.isBlank()) {
            errors.add("GPU cannot be empty")
        }

        // 5. Screen resolution consistency
        if (device.screenWidth <= 0 || device.screenHeight <= 0) {
            errors.add("Invalid screen resolution: ${device.screenWidth}x${device.screenHeight}")
        }
        if (device.density <= 0) {
            errors.add("Invalid density: ${device.density}")
        }
        if (device.refreshRate <= 0) {
            errors.add("Invalid refresh rate: ${device.refreshRate}")
        }

        // 6. RAM and Storage valid
        if (device.ram.isBlank()) {
            errors.add("RAM cannot be empty")
        }
        if (device.storage.isBlank()) {
            errors.add("Storage cannot be empty")
        }

        // 7. ABI and CPU Architecture valid
        val validAbis = listOf("arm64-v8a", "armeabi-v7a", "armeabi", "x86", "x86_64")
        if (device.abi !in validAbis) {
            errors.add("Invalid ABI: ${device.abi}")
        }

        // 8. Android ID valid (16 hex characters)
        if (!ANDROID_ID_REGEX.matches(device.androidId)) {
            errors.add("Android ID must be exactly 16 lowercase hex characters: ${device.androidId}")
        }

        // 9. Serial Number valid (alphanumeric)
        if (!SERIAL_REGEX.matches(device.serialNumber)) {
            errors.add("Serial number must be 8-16 alphanumeric uppercase characters: ${device.serialNumber}")
        }

        // 10. Build Fingerprint valid
        if (device.buildFingerprint.isBlank() || !device.buildFingerprint.contains(":")) {
            errors.add("Invalid build fingerprint: ${device.buildFingerprint}")
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
}
