package com.kryptoprefs.context.preference

/**
 * Represents a nullable preference of type T.
 * T is either {@String} or a Custom object with a {@Transform}
 */
interface NullablePreference<T> {

    /**
     * Returns the value of the preference synchronously.
     *
     * @return  {@T?}    Value of the preference.
     */
    fun get(): T?

    /**
     * Stores the value of the preference synchronously.
     *
     * @param   t       {@T?}    New value of the preference. A null value will clear the preference.
     */
    fun put(t: T?)

    /**
     * Returns the value of the preference asynchronously.
     *
     * @param  callback {@(T?) -> Unit}  Callback to be executed once the preference is retrieved asynchronously.
     *                                   This operation happens on the thread pool specified during creation of the preference.
     */
    fun getAsync(callback: (T?) -> Unit)

    /**
     * Stores the value of the preference asynchronously.
     *
     * @param  t    {@T?}    New value of the preference. A new value will clear the preference.
     *                      This operation happens on the thread pool specified during creation of the preference.
     */
    fun putAsync(t: T?)

    /**
     * Returns an observable for the preference.
     *
     * @return      {@PreferenceObservable}    Java Observable
     */
    fun observable(): PreferenceObservable<T>

}
