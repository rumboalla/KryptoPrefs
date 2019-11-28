# KryptoPrefs [![](https://jitpack.io/v/rumboalla/KryptoPrefs.svg)](https://jitpack.io/#rumboalla/KryptoPrefs) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-KryptoPrefs-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7948) [![](https://github.com/rumboalla/KryptoPrefs/workflows/Android%20Tests/badge.svg)](https://github.com/rumboalla/KryptoPrefs/actions?query=workflow%3A%22Android+Tests%22)
**KryptoPrefs** is an open source Kotlin library for handling encrypted SharedPreferences in Android.

## Features
* **Lightweight** library (~70 KB) with **no dependencies**.
* High **compatibility** (API 9 to API 29).
* **Modular**: Use the provided algorithms or make your own.
* Usable from **Java** and **Kotlin**.
* Supports the **Android Keystore**.
* **KryptoContext** for higher level features:
    * **Supported types**: string, int, long, boolean, date, double, float, short, byte, char, enum and json.
    * Types can be **nullable**.
    * Types can be **memory backed**.
    * Types can be **observed**.
    * **Custom types** with your custom transform (e.g. **JSON**). Provided transforms for **Gson** and **Moshi**.
    * **Asynchronous** reads and writes.

## Getting started
Add the **library** to your project:
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.rumboalla.KryptoPrefs:kryptoprefs:0.4.1'
}
```

Create **KryptoPrefs** and start using it:
```kotlin
val prefs = KryptoBuilder.pref(context, "MyPrefs", MyPassword, MySalt, ApiTarget)
prefs.putString("Key", "Value")
val v = prefs.getString("Key", "Default")
```

If you need higher level features, use a **KryptoContext**:
```kotlin
class Prefs(prefs: KryptoPrefs): KryptoContext(prefs) {
    val stringPref = string("stringPref", "defaultValue")
    val intPref = int("intPref", 42)
    val booleanPref = boolean("booleanPref", false)
}

val prefs = Prefs(KryptoBuilder.hybrid(context, "MyPrefs"))
prefs.stringPref("MyString")            // Write to prefs
val stringPref = prefs.stringPref()     // Read from prefs
prefs.stringPref.put("MyString")        // Alternative write to prefs
val stringPref = prefs.stringPref.get() // Alternative read from prefs
```

## Advanced usage
### KryptoPref modes
* **Default**: Uses the best possible encryption method available (no Keystore) at the specified API level.

| API | Encryption | Hash | Key |
|---|---|---|---|
| 9 | AesCbcEncryption | Sha512Hash | PbeKey |
| 10+ | AesCbcEncryption | Sha512Hash | Pbkdf2Key |
| 19+ | AesGcmEncryption | Sha512Hash | Pbkdf2Key |
 
```kotlin
val prefs = KryptoBuilder.pref(context, "MyPrefs", MyPassword, MySalt, ApiTarget)
``` 
* **Keystore**: Uses the Keystore for everything. More security, less performance.

| API | Encryption | Hash | Key |
|---|---|---|---|
| 23+ | AesGcmEncryption | Sha512Hash | KeystoreKey |

```kotlin
val prefs = KryptoBuilder.keystore(context, "MyPrefs")
``` 
* **Hybrid**: Uses the Keystore for the key, normal for everything else. Good compromise between security and performance.

| API | Encryption | Hash | Key |
|---|---|---|---|
| 23+ | AesGcmEncryption | Sha512Hash | HybridKey |

```kotlin
val prefs = KryptoBuilder.hybrid(context, "MyPrefs")
``` 

* **NoCrypt**: Nothing will be encrypted.

| API | Encryption | Hash | Key |
|---|---|---|---|
| 9+ | NoEncryption | NoHash | NoKey |

```kotlin
val prefs = KryptoBuilder.nocrypt(context, "MyPrefs")
``` 

### Custom KryptoPref
You can create your own **KryptoPref**:
```kotlin
val prefs = KryptoPrefsImpl(context, "MyPrefs", AesCbcEncryption(), Sha512Hash(), Pbkdf2Key(MyPassword, MySalt))
```

### Asynchronous operations
You can do **async** reads and writes:
```kotlin
stringPref.putAsync("MyString")
stringPref.getAsync { newValue = it }
```

### Using custom types, collections and JSON
Add kryptoprefs-gson to your project:
```groovy
implementation 'com.github.rumboalla.KryptoPrefs:kryptoprefs-gson:0.4.1'
```
**Or** kryptoprefs-moshi:
```groovy
implementation 'com.github.rumboalla.KryptoPrefs:kryptoprefs-gson:0.4.1'
```
Use **json** type in your **KryptoContext**:
```kotlin
class Prefs(prefs: KryptoPrefs): KryptoContext(prefs) {
    val jsonPref = json("jsonPref", TestClass())
    val listPref = json("listPref", emptyList<TestClass>())
}
```

### Nullable types
Types can be **nullable**:
```kotlin
val nullDatePref = date("nullDatePref", null)
```

### Memory backing
To help performance preferences can be **memory backed**:
```kotlin
val stringPrefBacked = string("stringPrefBacked", "defaultValue", true)
```

### Observing preferences
You can **observe** preferences in a **KryptoContext** by using += or addObserver:
```kotlin
prefs.intPref += { newValue = it }
prefs.intPref.addObserver { newValue = it }
```

## License
Copyright © 2019 rumboalla.  
Licensed under the [MIT](https://github.com/rumboalla/KryptoPrefs/blob/master/LICENSE) license.

