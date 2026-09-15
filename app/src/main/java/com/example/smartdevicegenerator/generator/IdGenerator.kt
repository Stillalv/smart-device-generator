package com.example.smartdevicegenerator.generator

import java.security.SecureRandom

object IdGenerator {
    private val secureRandom = SecureRandom()
    private val HEX_CHARS = "0123456789abcdef".toCharArray()
    private val ALPHANUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    /**
     * Generates a synthetic 16-character hexadecimal Android ID.
     * Complies with Android 64-bit hex standard (e.g. a83f91c7d2e54b10).
     */
    fun generateAndroidId(): String {
        val chars = CharArray(16)
        for (i in chars.indices) {
            chars[i] = HEX_CHARS[secureRandom.nextInt(HEX_CHARS.size)]
        }
        return String(chars)
    }

    /**
     * Generates a synthetic uppercase alphanumeric serial number.
     * Realistic manufacturer format (e.g. R5CT91K7ABXM).
     */
    fun generateSerialNumber(length: Int = 12): String {
        val chars = CharArray(length)
        for (i in chars.indices) {
            chars[i] = ALPHANUMERIC_CHARS[secureRandom.nextInt(ALPHANUMERIC_CHARS.size)]
        }
        return String(chars)
    }
}
