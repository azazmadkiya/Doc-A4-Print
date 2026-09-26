package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LegalDialog(
    initialTab: Int = 0, // 0: Privacy Policy, 1: Terms of Service
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PrivacyTip,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Legal & Privacy",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Developed By - Azazmadkiya",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tabs
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Privacy Policy", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Terms of Service", fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Content scroll area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(8.dp)
                    ) {
                        if (selectedTab == 0) {
                            Text(
                                text = "Privacy Policy for Doc A4 Print\nEffective Date: September 26, 2026\n\n" +
                                        "Doc A4 Print (\"we\", \"our\", or \"us\") is committed to protecting your privacy. This Privacy Policy explains how our mobile application handles your information.\n\n" +
                                        "1. Information Collection & Usage\n" +
                                        "• Local Document Processing: Doc A4 Print processes your documents, ID cards (Aadhaar, PAN, etc.), and photos 100% locally on your device.\n" +
                                        "• No Cloud Uploads: We do not collect, store, upload, or share your personal documents or photos on any external servers or cloud storage. All cropping and A4 layouts happen offline.\n" +
                                        "• Camera & Storage: Required solely to capture photos of documents and select existing images for alignment and printing.\n\n" +
                                        "2. Permissions\n" +
                                        "• Camera: Used to photograph ID cards and documents.\n" +
                                        "• Storage / Media: Used to load images for cropping and saving exported A4 PDFs.\n\n" +
                                        "3. Data Security\n" +
                                        "Because your documents are processed locally and never transmitted to servers, your personal data remains private and secure on your device.\n\n" +
                                        "4. Contact Us\n" +
                                        "Developer: Azazmadkiya\n" +
                                        "Email: azazmadkiya@gmail.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        } else {
                            Text(
                                text = "Terms of Service for Doc A4 Print\nEffective Date: September 26, 2026\n\n" +
                                        "Welcome to Doc A4 Print. By downloading or using the app, these terms apply to you.\n\n" +
                                        "1. Scope of Use\n" +
                                        "Doc A4 Print is designed to help users crop, align, and print ID cards and documents onto a single A4 page. You agree to use the app only for lawful purposes.\n\n" +
                                        "2. Intellectual Property\n" +
                                        "All rights, title, and interest in and to the app are and will remain the exclusive property of Azazmadkiya.\n\n" +
                                        "3. User Responsibility\n" +
                                        "You are solely responsible for the documents and cards you scan, crop, and print using Doc A4 Print.\n\n" +
                                        "4. Limitation of Liability\n" +
                                        "The app is provided on an \"as is\" and \"as available\" basis without warranties of any kind.\n\n" +
                                        "5. Contact\n" +
                                        "Developer: Azazmadkiya\n" +
                                        "Email: azazmadkiya@gmail.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
