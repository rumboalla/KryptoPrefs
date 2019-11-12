package com.kryptoprefs.preferences.compression

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Deflater.DEFAULT_COMPRESSION
import java.util.zip.Inflater

object DeflateHelper {

    private const val BUFFER_SIZE = 1024

    fun deflate(data: String, mode: Int = DEFAULT_COMPRESSION): String {
        val deflater = Deflater(mode)
        val stream = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        deflater.setInput(data.toByteArray())
        deflater.finish()
        while (!deflater.finished()) { stream.write(buffer, 0, deflater.deflate(buffer)) }
        deflater.end()
        stream.close()
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }

    fun inflate(data: String, mode: Int = DEFAULT_COMPRESSION): String {
        val inflater = Inflater()
        val stream = ByteArrayOutputStream(data.length)
        val buffer = ByteArray(BUFFER_SIZE)
        inflater.setInput(Base64.decode(data, Base64.NO_WRAP))
        while (!inflater.finished()) { stream.write(buffer, 0, inflater.inflate(buffer)) }
        inflater.end()
        stream.close()
        return stream.toByteArray().toString(Charsets.UTF_8)
    }

}
