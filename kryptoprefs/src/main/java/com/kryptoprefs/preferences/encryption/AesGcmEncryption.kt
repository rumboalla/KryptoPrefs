package com.kryptoprefs.preferences.encryption

import android.annotation.TargetApi
import android.os.Build
import android.util.Base64
import com.kryptoprefs.preferences.key.Key
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

@TargetApi(Build.VERSION_CODES.KITKAT)
class AesGcmEncryption: Encryption {

    companion object {
        private const val CIPHER = "AES/GCM/NoPadding"
        private const val IV_SIZE = 12
        private const val TAG_SIZE = 128
    }

    init { if (Build.VERSION.SDK_INT < 19) throw RuntimeException("API 19 required.") }

    override fun tag() = CIPHER

    override fun encrypt(key: Key, text: String): String {
        val cipher = Cipher.getInstance(CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, key.get())
        return Base64.encodeToString(cipher.iv + cipher.doFinal(text.toByteArray()), Base64.NO_WRAP)
    }

    override fun decrypt(key: Key, text: String): String {
        val cipher = Cipher.getInstance(CIPHER)
        val data = Base64.decode(text, Base64.NO_WRAP)
        cipher.init(Cipher.DECRYPT_MODE, key.get(), GCMParameterSpec(TAG_SIZE, data, 0, IV_SIZE))
        return cipher.doFinal(data, IV_SIZE, data.size - IV_SIZE).toString(Charsets.UTF_8)
    }

}
