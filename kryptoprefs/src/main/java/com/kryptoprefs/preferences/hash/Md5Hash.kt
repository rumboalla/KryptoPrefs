package com.kryptoprefs.preferences.hash

import android.util.Base64
import java.security.MessageDigest

class Md5Hash: Hash {

    companion object { private const val HASH = "MD5" }

    private val digest = MessageDigest.getInstance(HASH)

    override fun hash(text: String): String = Base64.encodeToString(digest.digest(text.toByteArray(Charsets.UTF_8)), Base64.NO_WRAP)

    override fun tag(): String = HASH

}
