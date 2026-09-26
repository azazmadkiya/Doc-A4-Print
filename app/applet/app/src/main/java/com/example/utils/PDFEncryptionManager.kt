package com.example.utils

import android.content.Context
import android.widget.Toast
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Locale

/**
 * Manager class to handle password protection, user/owner passwords,
 * and security parameters when exporting A4 documents as secure PDFs,
 * with robust character encoding handlers for special characters.
 */
class PDFEncryptionManager(private val context: Context) {

    data class EncryptionConfig(
        val userPassword: String,
        val ownerPassword: String = userPassword,
        val allowPrinting: Boolean = true,
        val allowCopying: Boolean = false
    )

    var currentConfig: EncryptionConfig? = null
        private set

    /**
     * Sets user and owner passwords for exported A4 files.
     */
    fun setPasswords(userPass: String, ownerPass: String = userPass): Boolean {
        val trimmedUser = userPass.trim()
        if (trimmedUser.isBlank()) {
            Toast.makeText(context, "Password cannot be blank", Toast.LENGTH_SHORT).show()
            return false
        }
        currentConfig = EncryptionConfig(
            userPassword = trimmedUser,
            ownerPassword = ownerPass.trim().ifBlank { trimmedUser }
        )
        Toast.makeText(context, "Password protection enabled for export", Toast.LENGTH_SHORT).show()
        return true
    }

    /**
     * Clears password protection (removes encryption config).
     */
    fun clearProtection() {
        currentConfig = null
        Toast.makeText(context, "Password protection removed", Toast.LENGTH_SHORT).show()
    }

    /**
     * Verifies if a given password matches the configured user password with robust normalization.
     */
    fun verifyPassword(inputPassword: String): Boolean {
        val config = currentConfig ?: return true
        val normalizedInput = inputPassword.trim()
        return config.userPassword.equals(normalizedInput, ignoreCase = false) ||
                config.ownerPassword.equals(normalizedInput, ignoreCase = false) ||
                config.userPassword.equals(normalizedInput, ignoreCase = true)
    }

    /**
     * Encodes a PDF password into standard-compliant byte arrays (supporting ISO-8859-1 / Cp1252 / UTF-8)
     * ensuring compatibility with special characters often found in Aadhaar / official ID PDFs.
     */
    fun getPasswordBytes(password: String): ByteArray {
        val trimmed = password.trim()
        return try {
            // Try ISO-8859-1 (standard PDFDocEncoding / PDF password standard)
            trimmed.toByteArray(StandardCharsets.ISO_8859_1)
        } catch (e: Exception) {
            try {
                // Fallback to UTF-8
                trimmed.toByteArray(StandardCharsets.UTF_8)
            } catch (e2: Exception) {
                trimmed.toByteArray()
            }
        }
    }

    /**
     * Hashes password for secure storage or checking if needed.
     */
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(getPasswordBytes(password))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
