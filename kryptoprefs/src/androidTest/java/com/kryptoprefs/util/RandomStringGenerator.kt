package com.kryptoprefs.util

import java.util.Random

object RandomStringGenerator {

    private const val source = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val random = Random()

    @JvmStatic
    @Synchronized
    fun generate(len: Int): String {
        val sb = StringBuilder(len)
        for (i in 0..len) sb.append(source[random.nextInt(source.length)])
        return sb.toString()
    }

}
