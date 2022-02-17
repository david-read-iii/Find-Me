# Installation
1. Clone this repository by entering this command into Bash:
```bash
git clone git@github.com:david-read-iii/Find-Me.git
```
2. Then, open the root project directory in Android Studio.

# Configuration
## Specify a Google Maps API key in local.properties
1. Register for your Google Maps API key from [this site](https://developers.google.com/maps/documentation/android-sdk/get-api-key).
2. Create a `local.properties` file in the root project directory.
3. Append this line to the file. Replace `...` with your key:
```properties
GOOGLE_MAPS_API_KEY=...
```

# Build APK or Android App Bundle
1. From *Android Studio*, go to the *Build* menu.
2. Go to the *Build Bundle(s) / APK(s)* menu.
3. Select either *Build APK(s)* or *Build Bundle(s)*.
4. Wait for a notification to pop signifying the operation completion.
5. Select *locate* in the notification to navigate to the file location of the built artifact.
6. Install the artifact on your Android device and try it out.

# Maintainers
This project is maintained by:
* [David Read](http://github.com/david-read-iii)
