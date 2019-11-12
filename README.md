# KryptoPrefs
**KryptoPrefs** is an open source Kotlin library for handling encrypted SharedPreferences in Android.

## Features
* Small library, no dependencies.
* High compatibility (API 9 to API 29)
* Easy to use and extend.
* Usable from Java and Kotlin.
* Supports several encryption methods, including the Android Keystore.
* Split in two parts, KryptoPrefs for low level and KryptoContext for high level.

## Getting started
Add the library to your project.
```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
    
    dependencies {
        implementation 'com.github.rumboalla:KryptoPrefs:0.1'
    }
```

Create KryptoPrefs and start using it:
```kotlin
    val prefs = KryptoBuilder.pref(context, "MyPrefs", "MyPassword", "MySalt", 10)
    prefs.putString("Key", "Value")
    val v = prefs.getString("Key", "Default")
```

If you need higher level features use a KryptoContext:
```kotlin
    class Prefs(prefs: KryptoPrefs): KryptoContext(prefs) {
        val stringPref = string("stringPref", "defaultValue")
        val intPref = int("intPref", 42)
        val booleanPref = boolean("booleanPref", false)
    }
```

## Advanced usage
TODO

## License
Copyright © 2019 rumboalla.
Licensed under the GNU General Public License v3.

