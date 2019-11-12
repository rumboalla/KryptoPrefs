package com.kryptoprefs.context.transform

/**
 * Class to transform Bytes.
 */
internal class ByteTransform: Transform<Byte> {

    override fun transform(t: Byte?): String? = t.toString()

    override fun transform(t: String?): Byte? = t?.toByte()

}
