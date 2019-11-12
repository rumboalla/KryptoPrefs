package com.kryptoprefs.context.transform

/**
 * Class to transform Chars.
 */
internal class CharTransform: Transform<Char> {

    override fun transform(t: Char?): String? = t.toString()

    override fun transform(t: String?): Char? = t?.single()

}
