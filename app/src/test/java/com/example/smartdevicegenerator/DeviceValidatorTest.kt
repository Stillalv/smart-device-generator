package com.example.smartdevicegenerator

import com.example.smartdevicegenerator.data.AndroidVersionDataset
import com.example.smartdevicegenerator.generator.DeviceGenerator
import com.example.smartdevicegenerator.generator.DeviceValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceValidatorTest {

    @Test
    fun testValidDevicePassesValidation() {
        val version = AndroidVersionDataset.getByApiLevel(34)
        val device = DeviceGenerator.generateDevice(version)
        val result = DeviceValidator.validate(device)
        assertTrue("Generated device must pass validation: ${result.errors}", result.isValid)
    }

    @Test
    fun testCorruptedAndroidIdFailsValidation() {
        val version = AndroidVersionDataset.getByApiLevel(34)
        val device = DeviceGenerator.generateDevice(version)

        // Invalid length (15 hex instead of 16)
        val corrupted1 = device.copy(androidId = "1234567890abcde")
        assertFalse("15-char Android ID must fail", DeviceValidator.validate(corrupted1).isValid)

        // Invalid character (non-hex 'z')
        val corrupted2 = device.copy(androidId = "1234567890abcdefz"[0].toString().repeat(16).replace('z', 'Z'))
        assertFalse("Uppercase or non-hex must fail", DeviceValidator.validate(corrupted2).isValid)
    }

    @Test
    fun testMismatchedSdkVersionFailsValidation() {
        val version = AndroidVersionDataset.getByApiLevel(34)
        val device = DeviceGenerator.generateDevice(version)

        val corrupted = device.copy(sdkVersion = 30) // Mismatched sdkVersion with androidVersion.apiLevel 34
        assertFalse("Mismatched sdkVersion must fail", DeviceValidator.validate(corrupted).isValid)
    }

    @Test
    fun testEmptyFieldsFailValidation() {
        val version = AndroidVersionDataset.getByApiLevel(34)
        val device = DeviceGenerator.generateDevice(version)

        val noBrand = device.copy(brand = "")
        assertFalse("Empty brand must fail", DeviceValidator.validate(noBrand).isValid)

        val noSoc = device.copy(soc = "")
        assertFalse("Empty SoC must fail", DeviceValidator.validate(noSoc).isValid)

        val invalidResolution = device.copy(screenWidth = 0, screenHeight = -10)
        assertFalse("Invalid resolution must fail", DeviceValidator.validate(invalidResolution).isValid)
    }
}
