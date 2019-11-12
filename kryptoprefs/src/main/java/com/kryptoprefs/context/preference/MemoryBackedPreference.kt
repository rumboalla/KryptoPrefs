package com.kryptoprefs.context.preference

import com.kryptoprefs.context.transform.Transform
import com.kryptoprefs.preferences.KryptoPrefs
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MemoryBackedPreference<T: Any> @JvmOverloads constructor(
        private val prefs: KryptoPrefs,
        private val key: String,
        private val defValue: T,
        private val transform: Transform<T>? = null,
        private val pool: ExecutorService = Executors.newSingleThreadExecutor())
: Preference<T>, PreferenceObservable<T>() {

    private val queue = QueueOfFutures()
    private lateinit var value: T
    private var invalid = true

    override fun get(): T {
        queue.sync()

        if (!::value.isInitialized || invalid) {
            value = when (defValue) {
                is Int -> prefs.getInt(key, defValue as Int) as T
                is Long -> prefs.getLong(key, defValue as Long) as T
                is Boolean -> prefs.getBoolean(key, defValue as Boolean) as T
                is String -> prefs.getString(key, defValue as String) as T
                else -> transform?.transform(prefs.getString(key, transform.transform(defValue).orEmpty())) as T
            }
            invalid = false
        }

        return value
    }

    override fun put(t: T?) {
        value = t ?: defValue

        when (defValue) {
            is Int -> prefs.putInt(key, t as Int?)
            is Long -> prefs.putLong(key, t as Long?)
            is Boolean -> prefs.putBoolean(key, t as Boolean?)
            is String -> prefs.putString(key, t as String)
            else -> prefs.putString(key, transform?.transform(t).orEmpty())
        }

        setChanged()
        notifyObservers(t)
        clearChanged()
    }

    override fun putAsync(t: T?) {
        queue.add(pool.submit { put(t) })
    }

    override fun getAsync(callback: (T) -> Unit) {
        thread { callback.invoke(get()) }
    }

    override fun observable(): PreferenceObservable<T> = this

    fun invalidate() {
        invalid = true
    }

}
