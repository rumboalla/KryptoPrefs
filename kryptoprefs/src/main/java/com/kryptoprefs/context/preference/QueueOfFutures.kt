package com.kryptoprefs.context.preference

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Future
import java.util.concurrent.locks.ReentrantLock

internal class QueueOfFutures {

    private val futureQueue : ConcurrentLinkedQueue<Future<*>> = ConcurrentLinkedQueue()
    private val mutex = ReentrantLock()

    fun add(future: Future<*>) {
        mutex.lock()
        futureQueue.add(future)
        mutex.unlock()
    }

    fun sync() {
        mutex.lock()
        futureQueue.forEach { it.get() }
        futureQueue.removeAll { it.isDone }
        mutex.unlock()
    }

}
