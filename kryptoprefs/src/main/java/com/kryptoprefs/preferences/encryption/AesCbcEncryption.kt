package com.kryptoprefs.preferences.encryption

import android.util.Base64
import com.kryptoprefs.preferences.key.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

class AesCbcEncryption: Encryption {

    companion object {
        private const val CIPHER = "AES/CBC/PKCS7Padding"
        private const val IV_SIZE = 16
    }

    override fun tag() = CIPHER

    override fun encrypt(key: Key, text: String): String {
        val cipher = Cipher.getInstance(CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, key.get())
        return Base64.encodeToString(cipher.iv + cipher.doFinal(text.toByteArray()), Base64.NO_WRAP)
    }

    override fun decrypt(key: Key, text: String): String {
        val cipher = Cipher.getInstance(CIPHER)
        val data = Base64.decode(text, Base64.NO_WRAP)
        cipher.init(Cipher.DECRYPT_MODE, key.get(), IvParameterSpec(data, 0, IV_SIZE))
        return cipher.doFinal(data, IV_SIZE, data.size - IV_SIZE).toString(Charsets.UTF_8)
    }

}
