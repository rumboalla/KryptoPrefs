package com.kryptoprefs.context.transform

/**
 * Class to transform Enums.
 */
class EnumTransform<T: Enum<T>>(private val type: Class<T>): Transform<T> {

    override fun transform(t: T?): String? = t.toString()

    override fun transform(t: String?): T? = t?.let { java.lang.Enum.valueOf(type, t) }

}
