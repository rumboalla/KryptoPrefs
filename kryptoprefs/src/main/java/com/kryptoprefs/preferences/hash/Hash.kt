package com.kryptoprefs.preferences.hash

interface Hash {

    fun hash(text: String): String

    fun tag(): String

}
