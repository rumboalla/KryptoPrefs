package com.kryptoprefs.preferences.encryption

import com.kryptoprefs.preferences.key.Key

class NoEncryption: Encryption {

	override fun encrypt(key: Key, text: String): String = text

	override fun decrypt(key: Key, text: String): String = text

	override fun tag() = "NoEncryption"

}