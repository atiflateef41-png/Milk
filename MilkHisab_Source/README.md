# Milk Hisab — Android Java source

A simple offline milk-accounting app for Android. Package: `com.aatif.milkhisab`.

## Features
- Add, edit and delete customers
- Each customer has an individual rate per KG
- Save daily milk quantity and date
- Monthly customer-wise bill = total KG × current customer rate
- Record amount paid and see pending amount
- View daily entries for each customer/month
- SQLite local database; no internet permission or account required
- Green/white UI with Hindi + English labels

## Open in AIDE
1. Extract this ZIP.
2. In AIDE choose **Open existing project** and select the extracted `MilkHisab` folder (the folder containing `settings.gradle`).
3. Allow Gradle sync if AIDE offers it.
4. Build/Run.

The project uses Android Gradle Plugin 7.4.2, compile SDK 33, min SDK 23, Java 8-compatible source, and no third-party libraries. AIDE versions differ in Gradle support; if your AIDE cannot sync AGP 7.4.2, send the exact error screenshot before changing versions.

## Important billing note
The customer rate is stored on the customer record. The report uses that customer's current saved rate for the selected month. If rates change over time, create a rate-history feature or store the rate snapshot with each milk entry before relying on old invoices. Always check the monthly report before collecting payment.

## Month format
Enter month as `YYYY-MM` (example `2026-11`). Dates use `YYYY-MM-DD`.

## Database
SQLite database file is created automatically on first launch inside app-private storage. Uninstalling the app normally deletes this local data; export/backup is not included in this starter version.


## Build APK with GitHub Actions (no AIDE required)
1. Create a new empty repository on GitHub.
2. Extract this ZIP and upload all files/folders (including the hidden `.github` folder) to the repository root.
3. Open the repository's **Actions** tab. If prompted, enable workflows.
4. Choose **Build Milk Hisab APK** and press **Run workflow**, or push to the `main` branch.
5. When the workflow completes, open its run and download the `MilkHisab-debug-apk` artifact.
6. Extract the downloaded artifact to get `app-debug.apk`.

The workflow builds a debug APK. It is for testing/installing, not a signed Play Store release.

## Build APK with GitHub Actions
1. Create a new empty GitHub repository.
2. Extract this ZIP on your device/computer.
3. Upload the *contents* of the `MilkHisab` folder to the repository root (so `settings.gradle`, `build.gradle`, and `app/` are at the top level).
4. Commit to the `main` branch, or open Actions and run **Build Milk Hisab APK** using **Run workflow**.
5. Open the completed workflow run, go to **Artifacts**, and download `Milk-Hisab-debug-apk`. Extract it to get `app-debug.apk`.

This workflow builds a debug APK only. It does not sign a release APK for Play Store distribution.
