package com.example.smartdevicegenerator

import com.example.smartdevicegenerator.data.AndroidVersionDataset
import com.example.smartdevicegenerator.data.DeviceDataset
import com.example.smartdevicegenerator.generator.DeviceGenerator
import com.example.smartdevicegenerator.generator.DeviceValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class DeviceGeneratorTest {

    @Test
    fun testGenerateTenThousandProfilesStressTest() {
        val allVersions = AndroidVersionDataset.versions
        val hexRegex = Regex("^[0-9a-f]{16}$")
        val serialRegex = Regex("^[A-Z0-9]{8,16}$")

        // Stress test generating 10,000 profiles across different Android versions without crashing
        val targetGenerations = 10000
        for (i in 0 until targetGenerations) {
            val randomVersion = allVersions[Random.nextInt(allVersions.size)]
            val generated = DeviceGenerator.generateDevice(randomVersion)

            // Validate entire profile
            val validation = DeviceValidator.validate(generated)
            assertTrue("Profile #$i failed validation: ${validation.errors}", validation.isValid)

            // Check specific requirements
            assertEquals("SDK version must match chosen version", randomVersion.apiLevel, generated.sdkVersion)
            assertTrue("Android ID must always be 16 hex characters", hexRegex.matches(generated.androidId))
            assertTrue("Serial must not be empty and alphanumeric", serialRegex.matches(generated.serialNumber))
            assertTrue("Brand must not be blank", generated.brand.isNotBlank())
            assertTrue("Model must not be blank", generated.model.isNotBlank())
            assertTrue("Manufacturer must not be blank", generated.manufacturer.isNotBlank())
            assertTrue("SoC must not be blank", generated.soc.isNotBlank())
            assertTrue("CPU must not be blank", generated.cpu.isNotBlank())
            assertTrue("GPU must not be blank", generated.gpu.isNotBlank())
            assertTrue("Resolution width must be > 0", generated.screenWidth > 0)
            assertTrue("Resolution height must be > 0", generated.screenHeight > 0)
            assertTrue("RAM must not be blank", generated.ram.isNotBlank())
            assertTrue("Storage must not be blank", generated.storage.isNotBlank())
            assertTrue("Build fingerprint must not be blank", generated.buildFingerprint.isNotBlank())
        }
    }

    @Test
    fun testGenerateForSpecificEras() {
        // Test Android 1.0 (Early Era)
        val v1 = AndroidVersionDataset.getByApiLevel(1)
        val d1 = DeviceGenerator.generateDevice(v1)
        assertNotNull(d1)
        assertEquals(1, d1.sdkVersion)

        // Test Android 4.4 (KitKat)
        val v19 = AndroidVersionDataset.getByApiLevel(19)
        val d19 = DeviceGenerator.generateDevice(v19)
        assertNotNull(d19)
        assertEquals(19, d19.sdkVersion)

        // Test Android 14 (Modern)
        val v34 = AndroidVersionDataset.getByApiLevel(34)
        val d34 = DeviceGenerator.generateDevice(v34)
        assertNotNull(d34)
        assertEquals(34, d34.sdkVersion)

        // Test Android 17 (Future Supported)
        val v37 = AndroidVersionDataset.getByApiLevel(37)
        val d37 = DeviceGenerator.generateDevice(v37)
        assertNotNull(d37)
        assertEquals(37, d37.sdkVersion)
    }

    @Test
    fun testJsonExportAndReconstruct() {
        val version = AndroidVersionDataset.getByApiLevel(34)
        val original = DeviceGenerator.generateDevice(version)
        val jsonString = original.toJsonString()

        val reconstructed = com.example.smartdevicegenerator.model.GeneratedDevice.fromJson(jsonString) { api ->
            AndroidVersionDataset.getByApiLevel(api)
        }

        assertEquals(original.model, reconstructed.model)
        assertEquals(original.brand, reconstructed.brand)
        assertEquals(original.sdkVersion, reconstructed.sdkVersion)
        assertEquals(original.androidId, reconstructed.androidId)
        assertEquals(original.serialNumber, reconstructed.serialNumber)
        assertEquals(original.screenWidth, reconstructed.screenWidth)
        assertEquals(original.screenHeight, reconstructed.screenHeight)
    }
}
