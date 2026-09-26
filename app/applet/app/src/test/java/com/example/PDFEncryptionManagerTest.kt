package com.example

import com.example.utils.PDFEncryptionManager
import org.junit.Assert.*
import org.junit.Test

class PDFEncryptionManagerTest {

    @Test
    fun testSetAndVerifyPassword() {
        val manager = PDFEncryptionManager(null)
        assertFalse(manager.setPasswords("   "))
        assertTrue(manager.setPasswords("ANIL1990", "OWNER123"))

        assertTrue(manager.verifyPassword("ANIL1990"))
        assertTrue(manager.verifyPassword("  ANIL1990  ")) // trimming protocol
        assertTrue(manager.verifyPassword("anil1990")) // case normalization protocol
        assertTrue(manager.verifyPassword("OWNER123"))
        assertFalse(manager.verifyPassword("WRONG"))
    }

    @Test
    fun testClearProtection() {
        val manager = PDFEncryptionManager(null)
        manager.setPasswords("SECRET")
        assertTrue(manager.verifyPassword("SECRET"))

        manager.clearProtection()
        assertNull(manager.currentConfig)
        assertTrue(manager.verifyPassword("ANY_PASSWORD")) // unencrypted when config is null
    }

    @Test
    fun testPasswordHashing() {
        val manager = PDFEncryptionManager(null)
        val hash1 = manager.hashPassword("TEST1990")
        val hash2 = manager.hashPassword("  TEST1990  ")
        assertEquals(hash1, hash2)
        assertNotNull(hash1)
        assertEquals(64, hash1.length) // SHA-256 hex string length
    }
}
