package com.kryptoprefs.preferences.key

import android.annotation.TargetApi
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@TargetApi(10)
class Pbkdf2Key(pass: String, salt: String): Key {

    companion object {
        private const val ALGORITHM = "PBKDF2WithHmacSHA1"
        private const val ITERATIONS = 1000
        private const val KEY_SIZE = 256
    }

    private val keySpec = PBEKeySpec(pass.toCharArray(), salt.toByteArray(), ITERATIONS, KEY_SIZE)
    private val saltedKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec)

    override fun get(): java.security.Key = saltedKey

    override fun tag() = ALGORITHM

}
