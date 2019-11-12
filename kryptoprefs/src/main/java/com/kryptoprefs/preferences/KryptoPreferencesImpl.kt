package com.kryptoprefs.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.kryptoprefs.preferences.encryption.Encryption
import com.kryptoprefs.preferences.hash.Hash
import com.kryptoprefs.preferences.key.Key
import java.security.KeyException

class KryptoPrefsImpl internal constructor(
        context: Context,
        private val prefsName: String,
        private val encryption: Encryption,
        private val hash: Hash,
        private val encryptionKey: Key
): KryptoPrefs {

    companion object { const val KEY_ENCRYPTION_TAG = "KEY_ENCRYPTION_TAG" }

    private val preferences: SharedPreferences = context.getSharedPreferences(prefsName, MODE_PRIVATE)

    init { tag() }

    override fun putBoolean(key: String, value: Boolean?): KryptoPrefs = putString(key, value?.toString())

    override fun putInt(key: String, value: Int?): KryptoPrefs = putString(key, value?.toString())

    override fun putLong(key: String, value: Long?): KryptoPrefs  = putString(key, value?.toString())

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = getString(key, defaultValue.toString())?.toBoolean() ?: defaultValue

    override fun getInt(key: String, defaultValue: Int): Int = getString(key, defaultValue.toString())?.toInt() ?: defaultValue

    override fun getLong(key: String, defaultValue: Long): Long = getString(key, defaultValue.toString())?.toLong() ?: defaultValue

    override fun contains(key: String): Boolean = preferences.contains(hash.hash(key))

    override fun name(): String = prefsName

    override fun sharedPreferences(): SharedPreferences = preferences

    @SuppressLint("CommitPrefEdits")
    override fun putString(key: String, value: String?): KryptoPrefs {
        value?.let {
            preferences.edit().putString(hash.hash(key), encryption.encrypt(encryptionKey, value)).apply()
        } ?: remove(key)

        return this
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return if (contains(key)) {
            preferences.getString(hash.hash(key), defaultValue)?.let { encryption.decrypt(encryptionKey, it) } ?: defaultValue
        } else {
            defaultValue
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun remove(key: String): KryptoPrefs {
        preferences.edit().remove(hash.hash(key)).apply()
        return this
    }

    @SuppressLint("CommitPrefEdits")
    override fun clear() {
        preferences.edit().clear().apply()
        tag()
    }

    private fun tag() {
        val tag = "${encryption.tag()}:${hash.tag()}:${encryptionKey.tag()}"
        if (preferences.all.isEmpty()) {
            putString(KEY_ENCRYPTION_TAG, tag)
        } else {
            if (getString("tag", null) != tag) {
                throw KeyException("File is using different encryption method or key.")
            }
        }
    }

}
