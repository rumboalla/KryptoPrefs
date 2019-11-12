package com.kryptoprefs.preferences.key

import android.annotation.TargetApi
import android.content.Context
import com.kryptoprefs.preferences.KryptoBuilder
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@TargetApi(23)
class HybridKey(context: Context, name: String): Key {

    companion object {
        private const val ALGORITHM = "PBKDF2WithHmacSHA1"
        private const val TAG = "HybridKey"
        private const val ITERATIONS = 1000
        private const val KEY_SIZE = 256
    }

    private val passKey = "$TAG-$name-pass"
    private val saltKey = "$TAG-$name-salt"
    private val prefs = KryptoBuilder.keystore(context, "$name-hybrid")
    private lateinit var finalKey: SecretKey

    override fun get(): java.security.Key = if (::finalKey.isInitialized) finalKey
    else if (prefs.contains(passKey) && prefs.contains(saltKey)) {
        val p = prefs.getString(passKey, null) ?: throw IllegalStateException("Wrong pass.")
        val s = prefs.getString(saltKey, null) ?: throw IllegalStateException("Wrong salt.")
        val keySpec = PBEKeySpec(p.toCharArray(), s.toByteArray(), ITERATIONS, KEY_SIZE)
        finalKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec)
        finalKey
    } else {
        val random = SecureRandom()
        val pass = ByteArray(256)
        val salt = ByteArray(256)
        random.nextBytes(pass)
        random.nextBytes(salt)
        prefs.putString(passKey, pass.toString(Charsets.UTF_8))
        prefs.putString(saltKey, salt.toString(Charsets.UTF_8))
        get()
    }

    override fun tag() = TAG

}
