package com.kryptoprefs

import android.content.Context
import android.provider.Settings
import android.security.keystore.KeyProperties.BLOCK_MODE_CBC
import android.security.keystore.KeyProperties.BLOCK_MODE_GCM
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7
import android.support.test.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.kryptoprefs.preferences.KryptoPrefs
import com.kryptoprefs.preferences.KryptoBuilder
import com.kryptoprefs.preferences.KryptoPrefsImpl
import com.kryptoprefs.preferences.KryptoPrefsImpl.Companion.KEY_ENCRYPTION_TAG
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
import com.kryptoprefs.util.RandomStringGenerator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RunWith(Parameterized::class)
class KryptoPrefsTest {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Array<Any> = arrayOf("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34")

        private const val fixedPass = "6wT7N2C#k#Tv+53DcLX@8d@-9t7-wScWZc@=PeTr?XLkG9VB3K!_mphjC^+aL%y_"
        private const val prefsName = "name"
        private const val hybridName = "$prefsName-hybrid"

    }

    @Suppress("MemberVisibilityCanBePrivate")
    @Parameterized.Parameter
    lateinit var param: String

    private val context: Context = InstrumentationRegistry.getContext()
    private lateinit var prefs: KryptoPrefs

    
    @Before
    fun setup() {
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit().clear().apply()
        context.getSharedPreferences(hybridName, Context.MODE_PRIVATE).edit().clear().apply()

        val cbcKeyStoreKey = KeyStoreKey("CbcAlias", BLOCK_MODE_CBC, ENCRYPTION_PADDING_PKCS7)
        val gcmKeyStoreKey = KeyStoreKey("GcmAlias", BLOCK_MODE_GCM, ENCRYPTION_PADDING_NONE)
        val salt = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        prefs = when (param) {

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
    }

    @Test
    fun string_noValue() {
        assertThat(prefs.getString("TestKey", "DefaultValue")).isEqualTo("DefaultValue")
    }

    @Test
    fun string_noValueWithNullDefault() {
        assertThat(prefs.getString("TestKey", null)).isNull()
    }

    @Test
    fun string_nullValue() {
        prefs.putString("TestKey", "value")
        prefs.putString("TestKey", null)
        assertThat(prefs.contains("TestKey")).isFalse()
    }

    @Test
    fun string_emptyValue() {
        prefs.putString("TestKey", "")
        assertThat(prefs.getString("TestKey", "DefaultValue")).isEqualTo("")
    }

    @Test
    fun string_validValue() {
        prefs.putString("TestKey", "TestValue")
        assertThat(prefs.getString("TestKey", "DefaultValue")).isEqualTo("TestValue")
    }

    @Test
    fun string_validLongValue() {
        val s = RandomStringGenerator.generate(2048)
        prefs.putString("TestKey", s)
        assertThat(prefs.getString("TestKey", "DefaultValue")).isEqualTo(s)
    }

    @Test
    fun boolean_noValue() {
        assertThat(prefs.getBoolean("TestKey", true)).isTrue()
    }

    @Test
    fun boolean_trueValue() {
        prefs.putBoolean("TestKey", true)
        assertThat(prefs.getBoolean("TestKey", false)).isTrue()
    }

    @Test
    fun boolean_falseValue() {
        prefs.putBoolean("TestKey", false)
        assertThat(prefs.getBoolean("TestKey", true)).isFalse()
    }

    @Test
    fun boolean_nullValue() {
        prefs.putBoolean("TestKey", true)
        prefs.putBoolean("TestKey", null)
        assertThat(prefs.contains("TestKey")).isFalse()
    }

    @Test
    fun integer_noValue() {
        assertThat(prefs.getInt("TestKey", 54321)).isEqualTo(54321)
    }

    @Test
    fun integer_validValue() {
        prefs.putInt("TestKey", 12345)
        assertThat(prefs.getInt("TestKey", 54321)).isEqualTo(12345)
    }

    @Test
    fun integer_nullValue() {
        prefs.putInt("TestKey", 1)
        prefs.putInt("TestKey", null)
        assertThat(prefs.contains("TestKey")).isFalse()
    }

    @Test
    fun long_noValue() {
        assertThat(prefs.getLong("TestKey", 54321L)).isEqualTo(54321L)
    }

    @Test
    fun long_validValue() {
        prefs.putLong("TestKey", 12345L)
        assertThat(prefs.getLong("TestKey", 54321L)).isEqualTo(12345L)
    }

    @Test
    fun long_nullValue() {
        prefs.putLong("TestKey", 1L)
        prefs.putLong("TestKey", null)
        assertThat(prefs.contains("TestKey")).isFalse()
    }

    @Test
    fun contains_validKey() {
        assertThat(prefs.contains("TestKey")).isFalse()
        prefs.putString("TestKey", "value")
        assertThat(prefs.contains("TestKey")).isTrue()
        assertThat(prefs.contains("FakeTestKey")).isFalse()
    }

    @Test
    fun remove_validKey() {
        prefs.putString("TestKey", "value")
        assertThat(prefs.contains("TestKey")).isTrue()
        prefs.remove("TestKey")
        assertThat(prefs.contains("TestKey")).isFalse()
    }

    @Test
    fun clear() {
        assertThat(prefs.contains(KEY_ENCRYPTION_TAG)).isTrue()
        prefs.clear()
        assertThat(prefs.contains(KEY_ENCRYPTION_TAG)).isTrue()
    }

    @Test
    fun clear_noTag() {
        prefs.remove(KEY_ENCRYPTION_TAG)
        assertThat(prefs.contains(KEY_ENCRYPTION_TAG)).isFalse()
        prefs.clear()
        assertThat(prefs.contains(KEY_ENCRYPTION_TAG)).isTrue()
    }

    @Test
    fun write_read_20_times() {
        val s = RandomStringGenerator.generate(1024)
        for (i in 0..19) {
            prefs.putString(i.toString(), s)
            assertThat(prefs.getString(i.toString(), null) != null).isTrue()
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun multi_thread() {
        prefs.putInt("TestKey", 12345)
        val pool = Executors.newFixedThreadPool(100)
        for (i in 0..199) {
            pool.submit { assertThat(prefs.getInt("TestKey", 0)).isEqualTo(12345) }
        }
        pool.shutdown()
        assertThat(pool.awaitTermination(10, TimeUnit.SECONDS)).isTrue()
    }

}
