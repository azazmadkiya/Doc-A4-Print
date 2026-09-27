# Doc A4 Print

[![Build and Release APK & AAB](https://github.com/azazmadkiya/azazmadkiya.github.io/actions/workflows/build-apk.yml/badge.svg)](https://github.com/azazmadkiya/azazmadkiya.github.io/actions/workflows/build-apk.yml)

**Doc A4 Print** is an advanced Android application built with **Jetpack Compose** and **Kotlin** that enables users to automatically crop document front and back sides, extract metadata from encrypted PDFs (such as e-Aadhaar cards and official ID documents), and format them perfectly onto an A4 page layout for printing, sharing, and downloading.

---

## 🌟 Key Features

- 🔐 **Protected PDF Decryption & Metadata Extraction**: Securely decrypt password-protected official PDFs (e.g., e-Aadhaar) and view file information such as Title, Author, Creation Date, Subject, and Creator.
- ✂️ **Smart Dual-Side Auto-Cropping**: Automatically detects and extracts the Front and Back sides of ID cards and documents.
- 🖨️ **A4 Print Studio**: Arrange, scale, and preview your document on a standard A4 sheet layout for professional printing.
- 🔒 **Secure Password Export**: Protect your exported A4 PDF documents with user and owner passwords.
- 🚀 **Automated CI/CD Pipeline**: Configured with GitHub Actions to automatically build both signed **APK** (for direct phone installation) and **AAB** (Android App Bundle for Play Store) on every push and publish them directly to GitHub Releases.

---

## 🛠️ Instructions for Building the App Locally

To build and run the app locally using Gradle:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/azazmadkiya/azazmadkiya.github.io.git
   cd azazmadkiya.github.io
   ```

2. **Build Debug APK & AAB via Gradle**:
   - For Debug APK:
     ```bash
     gradle :app:assembleDebug
     ```
   - For Release AAB:
     ```bash
     gradle :app:bundleRelease
     ```

3. **Run Unit Tests**:
   ```bash
   gradle :app:testDebugUnitTest
   ```

---

## ⚙️ GitHub Repository Settings for CI/CD

To enable automatic APK and AAB generation on GitHub Releases:
1. Go to your GitHub repository **Settings** -> **Actions** -> **General**.
2. Under **Workflow permissions**, select **"Read and write permissions"** and click **Save**.
3. Push changes to the `main` branch. The workflow will automatically compile the APK & AAB and publish them under GitHub Releases.
