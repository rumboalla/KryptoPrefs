package com.kryptoprefs.context.transform

/**
 * Class to transform Shorts.
 */
internal class ShortTransform: Transform<Short> {

    override fun transform(t: Short?): String? = t.toString()

    override fun transform(t: String?): Short? = t?.toShort()

}
