package com.kryptoprefs.context.preference

/**
 * Copy of the Java Observable to handle nicely typed callbacks.
 */
open class PreferenceObservable<T> {

    private val obs = mutableSetOf<(T?) -> Unit>()
    private var changed = false

    @Synchronized
    fun addObserver(o: (T?) -> Unit): (T?) -> Unit = obs.add(o).let { o }

    @Synchronized
    fun deleteObserver(o: (T?) -> Unit) = obs.remove(o)

    @Synchronized
    fun deleteObservers() = obs.clear()

    @Suppress("MemberVisibilityCanBePrivate")
    @Synchronized
    fun hasChanged(): Boolean = changed

    @Synchronized
    fun countObservers(): Int = obs.size

    fun notifyObservers(arg: T?) {
        lateinit var copy: Array<(T?) -> Unit>

        synchronized(this) {
            if (!hasChanged()) return
            copy = obs.toTypedArray()
            clearChanged()
        }

        copy.forEach { it.invoke(arg) }
    }

    fun setChangedAndNotifyObservers(arg: T?) {
        setChanged()
        notifyObservers(arg)
        clearChanged()
    }

    @Synchronized
    protected fun setChanged() {
        changed = true
    }


    @Synchronized
    protected fun clearChanged() {
        changed = false
    }

}
