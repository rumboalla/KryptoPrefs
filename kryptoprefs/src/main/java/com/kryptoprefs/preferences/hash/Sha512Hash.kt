package com.kryptoprefs.preferences.hash

import android.util.Base64
import java.security.MessageDigest

class Sha512Hash: Hash {

    companion object { private const val HASH = "SHA-512" }

    private val digest = MessageDigest.getInstance(HASH)

    override fun hash(text: String): String = Base64.encodeToString(digest.digest(text.toByteArray(Charsets.UTF_8)), Base64.NO_WRAP)

    override fun tag(): String = HASH

}
