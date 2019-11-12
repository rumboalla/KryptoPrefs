package com.kryptoprefs.context.transform

/**
 * Class to transform Floats.
 */
internal class FloatTransform: Transform<Float> {

    override fun transform(t: Float?): String? = t.toString()

    override fun transform(t: String?): Float? = t?.toFloat()

}
