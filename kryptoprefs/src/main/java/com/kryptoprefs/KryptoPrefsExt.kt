package com.kryptoprefs

import com.kryptoprefs.context.preference.NullablePreference
import com.kryptoprefs.context.preference.Preference

/**
 * Overloads () operator to return get().
 */
operator fun <T> Preference<T>.invoke(): T = get()

/**
 * Overloads () operator to return get().
 */
operator fun <T> NullablePreference<T>.invoke(): T? = get()

/**
 * Overloads (T?) operator to put a value.
 */
operator fun <T> NullablePreference<T>.invoke(o: T?): T? = put(o).let { o }

/**
 * Overloads (T) operator to put a value.
 */
operator fun <T> Preference<T>.invoke(o: T): T = put(o).let { o }

/**
 * Overloads += to add an observer.
 */
operator fun <T> Preference<T>.plusAssign(callback: (T?) -> Unit) {
    this.observable().addObserver(callback)
}

/**
 * Overloads -= to remove an observer.
 */
operator fun <T> Preference<T>.minusAssign(callback: (T?) -> Unit) {
    this.observable().deleteObserver(callback)
}

/**
 * Overloads += to add an observer.
 */
operator fun <T> NullablePreference<T>.plusAssign(callback: (T?) -> Unit) {
    this.observable().addObserver(callback)
}

/**
 * Overloads -= to remove an observer.
 */
operator fun <T> NullablePreference<T>.minusAssign(callback: (T?) -> Unit) {
    this.observable().deleteObserver(callback)
}

/**
 * Shortcut for observable().deleteObservers()
 */
fun <T> Preference<T>.deleteObservers() = this.observable().deleteObservers()

/**
 * Shortcut for observable().deleteObservers()
 */
fun <T> NullablePreference<T>.deleteObservers() = this.observable().deleteObservers()

/**
 * Shortcut for observable().addObserver()
 */
fun <T> Preference<T>.addObserver(callback: (T?) -> Unit) {
    this.observable().addObserver(callback)
}

/**
 * Shortcut for observable().deleteObserver()
 */
fun <T> Preference<T>.deleteObserver(callback: (T?) -> Unit) {
    this.observable().deleteObserver(callback)
}

/**
 * Shortcut for observable().addObserver()
 */
fun <T> NullablePreference<T>.addObserver(callback: (T?) -> Unit) {
    this.observable().addObserver(callback)
}

/**
 * Shortcut for observable().deleteObserver()
 */
fun <T> NullablePreference<T>.deleteObserver(callback: (T?) -> Unit) {
    this.observable().deleteObserver(callback)
}
