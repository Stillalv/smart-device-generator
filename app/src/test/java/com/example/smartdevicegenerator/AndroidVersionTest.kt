package com.example.smartdevicegenerator

import com.example.smartdevicegenerator.data.AndroidVersionDataset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidVersionTest {

    @Test
    fun testAll37ApiVersionsExist() {
        val versions = AndroidVersionDataset.versions
        assertEquals("Should have exactly 37 Android versions", 37, versions.size)

        for (api in 1..37) {
            val version = AndroidVersionDataset.getByApiLevel(api)
            assertNotNull("API $api should exist", version)
            assertEquals("API level must match index", api, version.apiLevel)
            assertTrue("Display name must not be blank", version.displayName.isNotBlank())
            assertTrue("Release name must not be blank", version.releaseName.isNotBlank())
        }
    }

    @Test
    fun testLegacyFlagCorrectness() {
        val versions = AndroidVersionDataset.versions
        for (version in versions) {
            if (version.apiLevel < 21) {
                assertTrue("API ${version.apiLevel} must be legacy", version.isLegacy)
            } else {
                assertFalse("API ${version.apiLevel} must NOT be legacy", version.isLegacy)
            }
        }
    }

    @Test
    fun testAvailableVersionsFilter() {
        val allVersions = AndroidVersionDataset.getAvailableVersions(includeLegacy = true)
        assertEquals("Include legacy should return 37 versions", 37, allVersions.size)

        val nonLegacyVersions = AndroidVersionDataset.getAvailableVersions(includeLegacy = false)
        assertEquals("Exclude legacy should return 17 modern/classic versions (API 21..37)", 17, nonLegacyVersions.size)
        assertTrue("All non-legacy versions must be >= 21", nonLegacyVersions.all { it.apiLevel >= 21 })
    }
}
