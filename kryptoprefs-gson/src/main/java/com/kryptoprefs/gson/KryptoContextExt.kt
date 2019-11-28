package com.kryptoprefs.gson

import com.google.gson.reflect.TypeToken
import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.context.preference.DefaultNullablePreference
import com.kryptoprefs.context.preference.DefaultPreference
import com.kryptoprefs.context.preference.MemoryBackedNullablePreference
import com.kryptoprefs.context.preference.MemoryBackedPreference
import com.kryptoprefs.context.preference.NullablePreference
import com.kryptoprefs.context.preference.Preference
import java.lang.reflect.Type

@JvmOverloads
inline fun <reified T: Any> KryptoContext.json(key: String, defValue: T, backed: Boolean = false): Preference<T> {
	val type: Type = object : TypeToken<T>() {}.type
	return if (backed) {
		val pref = MemoryBackedPreference(prefs, key, defValue, JsonTransform(type), pool)
		backedPrefs.add(pref).let { pref }
	} else {
		DefaultPreference(prefs, key, defValue, JsonTransform(type), pool)
	}
}

@JvmOverloads
inline fun <reified T: Any> KryptoContext.json(key: String, defValue: T?, backed: Boolean = false): NullablePreference<T> {
	val type: Type = object : TypeToken<T>() {}.type
	return if (backed) {
		val pref = MemoryBackedNullablePreference(prefs, key, defValue, JsonTransform(type), pool)
		nullBackedPrefs.add(pref).let { pref }
	} else {
		DefaultNullablePreference(prefs, key, defValue, JsonTransform(type), pool)
	}
}