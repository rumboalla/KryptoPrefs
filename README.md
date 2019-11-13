# KryptoPrefs [![](https://jitpack.io/v/rumboalla/KryptoPrefs.svg)](https://jitpack.io/#rumboalla/KryptoPrefs)
**KryptoPrefs** is an open source Kotlin library for handling encrypted SharedPreferences in Android.

## Features
* **Lightweight** library (~70 KB) with **no dependencies**.
* High **compatibility** (API 9 to API 29).
* **Modular**: Use the provided algorithms or provide your own.
* Usable from **Java** and **Kotlin**.
* Supports the **Android Keystore**.
* **KryptoContext** for higher level features:
    * Supported types: string, int, long, boolean, date, double, float, short, byte, char and enum.
    * Types can be nullable.
    * Types can be memory backed.
    * Types can be observed.
    * Custom types with your custom transform (e.g. JSON).

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

val prefs = Prefs(KryptoBuilder.hybrid(context, "MyPrefs"))
prefs.stringPref = "MyString"           // Write to prefs
val stringPref = prefs.stringPref()     // Read from prefs
```

## Advanced usage
### KryptoPref modes
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
val prefs = KryptoPrefsImpl(context, "MyPrefs", AesCbcEncryption(), Sha512Hash(), Pbkdf2Key(MyPassword, MySalt))
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
And then, use it inside the PreferenceContext:
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
Licensed under the [MIT](https://github.com/rumboalla/KryptoPrefs/blob/master/LICENSE) license.

