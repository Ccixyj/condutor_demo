package me.xyj.conduct.demo.glide.support

import com.bumptech.glide.manager.Lifecycle
import com.bumptech.glide.manager.LifecycleListener
import com.bumptech.glide.util.Util
import java.util.*

/**
 * A [com.bumptech.glide.manager.Lifecycle] implementation for tracking and notifying
 * listeners of [com.bluelinelabs.conductor.Controller] lifecycle events.
 */
class ControllerLifecycle : Lifecycle {

    private val lifecycleListeners = Collections.newSetFromMap(WeakHashMap<LifecycleListener, Boolean>())
    private var isStarted: Boolean = false
    private var isDestroyed: Boolean = false

    /**
     * Adds the given listener to the list of listeners to be notified on each lifecycle event.
     *
     *  The latest lifecycle event will be called on the given listener synchronously in this
     * method. If the activity or fragment is stopped, [LifecycleListener.onStop]} will be
     * called, and same for onStart and onDestroy.
     *
     *  Note - [com.bumptech.glide.manager.LifecycleListener]s that are added more than once
     * will have their lifecycle methods called more than once. It is the caller's responsibility to
     * avoid adding listeners multiple times.
     */
    override fun addListener(listener: LifecycleListener) {
        lifecycleListeners.add(listener)

        when {
            isDestroyed -> listener.onDestroy()
            isStarted -> listener.onStart()
            else -> listener.onStop()
        }
    }

    override fun removeListener(listener: LifecycleListener) {
        lifecycleListeners.remove(listener)
    }

    fun onStart() {
        isStarted = true
        for (lifecycleListener in Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStart()
        }
    }

    fun onStop() {
        isStarted = false
        for (lifecycleListener in Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop()
        }
    }

    fun onDestroy() {
        isDestroyed = true
        for (lifecycleListener in Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy()
        }
    }
}