package com.kryptoprefs.preferences.key

class NoKey : Key {

    override fun get(): java.security.Key = object : java.security.Key {
        override fun getAlgorithm(): String? = null
        override fun getEncoded(): ByteArray? = null
        override fun getFormat(): String? = null
    }

    override fun tag() = "NoKey"

}
