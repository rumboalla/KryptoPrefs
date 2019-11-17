package com.kryptoprefs.preferences.hash

class NoHash: Hash {

	override fun hash(text: String): String = text

	override fun tag(): String = "NoHash"

}
