package com.kryptoprefs.preferences

import android.content.SharedPreferences

interface KryptoPrefs {

    fun putString(key: String, value: String?): KryptoPrefs

    fun putBoolean(key: String, value: Boolean?): KryptoPrefs

    fun putInt(key: String, value: Int?): KryptoPrefs

    fun putLong(key: String, value: Long?): KryptoPrefs

    fun getString(key: String, defaultValue: String?): String?

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun getInt(key: String, defaultValue: Int): Int

    fun getLong(key: String, defaultValue: Long): Long

    fun remove(key: String): KryptoPrefs

    fun contains(key: String): Boolean

    fun clear()

    fun name(): String

    fun sharedPreferences(): SharedPreferences

}
