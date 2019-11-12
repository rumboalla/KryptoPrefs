package com.kryptoprefs.context.transform

import java.util.Date

/**
 * Class to transform Dates.
 */
internal class DateTransform: Transform<Date> {

    override fun transform(t: Date?): String? = t?.time.toString()

    override fun transform(t: String?): Date? = t?.let { Date(it.toLong()) }

}
