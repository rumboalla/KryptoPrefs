package com.kryptoprefs.preferences.key

interface Key {

    fun get(): java.security.Key

    fun tag(): String

}
