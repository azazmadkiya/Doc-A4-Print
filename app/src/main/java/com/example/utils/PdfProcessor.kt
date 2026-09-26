package com.example.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.encryption.InvalidPasswordException
import com.tom_roush.pdfbox.rendering.PDFRenderer
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

object PdfProcessor {

    class PasswordRequiredException(message: String = "Password required to decrypt PDF") : Exception(message)

    data class ExtractedDoc(
        val frontUri: Uri?,
        val backUri: Uri?,
        val title: String,
        val author: String? = null,
        val creationDate: String? = null,
        val subject: String? = null,
        val creator: String? = null
    )

    fun processPdf(context: Context, pdfUri: Uri, password: String? = null): ExtractedDoc? {
        try {
            PDFBoxResourceLoader.init(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val inputStream: InputStream = context.contentResolver.openInputStream(pdfUri) ?: return null
        val tempFile = File(context.cacheDir, "temp_import_${System.currentTimeMillis()}.pdf")
        try {
            FileOutputStream(tempFile).use { out ->
                inputStream.copyTo(out)
            }
        } finally {
            inputStream.close()
        }

        val document: PDDocument = try {
            if (!password.isNullOrBlank()) {
                PDDocument.load(tempFile, password)
            } else {
                PDDocument.load(tempFile)
            }
        } catch (e: InvalidPasswordException) {
            tempFile.delete()
            if (password.isNullOrBlank()) {
                throw PasswordRequiredException("PDF is password protected.")
            } else {
                throw PasswordRequiredException("Incorrect password. Please try again.")
            }
        } catch (e: PasswordRequiredException) {
            tempFile.delete()
            throw e
        } catch (e: Exception) {
            val msg = e.message?.lowercase() ?: ""
            val cls = e.javaClass.simpleName.lowercase()
            val isEncrypted = msg.contains("password") || msg.contains("encrypt") ||
                              msg.contains("crypt") || cls.contains("password") ||
                              cls.contains("crypt") || cls.contains("invalid") ||
                              msg.contains("bad password")
            tempFile.delete()
            if (isEncrypted) {
                if (password.isNullOrBlank()) {
                    throw PasswordRequiredException("PDF is password protected.")
                } else {
                    throw PasswordRequiredException("Incorrect password. Please try again.")
                }
            } else {
                throw e
            }
        }

        try {
            // Extract document metadata
            val info = document.documentInformation
            val docTitle = if (!info?.title.isNullOrBlank()) info.title!! else "Aadhaar / Official ID Card (PDF)"
            val author = info?.author
            val creator = info?.creator
            val subject = info?.subject
            
            val creationDateCal = info?.creationDate
            val creationDateStr = if (creationDateCal != null) {
                try {
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(creationDateCal.time)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }

            val renderer = PDFRenderer(document)
            val pageCount = document.numberOfPages
            if (pageCount <= 0) {
                document.close()
                tempFile.delete()
                return null
            }

            val page1Image = renderer.renderImageWithDPI(0, 300f)
            var frontBitmap: Bitmap
            var backBitmap: Bitmap? = null

            if (pageCount >= 2) {
                frontBitmap = page1Image
                backBitmap = renderer.renderImageWithDPI(1, 300f)
            } else {
                // Single page e-Aadhaar / ID Card PDF:
                // Instructions at top, card in lower portion with Front on Left and Back on Right side-by-side.
                val width = page1Image.width
                val height = page1Image.height
                val cardTop = (height * 0.36f).toInt().coerceIn(0, height - 200)
                val cardBottom = (height * 0.96f).toInt().coerceIn(cardTop + 200, height)
                val cardHeight = cardBottom - cardTop

                val halfWidth = width / 2
                // Front Side = Left half of the card region
                frontBitmap = Bitmap.createBitmap(page1Image, 0, cardTop, halfWidth, cardHeight)
                // Back Side = Right half of the card region
                backBitmap = Bitmap.createBitmap(page1Image, halfWidth, cardTop, width - halfWidth, cardHeight)
            }

            document.close()
            tempFile.delete()

            // Save extracted bitmaps to cache files
            val frontFile = File(context.cacheDir, "pdf_front_${System.currentTimeMillis()}.png")
            FileOutputStream(frontFile).use { out ->
                frontBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val frontUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", frontFile)

            var backUri: Uri? = null
            if (backBitmap != null) {
                val backFile = File(context.cacheDir, "pdf_back_${System.currentTimeMillis()}.png")
                FileOutputStream(backFile).use { out ->
                    backBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                backUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", backFile)
            }

            return ExtractedDoc(
                frontUri = frontUri,
                backUri = backUri,
                title = docTitle,
                author = author,
                creationDate = creationDateStr,
                subject = subject,
                creator = creator
            )
        } catch (e: Exception) {
            try {
                document.close()
            } catch (ignored: Exception) {}
            tempFile.delete()
            e.printStackTrace()
            return null
        }
    }
}
