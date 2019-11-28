package com.kryptoprefs.gson

import com.google.gson.Gson
import com.kryptoprefs.context.transform.Transform
import java.lang.reflect.Type

class JsonTransform<T: Any>(private val type: Type, private val gson: Gson = Gson()): Transform<T> {
	override fun transform(t: T?): String? = gson.toJson(t)
	override fun transform(t: String?): T? = gson.fromJson(t, type)
}