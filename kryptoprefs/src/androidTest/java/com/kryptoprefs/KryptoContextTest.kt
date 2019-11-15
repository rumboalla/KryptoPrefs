package com.kryptoprefs

import android.content.Context
import android.provider.Settings
import android.security.keystore.KeyProperties
import android.support.test.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.context.transform.Transform
import com.kryptoprefs.preferences.KryptoBuilder
import com.kryptoprefs.preferences.KryptoPrefs
import com.kryptoprefs.preferences.KryptoPrefsImpl
import com.kryptoprefs.preferences.encryption.AesCbcEncryption
import com.kryptoprefs.preferences.encryption.AesGcmEncryption
import com.kryptoprefs.preferences.hash.Md5Hash
import com.kryptoprefs.preferences.hash.Sha256Hash
import com.kryptoprefs.preferences.hash.Sha512Hash
import com.kryptoprefs.preferences.key.FixedKey
import com.kryptoprefs.preferences.key.HybridKey
import com.kryptoprefs.preferences.key.KeyStoreKey
import com.kryptoprefs.preferences.key.PbeKey
import com.kryptoprefs.preferences.key.Pbkdf2Key
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Date
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(Parameterized::class)
class KryptoContextTest {

    companion object {

        private const val fixedPass = "6wT7N2C#k#Tv+53DcLX@8d@-9t7-wScWZc@=PeTr?XLkG9VB3K!_mphjC^+aL%y_"
        private const val intValue = 100
        private const val longValue = 100L
        private const val stringValue = "Don't panic."
        private const val doubleValue = 100.0
        private const val booleanValue = true
        private const val floatValue = 100.0f
        private const val shortValue: Short = 100
        private const val charValue = 'V'
        private const val byteValue: Byte = 0x42
        private const val prefsName = "name"
        private const val hybridName = "$prefsName-hybrid"

        private val dateValue = Date(longValue)

        const val intDefaultValue = -1
        const val longDefaultValue = -1L
        const val doubleDefaultValue = 0.0
        const val booleanDefaultValue = false
        const val floatDefaultValue = 0.0f
        const val shortDefaultValue: Short = 0
        const val stringDefaultValue = "42"
        const val charDefaultValue = 'd'
        const val byteDefaultValue: Byte = 0x2A

        val dateDefaultValue = Date(longDefaultValue)

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Array<Any> = arrayOf("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34")
    }

    private val context: Context = InstrumentationRegistry.getContext()
    private lateinit var prefs: TestPreference

    @Suppress("MemberVisibilityCanBePrivate")
    @Parameterized.Parameter
    lateinit var param: String

    @Before
    fun setup() {
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit().clear().apply()
        context.getSharedPreferences(hybridName, Context.MODE_PRIVATE).edit().clear().apply()

        val cbcKeyStoreKey = KeyStoreKey("CbcAlias", KeyProperties.BLOCK_MODE_CBC, KeyProperties.ENCRYPTION_PADDING_PKCS7)
        val gcmKeyStoreKey = KeyStoreKey("GcmAlias", KeyProperties.BLOCK_MODE_GCM, KeyProperties.ENCRYPTION_PADDING_NONE)
        val salt = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val iprefs = when (param) {

            "00" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), Pbkdf2Key(fixedPass, salt))
            "01" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), PbeKey(fixedPass, salt))
            "02" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), cbcKeyStoreKey)
            "03" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), FixedKey(fixedPass))
            "04" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), HybridKey(context, prefsName))

            "05" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha256Hash(), Pbkdf2Key(fixedPass, salt))
            "06" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha256Hash(), PbeKey(fixedPass, salt))
            "07" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha256Hash(), cbcKeyStoreKey)
            "08" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha256Hash(), FixedKey(fixedPass))
            "09" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), HybridKey(context, prefsName))

            "10" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Md5Hash(), Pbkdf2Key(fixedPass, salt))
            "11" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Md5Hash(), PbeKey(fixedPass, salt))
            "12" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Md5Hash(), cbcKeyStoreKey)
            "13" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Md5Hash(), FixedKey(fixedPass))
            "14" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), HybridKey(context, prefsName))

            "15" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha512Hash(), Pbkdf2Key(fixedPass, salt))
            "16" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha512Hash(), PbeKey(fixedPass, salt))
            "17" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha512Hash(), gcmKeyStoreKey)
            "18" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha512Hash(), FixedKey(fixedPass))
            "19" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), HybridKey(context, prefsName))

            "20" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha256Hash(), Pbkdf2Key(fixedPass, salt))
            "21" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha256Hash(), PbeKey(fixedPass, salt))
            "22" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha256Hash(), gcmKeyStoreKey)
            "23" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Sha256Hash(), FixedKey(fixedPass))
            "24" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), HybridKey(context, prefsName))

            "25" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Md5Hash(), Pbkdf2Key(fixedPass, salt))
            "26" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Md5Hash(), PbeKey(fixedPass, salt))
            "27" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Md5Hash(), gcmKeyStoreKey)
            "28" -> KryptoPrefsImpl(context, prefsName, AesGcmEncryption(), Md5Hash(), FixedKey(fixedPass))
            "29" -> KryptoPrefsImpl(context, prefsName, AesCbcEncryption(), Sha512Hash(), HybridKey(context, prefsName))

            "30" -> KryptoBuilder.pref(context, prefsName, fixedPass, salt, 9)
            "31" -> KryptoBuilder.pref(context, prefsName, fixedPass, salt, 10)
            "32" -> KryptoBuilder.pref(context, prefsName, fixedPass, salt, 19)
            "33" -> KryptoBuilder.keystore(context, prefsName)
            "34" -> KryptoBuilder.hybrid(context, prefsName)

            else -> throw RuntimeException("Invalid value.")
        }

        prefs = TestPreference(iprefs)
    }

    @Test
    fun preference_date() {
        // Date
        assertThat(prefs.datePref.get() == dateDefaultValue).isTrue()
        prefs.datePref.put(dateValue)
        assertThat(prefs.datePref.get() == dateValue).isTrue()

        // DateBacked
        assertThat(prefs.datePrefBacked.get() == dateDefaultValue).isTrue()
        prefs.datePrefBacked.put(dateValue)
        assertThat(prefs.datePrefBacked.get() == dateValue).isTrue()
    }

    @Test
    fun preference_nullDate() {
        // Date
        assertThat(prefs.nullDatePref.get() == null).isTrue()
        prefs.nullDatePref.put(dateValue)
        assertThat(prefs.nullDatePref.get() == dateValue).isTrue()

        // DateBacked
        assertThat(prefs.nullDatePrefBacked.get() == null).isTrue()
        prefs.nullDatePrefBacked.put(dateValue)
        assertThat(prefs.nullDatePrefBacked.get() == dateValue).isTrue()
    }

    @Test
    fun preference_int() {
        // Int
        assertThat(prefs.intPref.get() == intDefaultValue).isTrue()
        prefs.intPref.put(intValue)
        assertThat(prefs.intPref.get() == intValue).isTrue()

        // IntBacked
        assertThat(prefs.intPrefBacked.get() == intDefaultValue).isTrue()
        prefs.intPrefBacked.put(intValue)
        assertThat(prefs.intPrefBacked.get() == intValue).isTrue()
    }

    @Test
    fun preference_boolean() {
        // Boolean
        assertThat(!prefs.booleanPref.get()).isTrue()
        prefs.booleanPref.put(booleanValue)
        assertThat(prefs.booleanPref.get()).isTrue()

        // BooleanBacked
        assertThat(!prefs.booleanPrefBacked.get()).isTrue()
        prefs.booleanPrefBacked.put(booleanValue)
        assertThat(prefs.booleanPrefBacked.get()).isTrue()
    }

    @Test
    fun preference_long() {
        // Long
        assertThat(prefs.longPref.get() == longDefaultValue).isTrue()
        prefs.longPref.put(longValue)
        assertThat(prefs.longPref.get() == longValue).isTrue()

        // LongBacked
        assertThat(prefs.longPrefBacked.get() == longDefaultValue).isTrue()
        prefs.longPrefBacked.put(longValue)
        assertThat(prefs.longPrefBacked.get() == longValue).isTrue()
    }

    @Test
    fun preference_double() {
        // Double
        assertThat(prefs.doublePref.get() == doubleDefaultValue).isTrue()
        prefs.doublePref.put(doubleValue)
        assertThat(prefs.doublePref.get() == doubleValue).isTrue()

        // DoubleBacked
        assertThat(prefs.doublePrefBacked.get() == doubleDefaultValue).isTrue()
        prefs.doublePrefBacked.put(doubleValue)
        assertThat(prefs.doublePrefBacked.get() == doubleValue).isTrue()
    }

    @Test
    fun preference_float() {
        // Double
        assertThat(prefs.floatPref.get() == floatDefaultValue).isTrue()
        prefs.floatPref.put(floatValue)
        assertThat(prefs.floatPref.get() == floatValue).isTrue()

        // DoubleBacked
        assertThat(prefs.floatPrefBacked.get() == floatDefaultValue).isTrue()
        prefs.floatPrefBacked.put(floatValue)
        assertThat(prefs.floatPrefBacked.get() == floatValue).isTrue()
    }

    @Test
    fun preference_short() {
        // Double
        assertThat(prefs.shortPref.get() == shortDefaultValue).isTrue()
        prefs.shortPref.put(shortValue)
        assertThat(prefs.shortPref.get() == shortValue).isTrue()

        // DoubleBacked
        assertThat(prefs.shortPrefBacked.get() == shortDefaultValue).isTrue()
        prefs.shortPrefBacked.put(shortValue)
        assertThat(prefs.shortPrefBacked.get() == shortValue).isTrue()
    }

    @Test
    fun preference_char() {
        // Char
        assertThat(prefs.charPref.get() == charDefaultValue).isTrue()
        prefs.charPref.put(charValue)
        assertThat(prefs.charPref.get() == charValue).isTrue()

        // CharBacked
        assertThat(prefs.charPrefBacked.get() == charDefaultValue).isTrue()
        prefs.charPrefBacked.put(charValue)
        assertThat(prefs.charPrefBacked.get() == charValue).isTrue()
    }

    @Test
    fun preference_byte() {
        // Double
        assertThat(prefs.bytePref.get() == byteDefaultValue).isTrue()
        prefs.bytePref.put(byteValue)
        assertThat(prefs.bytePref.get() == byteValue).isTrue()

        // DoubleBacked
        assertThat(prefs.bytePrefBacked.get() == byteDefaultValue).isTrue()
        prefs.bytePrefBacked.put(byteValue)
        assertThat(prefs.bytePrefBacked.get() == byteValue).isTrue()
    }

    @Test
    fun preference_string() {
        // String
        assertThat(prefs.stringPref.get() == stringDefaultValue).isTrue()
        prefs.stringPref.put(stringValue)
        assertThat(prefs.stringPref.get() == stringValue).isTrue()

        // StringBacked
        assertThat(prefs.stringPrefBacked.get() == stringDefaultValue).isTrue()
        prefs.stringPrefBacked.put(stringValue)
        assertThat(prefs.stringPrefBacked.get() == stringValue).isTrue()
    }

    @Test
    fun preference_nullString() {
        // String
        assertThat(prefs.nullStringPref.get() == null).isTrue()
        prefs.nullStringPref.put(stringValue)
        assertThat(prefs.nullStringPref.get() == stringValue).isTrue()

        // StringBacked
        assertThat(prefs.nullStringPrefBacked.get() == null).isTrue()
        prefs.nullStringPrefBacked.put(stringValue)
        assertThat(prefs.nullStringPrefBacked.get() == stringValue).isTrue()
    }

    @Test
    fun preference_enum() {
        // Enum
        assertThat(prefs.enumPref.get() == TestEnum.D).isTrue()
        prefs.enumPref.put(TestEnum.F)
        assertThat(prefs.enumPref.get() == TestEnum.F).isTrue()

        // EnumBacked
        assertThat(prefs.enumPrefBacked.get() == TestEnum.D).isTrue()
        prefs.enumPrefBacked.put(TestEnum.F)
        assertThat(prefs.enumPrefBacked.get() == TestEnum.F).isTrue()

        // nullEnum
        assertThat(prefs.nullEnumPref()).isEqualTo(null)
        prefs.nullEnumPref(TestEnum.F)
        assertThat(prefs.nullEnumPref()).isEqualTo(TestEnum.F)

        // nullEnumBacked
        assertThat(prefs.nullEnumPrefBacked()).isEqualTo(null)
        prefs.nullEnumPrefBacked(TestEnum.F)
        assertThat(prefs.nullEnumPrefBacked()).isEqualTo(TestEnum.F)
    }

    @Test
    fun preference_custom() {
        // Custom
        assertThat(prefs.customPref.get().string).isEqualTo(stringDefaultValue)
        assertThat(prefs.customPref.get().number).isEqualTo(intDefaultValue)
        prefs.customPref.put(CustomClass(intValue, stringValue))
        assertThat(prefs.customPref.get().string).isEqualTo(stringValue)
        assertThat(prefs.customPref.get().number).isEqualTo(intValue)

        // CustomBacked
        assertThat(prefs.customPrefBacked.get().string).isEqualTo(stringDefaultValue)
        assertThat(prefs.customPrefBacked.get().number).isEqualTo(intDefaultValue)
        prefs.customPrefBacked.put(CustomClass(intValue, stringValue))
        assertThat(prefs.customPrefBacked.get().string).isEqualTo(stringValue)
        assertThat(prefs.customPrefBacked.get().number).isEqualTo(intValue)

        // nullCustom
        assertThat(prefs.nullCustomPref.get()?.string).isEqualTo(null)
        assertThat(prefs.nullCustomPref.get()?.number).isEqualTo(null)
        prefs.nullCustomPref.put(CustomClass(intValue, stringValue))
        assertThat(prefs.nullCustomPref.get()?.string).isEqualTo(stringValue)
        assertThat(prefs.nullCustomPref.get()?.number).isEqualTo(intValue)

        // nullCustomBacked
        assertThat(prefs.nullCustomPrefBacked.get()?.string).isEqualTo(null)
        assertThat(prefs.nullCustomPrefBacked.get()?.number).isEqualTo(null)
        prefs.nullCustomPrefBacked.put(CustomClass(intValue, stringValue))
        assertThat(prefs.nullCustomPrefBacked.get()?.string).isEqualTo(stringValue)
        assertThat(prefs.nullCustomPrefBacked.get()?.number).isEqualTo(intValue)
    }

    @Test
    fun preference_invoke() {
        assertThat(prefs.intPref(intValue)).isEqualTo(intValue)
        assertThat(prefs.intPref()).isEqualTo(intValue)

        assertThat(prefs.intPrefBacked(intValue)).isEqualTo(intValue)
        assertThat(prefs.intPrefBacked()).isEqualTo(intValue)

        assertThat(prefs.stringPref(stringValue)).isEqualTo(stringValue)
        assertThat(prefs.stringPref()).isEqualTo(stringValue)

        assertThat(prefs.stringPrefBacked(stringValue)).isEqualTo(stringValue)
        assertThat(prefs.stringPrefBacked()).isEqualTo(stringValue)
    }

    @Test
    @SuppressWarnings("LongMethod")
    fun preference_observe() {
        // DefaultPreference
        var latch = CountDownLatch(2)
        prefs.intPref.addObserver { latch.countDown() }
        prefs.intPref.put(intDefaultValue)
        prefs.intPref.putAsync(intValue)
        assertThat(latch.await(1, TimeUnit.SECONDS)).isTrue()
        assertThat(prefs.intPref.get()).isEqualTo(intValue)

        // MemoryBackedPreference
        latch = CountDownLatch(2)
        prefs.intPrefBacked.addObserver { latch.countDown() }
        prefs.intPrefBacked.put(intValue)
        prefs.intPrefBacked.putAsync(intValue)
        assertThat(latch.await(1, TimeUnit.SECONDS)).isTrue()
        assertThat(prefs.intPrefBacked.get()).isEqualTo(intValue)

        // NullablePreference
        latch = CountDownLatch(2)
        prefs.stringPref.addObserver { latch.countDown() }
        prefs.stringPref.put(stringDefaultValue)
        prefs.stringPref.putAsync(stringValue)
        assertThat(latch.await(1, TimeUnit.SECONDS)).isTrue()
        assertThat(prefs.stringPref.get()).isEqualTo(stringValue)

        // MemoryBackedNullablePreference
        latch = CountDownLatch(2)
        prefs.stringPrefBacked.addObserver { latch.countDown() }
        prefs.stringPrefBacked.put(stringDefaultValue)
        prefs.stringPrefBacked.putAsync(stringValue)
        assertThat(latch.await(1, TimeUnit.SECONDS)).isTrue()
        assertThat(prefs.stringPrefBacked.get()).isEqualTo(stringValue)
    }

    @Test
    fun preference_observable() {
        // Preference
        var latch = CountDownLatch(4)
        val observer = { _: Int? -> latch.countDown() }

        prefs.intPref += observer
        prefs.intPref.put(intDefaultValue)
        prefs.intPref -= observer

        prefs.intPref.addObserver(observer)
        prefs.intPref.put(intDefaultValue)
        prefs.intPref.deleteObserver(observer)

        prefs.intPref += observer
        prefs.intPref.put(intDefaultValue)
        prefs.intPref.deleteObservers()

        prefs.intPref.put(intValue)
        assertThat(latch.count).isEqualTo(1)

        // NullablePreference
        latch = CountDownLatch(4)
        val observer2 = { _: String? -> latch.countDown() }

        prefs.nullStringPref += observer2
        prefs.nullStringPref.put(stringDefaultValue)
        prefs.nullStringPref -= observer2

        prefs.nullStringPref.addObserver(observer2)
        prefs.nullStringPref.put(stringDefaultValue)
        prefs.nullStringPref.deleteObserver(observer2)

        prefs.nullStringPref += observer2
        prefs.nullStringPref.put(stringDefaultValue)
        prefs.nullStringPref.deleteObservers()

        prefs.stringPref.put(stringValue)
        assertThat(latch.count).isEqualTo(1)
    }

    @Test
    fun preference_invalidate() {
        prefs.intPrefBacked(intValue)
        prefs.nullStringPrefBacked(stringValue)

        prefs.clear()

        assertThat(prefs.intPrefBacked()).isEqualTo(intDefaultValue)
        assertThat(prefs.nullStringPrefBacked()).isEqualTo(null)
    }

    @Test
    fun test() {
//        val l = listOf("String", "Test", "dfjsodklfj")
//        prefs.json(l)
//        val j = prefs.json()
//        assertThat(j == l)
    }

    @Test
    @SuppressWarnings("MagicNumber")
    fun preference_async() {
        val latch = CountDownLatch(4)

        prefs.intPref.put(intDefaultValue)
        prefs.intPref.putAsync(intValue)
        assertThat(prefs.intPref.get()).isEqualTo(intValue)
        prefs.intPref.getAsync {
            assertThat(it).isEqualTo(intValue)
            latch.countDown()
        }

        prefs.intPrefBacked.put(intDefaultValue)
        prefs.intPrefBacked.putAsync(intValue)
        assertThat(prefs.intPrefBacked.get()).isEqualTo(intValue)
        prefs.intPrefBacked.getAsync {
            assertThat(it).isEqualTo(intValue)
            latch.countDown()
        }

        prefs.stringPref.put(stringDefaultValue)
        prefs.stringPref.putAsync(stringValue)
        assertThat(prefs.stringPref.get()).isEqualTo(stringValue)
        prefs.stringPref.getAsync {
            assertThat(it).isEqualTo(stringValue)
            latch.countDown()
        }

        prefs.stringPrefBacked.put(stringDefaultValue)
        prefs.stringPrefBacked.putAsync(stringValue)
        assertThat(prefs.stringPrefBacked.get()).isEqualTo(stringValue)
        prefs.stringPrefBacked.getAsync {
            assertThat(it).isEqualTo(stringValue)
            latch.countDown()
        }

        assertThat(latch.await(1, TimeUnit.SECONDS)).isTrue()
    }

}

class TestPreference(prefs: KryptoPrefs) : KryptoContext(prefs) {

    val stringPref = string("stringPref", KryptoContextTest.stringDefaultValue)
    val stringPrefBacked = string("stringPrefBacked", KryptoContextTest.stringDefaultValue, true)

    val nullStringPref = string("nullStringPref", null)
    val nullStringPrefBacked = string("nullStringPrefBacked", null, true)

    val intPref = int("intPref", KryptoContextTest.intDefaultValue)
    val intPrefBacked = int("intPrefBacked", KryptoContextTest.intDefaultValue, true)

    val longPref = long("longPref", KryptoContextTest.longDefaultValue)
    val longPrefBacked = long("longPrefBacked", KryptoContextTest.longDefaultValue, true)

    val booleanPref = boolean("booleanPref", KryptoContextTest.booleanDefaultValue)
    val booleanPrefBacked = boolean("booleanPrefBacked", false, true)

    val datePref = date("datePref", KryptoContextTest.dateDefaultValue)
    val datePrefBacked = date("datePrefBacked", KryptoContextTest.dateDefaultValue, true)

    val nullDatePref = date("nullDatePref", null)
    val nullDatePrefBacked = date("nullDatePrefBacked", null, true)

    val doublePref = double("doublePref", KryptoContextTest.doubleDefaultValue)
    val doublePrefBacked = double("doublePrefBacked", KryptoContextTest.doubleDefaultValue, true)

    val floatPref = float("floatPref", KryptoContextTest.floatDefaultValue)
    val floatPrefBacked = float("floatPrefBacked", KryptoContextTest.floatDefaultValue, true)

    val shortPref = short("shortPref", KryptoContextTest.shortDefaultValue)
    val shortPrefBacked = short("shortPrefBacked", KryptoContextTest.shortDefaultValue, true)

    val bytePref = byte("bytePref", KryptoContextTest.byteDefaultValue)
    val bytePrefBacked = byte("bytePrefBacked", KryptoContextTest.byteDefaultValue, true)

    val charPref = char("charPref", KryptoContextTest.charDefaultValue)
    val charPrefBacked = char("charPrefBaked", KryptoContextTest.charDefaultValue, true)

    val enumPref = enum("enumPref", TestEnum.D)
    val enumPrefBacked = enum("enumPrefBacked", TestEnum.D, true)

    val nullEnumPref = enum<TestEnum>("nullEnumPref", null)
    val nullEnumPrefBacked = enum<TestEnum>("nullEnumPrefBacked", null, true)

    private val customClass = CustomClass(KryptoContextTest.intDefaultValue, KryptoContextTest.stringDefaultValue)
    val customPref = custom("customPref", customClass, JsonTransform(CustomClass::class.java))
    val customPrefBacked = custom("customPrefBacked", customClass, JsonTransform(CustomClass::class.java), true)
    val nullCustomPref = custom("nullCustomPref", null, JsonTransform(CustomClass::class.java))
    val nullCustomPrefBacked = custom("nullCustomPrefBacked", null, JsonTransform(CustomClass::class.java), true)

}

enum class TestEnum { A, B, C, D, E, F, G, H }

class CustomClass(val number: Int, val string: String)

class JsonTransform<T: Any>(private val type: Class<T>, private val gson: Gson = Gson()): Transform<T> {
    override fun transform(t: T?): String? = gson.toJson(t)
    override fun transform(t: String?): T? = gson.fromJson(t, type)
}
