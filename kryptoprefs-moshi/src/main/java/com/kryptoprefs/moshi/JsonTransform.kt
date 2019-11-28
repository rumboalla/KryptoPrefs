package com.kryptoprefs.moshi

import com.kryptoprefs.context.transform.Transform
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

var defaultMoshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

class JsonTransform<T: Any>(type: Class<T>, moshi: Moshi = defaultMoshi): Transform<T> {
	private val adapter = moshi.adapter(type)
	override fun transform(t: T?): String? = adapter.toJson(t)
	override fun transform(t: String?): T? = t?.let { adapter.fromJson(t) }
}