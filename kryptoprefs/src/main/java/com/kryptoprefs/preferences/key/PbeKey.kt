package com.kryptoprefs.preferences.key

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PbeKey(pass: String, salt: String): Key {

    companion object {
        private const val ALGORITHM = "PBEwithSHA256AND256BITAES-CBC-BC"
        private const val ITERATIONS = 100
        private const val KEY_SIZE = 256
    }

    private val keySpec = PBEKeySpec(pass.toCharArray(), salt.toByteArray(), ITERATIONS, KEY_SIZE)
    private val saltedKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec)

    override fun get(): java.security.Key = saltedKey

    override fun tag() = ALGORITHM

}
