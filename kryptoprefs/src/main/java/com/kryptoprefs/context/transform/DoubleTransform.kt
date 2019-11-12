package com.kryptoprefs.context.transform

/**
 * Class to transform Doubles.
 */
internal class DoubleTransform: Transform<Double> {

    override fun transform(t: Double?): String? = t.toString()

    override fun transform(t: String?): Double? = t?.toDouble()

}
