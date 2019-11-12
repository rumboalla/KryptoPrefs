package com.kryptoprefs.preferences

import android.annotation.TargetApi
import android.content.Context
import android.security.keystore.KeyProperties
import com.kryptoprefs.preferences.encryption.AesCbcEncryption
import com.kryptoprefs.preferences.encryption.AesGcmEncryption
import com.kryptoprefs.preferences.hash.Sha512Hash
import com.kryptoprefs.preferences.key.HybridKey
import com.kryptoprefs.preferences.key.KeyStoreKey
import com.kryptoprefs.preferences.key.PbeKey
import com.kryptoprefs.preferences.key.Pbkdf2Key

object KryptoBuilder {

    @JvmStatic
    fun pref(context: Context, name: String, pass: String, salt: String, api: Int) : KryptoPrefs = when {
        api < 10 -> KryptoPrefsImpl(context, name, AesCbcEncryption(), Sha512Hash(), PbeKey(pass, salt))
        api < 19 -> KryptoPrefsImpl(context, name, AesCbcEncryption(), Sha512Hash(), Pbkdf2Key(pass, salt))
        else -> KryptoPrefsImpl(context, name, AesGcmEncryption(), Sha512Hash(), Pbkdf2Key(pass, salt))
    }

    @JvmStatic
    @TargetApi(23)
    fun keystore(context: Context, name: String) : KryptoPrefs  {
        val key = KeyStoreKey(name, KeyProperties.BLOCK_MODE_GCM, KeyProperties.ENCRYPTION_PADDING_NONE)
        return KryptoPrefsImpl(context, name, AesGcmEncryption(), Sha512Hash(), key)
    }

    @JvmStatic
    @TargetApi(23)
    fun hybrid(context: Context, name: String) : KryptoPrefs {
        val key = HybridKey(context, name)
        return KryptoPrefsImpl(context, name, AesGcmEncryption(), Sha512Hash(), key)
    }

}
