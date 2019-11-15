package com.kryptoprefs.context

import com.kryptoprefs.context.transform.ByteTransform
import com.kryptoprefs.context.transform.CharTransform
import com.kryptoprefs.context.transform.DateTransform
import com.kryptoprefs.context.transform.DoubleTransform
import com.kryptoprefs.context.transform.EnumTransform
import com.kryptoprefs.context.transform.FloatTransform
import com.kryptoprefs.context.transform.ShortTransform
import com.kryptoprefs.context.preference.DefaultNullablePreference
import com.kryptoprefs.context.preference.DefaultPreference
import com.kryptoprefs.context.preference.MemoryBackedNullablePreference
import com.kryptoprefs.context.preference.MemoryBackedPreference
import com.kryptoprefs.context.preference.NullablePreference
import com.kryptoprefs.context.preference.Preference
import com.kryptoprefs.context.transform.Transform
import com.kryptoprefs.preferences.KryptoPrefs
import java.lang.Runtime.getRuntime
import java.util.Date
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

open class KryptoContext @JvmOverloads protected constructor(
	val prefs: KryptoPrefs,
	val pool: ExecutorService = Executors.newFixedThreadPool(getRuntime().availableProcessors())
) {

    val backedPrefs = mutableListOf<MemoryBackedPreference<*>>()
    val nullBackedPrefs = mutableListOf<MemoryBackedNullablePreference<*>>()

    /**
     * Creates a {@String} preference
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@String}                       Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@NullablePreference<String>}
     */
    @JvmOverloads
    @JvmName("nullStringPref")
    protected fun string(key: String, defValue: String? = null, backed: Boolean = false): NullablePreference<String> {
        return if (backed) {
            val pref = MemoryBackedNullablePreference(prefs, key, defValue, null, pool)
            nullBackedPrefs.add(pref).let { pref }
        } else {
            DefaultNullablePreference(prefs, key, defValue, null, pool)
        }
    }

    /**
     * Creates a Custom preference.
     * Custom preferences need a {@Transform} object that will transform from T to String and String to T.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@T}                            Default value for the preference.
     * @param       trans       {@Transform<T>}                 Transforms T to String and String to T.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@NullablePreference<T>}
     *
     * If you need to create a json or enum from Java, use custom like this:
     *      val jsonPref = custom("jsonPref", PreferenceTest.jsonDefaultValue, JsonTransform(JsonClass::class.java))
     *      val jsonPrefBacked = custom("jsonPrefBacked", PreferenceTest.jsonDefaultValue, JsonTransform(JsonClass::class.java))
     *      val enumPref = custom("enumPref", Pillar.D, EnumTransform(Pillar::class.java))
     *      val enumPrefBacked = custom("enumPrefBacked", Pillar.D, EnumTransform(Pillar::class.java))
     */
    @JvmOverloads
    @JvmName("nullCustomPref")
    protected fun <T: Any> custom(key: String, defValue: T?, trans: Transform<T>, backed: Boolean = false): NullablePreference<T> {
        return if (backed) {
            val pref = MemoryBackedNullablePreference(prefs, key, defValue, trans, pool)
            nullBackedPrefs.add(pref).let { pref }
        } else {
            DefaultNullablePreference(prefs, key, defValue, trans, pool)
        }
    }

    /**
     * Creates a Date preference.
     *
     * @receiver            {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param   key         {@String}                       Key for the preference.
     * @param   defValue    {@Date}                         Default value for the preference.
     * @param   backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@NullablePreference<T>}
     */
    @JvmOverloads
    @JvmName("nullDatePref")
    protected fun date(key: String, defValue: Date?, backed: Boolean = false): NullablePreference<Date> {
        return if (backed) {
            val pref = MemoryBackedNullablePreference(prefs, key, defValue, DateTransform(), pool)
            nullBackedPrefs.add(pref).let { pref }
        } else {
            DefaultNullablePreference(prefs, key, defValue, DateTransform(), pool)
        }
    }

    /**
     * Creates a Custom preference.
     * Custom preferences need a {@Transform} object that will transform from T to String and String to T.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@T}                            Default value for the preference.
     * @param       trans       {@Transform<T>}                 Transforms T to String and String to T.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<T>}
     *
     * If you need to create a json or enum from Java, use custom like this:
     *      val jsonPref = custom("jsonPref", PreferenceTest.jsonDefaultValue, JsonTransform(JsonClass::class.java))
     *      val jsonPrefBacked = custom("jsonPrefBacked", PreferenceTest.jsonDefaultValue, JsonTransform(JsonClass::class.java))
     *      val enumPref = custom("enumPref", Pillar.D, EnumTransform(Pillar::class.java))
     *      val enumPrefBacked = custom("enumPrefBacked", Pillar.D, EnumTransform(Pillar::class.java))
     */
    @JvmOverloads
    @JvmName("customPref")
    protected fun <T: Any> custom(key: String, defValue: T, trans: Transform<T>, backed: Boolean = false): Preference<T> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, trans, pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, trans, pool)
        }
    }

    /**
     * Creates a Date preference.
     *
     * @receiver            {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param   key         {@String}                       Key for the preference.
     * @param   defValue    {@Date}                         Default value for the preference.
     * @param   backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<Date>}
     */
    @JvmOverloads
    @JvmName("datePref")
    protected fun date(key: String, defValue: Date, backed: Boolean = false): Preference<Date> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, DateTransform(), pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, DateTransform(), pool)
        }
    }

    /**
     * Creates a {@String} preference
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@String}                       Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<String>}
     */
    @JvmOverloads
    @JvmName("stringPref")
    protected fun string(key: String, defValue: String, backed: Boolean = false): Preference<String> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, null, pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, null, pool)
        }
    }

    /**
     * Creates an Enum preference.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@T}                            Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<T>}
     */
    @JvmOverloads
    protected inline fun <reified T: Enum<T>> enum(key: String, defValue: T, backed: Boolean = false): Preference<T> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, EnumTransform(T::class.java), pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, EnumTransform(T::class.java), pool)
        }
    }

    /**
     * Creates a nullable Enum preference.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@T}                            Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@NullablePreference<T>}
     */
    @JvmOverloads
    protected inline fun <reified T: Enum<T>> enum(key: String, defValue: T?, backed: Boolean = false): NullablePreference<T> {
        return if (backed) {
            val pref = MemoryBackedNullablePreference(prefs, key, defValue, EnumTransform(T::class.java), pool)
            nullBackedPrefs.add(pref).let { pref }
        } else {
            DefaultNullablePreference(prefs, key, defValue, EnumTransform(T::class.java), pool)
        }
    }

    /**
     * Creates a Double preference.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@Double}                       Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<T>}
     */
    @JvmOverloads
    @JvmName("doublePref")
    protected fun double(key: String, defValue: Double, backed: Boolean = false): Preference<Double> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, DoubleTransform(), pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, DoubleTransform(), pool)
        }
    }

    /**
     * Creates a Short preference.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@Short}                        Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<T>}
     */
    @JvmOverloads
    @JvmName("shortPref")
    protected fun short(key: String, defValue: Short, backed: Boolean = false): Preference<Short> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, ShortTransform(), pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, ShortTransform(), pool)
        }
    }

    /**
     * Creates a Float preference.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@Float}                        Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<T>}
     */
    @JvmOverloads
    @JvmName("floatPref")
    protected fun float(key: String, defValue: Float, backed: Boolean = false): Preference<Float> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, FloatTransform(), pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, FloatTransform(), pool)
        }
    }

    /**
     * Creates an {@Int} preference.
     *
     * @receiver            {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param   key         {@String}                       Key for the preference.
     * @param   defValue    {@Int}                          Default value for the preference.
     * @param   backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<Int>}
     */
    @JvmOverloads
    @JvmName("intPref")
    protected fun int(key: String, defValue: Int, backed: Boolean = false): Preference<Int> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, null, pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, null, pool)
        }
    }

    /**
     * Creates a {@Char} preference.
     *
     * @receiver            {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param   key         {@String}                       Key for the preference.
     * @param   defValue    {@Char}                         Default value for the preference.
     * @param   backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<Char>}
     */
    @JvmOverloads
    @JvmName("charPref")
    protected fun char(key: String, defValue: Char, backed: Boolean = false): Preference<Char> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, CharTransform(), pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, CharTransform(), pool)
        }
    }

    /**
     * Creates a {@Byte} preference.
     *
     * @receiver            {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param   key         {@String}                       Key for the preference.
     * @param   defValue    {@Byte}                         Default value for the preference.
     * @param   backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<Byte>}
     */
    @JvmOverloads
    @JvmName("bytePref")
    protected fun byte(key: String, defValue: Byte, backed: Boolean = false): Preference<Byte> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, ByteTransform(), pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, ByteTransform(), pool)
        }
    }

    /**
     * Creates a {@Long} preference.
     *
     * @receiver            {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param   key         {@String}                       Key for the preference.
     * @param   defValue    {@Long}                         Default value for the preference.
     * @param   backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<Int>}
     */
    @JvmOverloads
    @JvmName("longPref")
    protected fun long(key: String, defValue: Long, backed: Boolean = false): Preference<Long> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, null, pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, null, pool)
        }
    }

    /**
     * Creates a {@Boolean} preference.
     *
     * @receiver                {@PreferenceContext}            Configuration object with the pool and EncryptedPreferences.
     * @param       key         {@String}                       Key for the preference.
     * @param       defValue    {@Boolean}                      Default value for the preference.
     * @param       backed      {@Boolean}                      Specifies if this preference will have a memory backed value.
     *
     * @return  {@Preference<Boolean>}
     */
    @JvmOverloads
    @JvmName("booleanPref")
    protected fun boolean(key: String, defValue: Boolean, backed: Boolean = false): Preference<Boolean> {
        return if (backed) {
            val pref = MemoryBackedPreference(prefs, key, defValue, null, pool)
            backedPrefs.add(pref).let { pref }
        } else {
            DefaultPreference(prefs, key, defValue, null, pool)
        }
    }

    /**
     * Shortcut for clearing the preferences.
     */
    fun clear() {
        nullBackedPrefs.forEach { it.invalidate() }
        backedPrefs.forEach { it.invalidate() }
        prefs.clear()
    }

}
