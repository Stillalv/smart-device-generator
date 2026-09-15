package com.example.smartdevicegenerator

import com.example.smartdevicegenerator.generator.IdGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IdGeneratorTest {

    private val hexRegex = Regex("^[0-9a-f]{16}$")
    private val serialRegex = Regex("^[A-Z0-9]{12}$")

    @Test
    fun testGenerateAndroidIdFormat() {
        for (i in 0 until 1000) {
            val androidId = IdGenerator.generateAndroidId()
            assertEquals("Android ID must be 16 characters", 16, androidId.length)
            assertTrue("Android ID must be valid lowercase hex: $androidId", hexRegex.matches(androidId))
        }
    }

    @Test
    fun testGenerateSerialNumberFormat() {
        for (i in 0 until 1000) {
            val serial = IdGenerator.generateSerialNumber(12)
            assertEquals("Serial must be 12 characters", 12, serial.length)
            assertTrue("Serial must be alphanumeric uppercase: $serial", serialRegex.matches(serial))
        }
    }

    @Test
    fun testAndroidIdUniqueness() {
        val count = 2000
        val set = HashSet<String>(count)
        for (i in 0 until count) {
            set.add(IdGenerator.generateAndroidId())
        }
        assertEquals("Generated Android IDs must all be unique", count, set.size)
    }

    @Test
    fun testSerialNumberUniqueness() {
        val count = 2000
        val set = HashSet<String>(count)
        for (i in 0 until count) {
            set.add(IdGenerator.generateSerialNumber(12))
        }
        assertEquals("Generated serial numbers must all be unique", count, set.size)
    }
}
