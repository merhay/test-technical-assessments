# Project Name

Automated testing of Android application using Jetpack Compose

## Prerequisites
- Android Studio
- Gradle
- Java 17 or higher
- Kotlin 1.x
- Android Virtual Device: Medium Phone API 35 or higher

## Installation
- Clone the repository
- Open the Android project in Android Studio. Navigate to the project directory:
```bash
cd Android
```
## Running the tests
- Make sure the required Android virtual or physical device is connected

### Using Android Studio
- Run the UI tests using the `Run` button in Android Studio

### Using the Command Line
- **For MacOS/Linux**: 
  ```bash
  ./gradlew connectedAndroidTest
  ```
- **For WINDOWS (Command Prompt)**:
  ```cmd
  gradlew.bat connectedAndroidTest
  ```
## Viewing the test results
- View the test results in the `app/build/reports/tests/testDebugUnitTest/index.html` file
