# Project Name

Automated testing of Android application using Jetpack Compose

## Prerequisites
- Android Studio
- Gradle
- Java 17 or higher
- Kotlin 1.x
- Android Virtual Device (AVD): Medium Phone API 35 or higher

## Running the UI tests
**1) Clone the repository**
```bash
git clone https://github.com/merhay/test-technical-assessments.git
```
**2) Open the Android project folder in Android Studio.**

**3) Sync Gradle**
  - Android Studio will automatically detect the Gradle files (build.gradle.kts) and prompt you to sync the project.
  - If not, click File > Sync Project with Gradle Files.

**4) Testing requires an Android virtual or physical device to be connected:**
  - For Virtual Device: Open Android Studio, navigate to Tools > Device Manager, and start or create an AVD.
  - For Physical Device: Connect via USB and enable USB Debugging in developer options.

**5) Run the UI Tests**
   
   **Option 1**: Using Android Studio
    - Run the UI tests using the `Run` button in Android Studio (Requires an Android Test Run/Debug configuration)
    
   **Option 2**: Using the Command Line

   Note: make sure you are in "Android" directory before running the commands
    
  - For MacOS/Linux:
  ```bash
  ./gradlew connectedAndroidTest
  ```
  - **For Windows (Command Prompt)**:
  ```cmd
  gradlew.bat connectedAndroidTest
  ```
## Viewing the test results
- View the test results in the `app/build/reports/tests/testDebugUnitTest/index.html` file
