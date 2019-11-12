# KryptoPrefs
**KryptoPrefs** is an open source Kotlin library for handling encrypted SharedPreferences in Android.

## Features
* Small library, no dependencies.
* High compatibility (API 9 to API 29)
* Easy to use and extend.
* Usable from Java and Kotlin.
* Supports several encryption methods, including the Android Keystore.
* Split in two parts, KryptoPrefs for low level and KryptoContext for high level.
* Supports many types: string, int, long, boolean, date, double, float, short, byte, char, enum and custom (create your own transformation). Types can also be nullable and memory backed.

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
val prefs = KryptoBuilder.pref(context, "MyPrefs", MyPassword, MySalt, ApiTarget)
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
### KryptoPref types
* Default: Uses normal encryption for everything.
```kotlin
val prefs = KryptoBuilder.pref(context, "MyPrefs", MyPassword, MySalt, ApiTarget)
``` 
* Keystore: Uses the Keystore for everything. More security, less performance.
```kotlin
val prefs = KryptoBuilder.keystore(context, "MyPrefs")
``` 
* Hybrid: Uses the Keystore for the key, normal for everything else. Good compromise between security and performance.
```kotlin
val prefs = KryptoBuilder.hybrid(context, "MyPrefs")
``` 

### Custom KryptoPref
You can create your own KryptoPref:
```kotlin
KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), Pbkdf2Key(fixedPass, salt))
```

### Using JSON types
In order to have JSON types in your PreferenceContext you have to create a transformer.
Example using Gson:
```kotlin
class JsonTransform<T: Any>(private val type: Class<T>, private val gson: Gson = Gson()): Transform<T> {
    override fun transform(t: T?): String? = gson.toJson(t)
    override fun transform(t: String?): T? = gson.fromJson(t, type)
}
``` 
And the use it, inside the PreferenceContext:
```kotlin
val customPref = custom("customPref", defaultValue, JsonTransform(CustomClass::class.java))
```

### Nullable types
Types can be nullable:
```kotlin
val nullDatePref = date("nullDatePref", null)
```

### Memory backing
To help performance preferences can be memory backed:
```kotlin
val stringPrefBacked = string("stringPrefBacked", "defaultValue", true)
```

### Observing preferences
You can observe preferences in an PreferenceContext by using += or addObserver:
```kotlin
prefs.intPref += { newValue = it }
prefs.intPref.addObserver { newValue = it }
```

## License
Copyright © 2019 rumboalla.

Licensed under the MIT license.

