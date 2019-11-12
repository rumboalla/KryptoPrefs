package com.kryptoprefs.preferences.key

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator

@TargetApi(Build.VERSION_CODES.M)
class KeyStoreKey(
        private val alias: String,
        private val mode: String,
        private val padding: String,
        private val userAuthenticationRequired: Boolean = false,
        private val userAuthenticationTimeout: Int = Int.MAX_VALUE
) : Key {

    companion object {
        private const val STORETYPE = "AndroidKeyStore"
        private const val KEY_SIZE = 256
    }

    private val keyStore = KeyStore.getInstance(STORETYPE).apply { load(null) }
    private val key = createKey()

    init { if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) throw RuntimeException("Android M Needed.") }

    override fun get() = key

    override fun tag() = STORETYPE

    private fun createKey(): java.security.Key {
        if (!keyStore.containsAlias(alias)) {
            val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, STORETYPE)
            val builder =  KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
                    .setBlockModes(mode)
                    .setEncryptionPaddings(padding)
                    .setKeySize(KEY_SIZE)

            if (userAuthenticationRequired) {
                builder.setUserAuthenticationRequired(userAuthenticationRequired)
                builder.setUserAuthenticationValidityDurationSeconds(userAuthenticationTimeout)
            }

            generator.init(builder.build())
            generator.generateKey()
        }
        return keyStore.getKey(alias, null)
    }

}
