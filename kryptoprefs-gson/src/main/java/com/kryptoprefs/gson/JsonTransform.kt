package com.kryptoprefs.gson

import com.google.gson.Gson
import com.kryptoprefs.context.transform.Transform

class JsonTransform<T: Any>(private val type: Class<T>, private val gson: Gson = Gson()): Transform<T> {
	override fun transform(t: T?): String? = gson.toJson(t)
	override fun transform(t: String?): T? = gson.fromJson(t, type)
}