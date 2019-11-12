package com.kryptoprefs.preferences.key

import javax.crypto.spec.SecretKeySpec

class FixedKey(key: String): Key {

    companion object {
        private const val TAG = "FixedKey"
        private const val KEY_LEN = 16
    }

    private val saltedKey = SecretKeySpec(key.substring(0, KEY_LEN).toByteArray(), "AES")

    override fun get(): java.security.Key = saltedKey

    override fun tag() = TAG

}
