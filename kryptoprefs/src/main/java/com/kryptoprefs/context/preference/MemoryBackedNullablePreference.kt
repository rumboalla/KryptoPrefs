package com.kryptoprefs.context.preference

import com.kryptoprefs.context.transform.Transform
import com.kryptoprefs.preferences.KryptoPrefs
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MemoryBackedNullablePreference<T: Any> @JvmOverloads constructor(
        private val prefs: KryptoPrefs,
        private val key: String,
        private val defValue: T?,
        private val transform: Transform<T>? = null,
        private val pool: ExecutorService = Executors.newSingleThreadExecutor())
: NullablePreference<T>, PreferenceObservable<T>() {

    private val queue = QueueOfFutures()
    private var value: T? = null

    override fun get(): T? {
        queue.sync()

        if (value == null) {
            value = if(transform == null) {
                prefs.getString(key, defValue as String?) as T?
            } else {
                transform.transform(prefs.getString(key, defValue?.let { transform.transform(defValue) }))
            }
        }

        return value
    }

    override fun put(t: T?) {
        value = t

        if (transform == null) {
            prefs.putString(key, t as String?)
        } else {
            prefs.putString(key, t?.let { transform.transform(t) })
        }

        setChanged()
        notifyObservers(t)
        clearChanged()
    }

    override fun putAsync(t: T?) {
        queue.add(pool.submit { put(t) })
    }

    override fun getAsync(callback: (T?) -> Unit) {
        thread { callback.invoke(get()) }
    }

    override fun observable(): PreferenceObservable<T> = this

    fun invalidate() {
        value = null
    }

}
