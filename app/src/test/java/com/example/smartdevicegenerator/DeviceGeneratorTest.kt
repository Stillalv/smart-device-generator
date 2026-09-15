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

    @Test
    fun testBrandSelectionMode() {
        val version = AndroidVersionDataset.getByApiLevel(34)
        val testBrands = listOf("Xiaomi", "Samsung", "Google")

        for (brand in testBrands) {
            val device = DeviceGenerator.generateDevice(version, selectedBrand = brand)
            assertNotNull(device)
            assertEquals("Device brand should match selected brand filter", brand.lowercase(), device.brand.lowercase())
        }
    }

    @Test
    fun testCleanDeviceNameSanitizer() {
        assertEquals("HTC One (M8)", DeviceDataset.cleanDeviceName("HTC One (M8) Duo Camera BoomSound"))
        assertEquals("HTC One (M7)", DeviceDataset.cleanDeviceName("HTC One (M7) UltraPixel"))
        assertEquals("Samsung Galaxy S24 Ultra", DeviceDataset.cleanDeviceName("Samsung Galaxy S24 Ultra (Titanium Frame & Galaxy AI)"))
        assertEquals("Google Nexus 7 (2012)", DeviceDataset.cleanDeviceName("Google Nexus 7 (2012)"))
        assertEquals("Motorola Moto G (1st Gen)", DeviceDataset.cleanDeviceName("Motorola Moto G (1st Gen)"))
        assertEquals("Nothing Phone (1)", DeviceDataset.cleanDeviceName("Nothing Phone (1) (Glyph Interface)"))
        assertEquals("POCOPHONE F1", DeviceDataset.cleanDeviceName("POCOPHONE F1 (LiquidCool Snapdragon 845)"))
        assertEquals("Xiaomi 14 Ultra", DeviceDataset.cleanDeviceName("Xiaomi 14 Ultra (1-inch LYT-900 Stepless f/1.63-f/4.0 Leica)"))
    }

    @Test
    fun testBuildPropAndPifJsonExportFormats() {
        val version = AndroidVersionDataset.getByApiLevel(34)
        val device = DeviceGenerator.generateDevice(version, selectedBrand = "Xiaomi")

        val buildProp = device.toBuildPropString()
        assertTrue(buildProp.contains("ro.product.brand="))
        assertTrue(buildProp.contains("ro.product.model="))
        assertTrue(buildProp.contains("ro.product.device="))
        assertTrue(buildProp.contains("ro.product.name="))
        assertTrue(buildProp.contains("ro.product.manufacturer="))
        assertTrue(buildProp.contains("ro.build.fingerprint="))
        assertTrue(buildProp.contains("ro.build.id="))
        assertTrue(buildProp.contains("ro.build.version.sdk=34"))

        val pifJson = device.toPifJsonString()
        val pifObj = org.json.JSONObject(pifJson)
        assertTrue(pifObj.has("MANUFACTURER"))
        assertTrue(pifObj.has("BRAND"))
        assertTrue(pifObj.has("PRODUCT"))
        assertTrue(pifObj.has("DEVICE"))
        assertTrue(pifObj.has("MODEL"))
        assertTrue(pifObj.has("FINGERPRINT"))
        assertTrue(pifObj.has("ID"))
        assertTrue(pifObj.has("FIRST_API_LEVEL"))
    }
}
