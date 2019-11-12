package com.kryptoprefs.preferences.encryption

import com.kryptoprefs.preferences.key.Key

interface Encryption {

    fun encrypt(key: Key, text: String): String

    fun decrypt(key: Key, text: String): String

    fun tag(): String

}
