package me.xyj.conduct.demo.vm.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import leakcanary.AppWatcher
import org.koin.core.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class ViewModelController : LifecycleController, KoinComponent, CoroutineScope {


    private val viewModelStore = ViewModelStore()

    constructor() : super()

    constructor(bundle: Bundle) : super(bundle)

    override val coroutineContext: CoroutineContext = MainScope().coroutineContext


    @JvmOverloads
    fun viewModelProvider(
        factory: ViewModelProvider.NewInstanceFactory = ViewModelProvider.AndroidViewModelFactory(
            activity!!.application
        )
    ): ViewModelProvider {
        return ViewModelProvider(viewModelStore, factory)
    }

    fun viewModelProvider(factory: ViewModelProvider.Factory): ViewModelProvider {
        return ViewModelProvider(viewModelStore, factory)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        this.coroutineContext.cancel()
        AppWatcher.objectWatcher.watch(this)
    }


    fun requireActivity(): FragmentActivity {
        return activity.takeIf { it != null } as? FragmentActivity
            ?: error("Fragment $this not attached to an activity.")
    }

    companion object {
        fun ViewModelController.of(factory: ViewModelProvider.Factory? = null): ViewModelProvider {
            return if (factory == null) this.viewModelProvider()
            else this.viewModelProvider(factory)
        }
    }
}
